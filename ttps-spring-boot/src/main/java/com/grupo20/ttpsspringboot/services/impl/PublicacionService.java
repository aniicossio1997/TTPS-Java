package com.grupo20.ttpsspringboot.services.impl;

import com.grupo20.ttpsspringboot.domain.enums.EstadoPublicacionEnum;
import com.grupo20.ttpsspringboot.domain.enums.RolUsuarioEnum;
import com.grupo20.ttpsspringboot.domain.models.*;
import com.grupo20.ttpsspringboot.dtos.*;
import com.grupo20.ttpsspringboot.dtos.bases.PaginateBaseDTO;
import com.grupo20.ttpsspringboot.exceptions.ForbiddenException;
import com.grupo20.ttpsspringboot.exceptions.NotFoundException;
import com.grupo20.ttpsspringboot.persistence.repository.PublicacionRepository;
import com.grupo20.ttpsspringboot.services.IGeorefService;
import com.grupo20.ttpsspringboot.services.IPublicacionService;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PublicacionService implements IPublicacionService {

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private PuntuacionService puntuacionService;

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private IGeorefService georefService;


    @Transactional
    public Publicacion create(Usuario usuario, PublicacionCreateDTO dto, List<MultipartFile> files) {
        Publicacion publicacion = dto.toEntity();
        publicacion.setUsuario(usuario);

        UbicacionCreateDTO dtoUbicacionNuevo = dto.getUbicacion();
        // 3. Llamas al servicio georef para obtener la ubicación formateada
        if (dto.getUbicacion().getLatitud() != null && dto.getUbicacion().getLongitud() != null) {
            dtoUbicacionNuevo = georefService.getUbicacionFormateada(
                    dto.getUbicacion().getLatitud().toString(),
                    dto.getUbicacion().getLongitud().toString(),
                    UbicacionCreateDTO.class);

            // 4. Verificas que la respuesta no sea null y extraes los datos
            if (dtoUbicacionNuevo != null) {
                dto.setUbicacion(dtoUbicacionNuevo);
            }
        }

        Ubicacion ubicacion = ubicacionService.crearUbicacion(dto.getUbicacion());

        publicacion.setUbicacion(ubicacion);
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    try {
                        Foto nuevaFoto = new Foto();

                        // Asignar los datos del archivo
                        nuevaFoto.setNombre(file.getOriginalFilename());

                        // Usando la lógica de guardar bytes:
                        nuevaFoto.setContent(file.getBytes());

                        // Asignar las relaciones
                        nuevaFoto.setPublicacion(publicacion);

                        // Añadir la foto a la colección
                        publicacion.addFoto(nuevaFoto);

                    } catch (IOException | java.io.IOException e) {
                        throw new RuntimeException("Error al leer el contenido del archivo: " + file.getOriginalFilename(), e);
                    }
                }
            }
        }
        this.publicacionRepository.save(publicacion);

        puntuacionService.otorgarPuntosPorReporte(usuario);

        return publicacion;
    }

    @Transactional
    public Publicacion get(Long id) {
        return publicacionRepository.findActiveById(id)
                .orElseThrow(() -> new NotFoundException("Publicación no encontrada"));
    }

    @Transactional
    public PaginateBaseDTO<PublicacionDTO> getFiltered(PublicacionFilterDTO filter) {
        // 1. Crear el objeto Sort a partir de tu DTO
        Sort sort = Sort.by(
                filter.getSortDir().equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC,
                filter.getSortBy()
        );

        // 2. Crear el objeto Pageable a partir de tu DTO
        Pageable pageable = PageRequest.of(
                filter.getPage()-1,
                filter.getSize(),
                sort
        );

        // 3. Llamar al repositorio. Spring se encarga del resto.
        var pageResult = publicacionRepository.findByCaracteristicas(filter, pageable);

        // 4. Mapear el Page a tu PaginateBaseDTO
        PaginateBaseDTO<PublicacionDTO> response = new PaginateBaseDTO<>();
        response.setPage(pageResult.getNumber() +1);
        response.setSize(pageResult.getSize());
        response.setTotalElements(pageResult.getTotalElements());
        response.setElements(pageResult.getContent().stream().map(PublicacionDTO::fromEntity).toList());

        return response;
    }

    @Transactional
    public Publicacion update(Long id, Usuario usuario, PublicacionUpdateDTO dto, List<MultipartFile> files) {
        Publicacion publicacion = publicacionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Publicación no encontrada"));

        validate(publicacion, usuario);

        if (dto.getFecha() != null) publicacion.setFecha(dto.getFecha());
        if (dto.getNombre() != null) publicacion.setNombre(dto.getNombre());
        if (dto.getDescripcion() != null) publicacion.setDescripcion(dto.getDescripcion());
        if (dto.getColor() != null) publicacion.setColor(dto.getColor());
        if (dto.getEspecie() != null) publicacion.setEspecie(dto.getEspecie());
        if (dto.getTamanio() != null) publicacion.setTamanio(dto.getTamanio());
        if (dto.getEstado() != null && dto.getEstado() != publicacion.getEstadoEnum()) {
            publicacion.addEstado(new EstadoPublicacion(dto.getEstado(), publicacion));
            if (dto.getEstado() == EstadoPublicacionEnum.ADOPTADO) {
                puntuacionService.otorgarPuntajePorAdopcion(usuario);
            }

            if (dto.getEstado() == EstadoPublicacionEnum.RECUPERADO) {
                puntuacionService.otorgarPuntajesPorHallazgo(publicacion, dto.getAgradecimientos());
            }
        }

        if (dto.getUbicacion() != null) {
            UbicacionUpdateDTO dtoUbicacionNuevo = dto.getUbicacion();
            // 3. Llamas al servicio georef para obtener la ubicación formateada
            if (dto.getUbicacion().getLatitud() != null && dto.getUbicacion().getLongitud() != null) {
                 dtoUbicacionNuevo = georefService.getUbicacionFormateada(
                        dto.getUbicacion().getLatitud().toString(),
                        dto.getUbicacion().getLongitud().toString(),
                        UbicacionUpdateDTO.class);

                // 4. Verificas que la respuesta no sea null y extraes los datos
                if (dtoUbicacionNuevo != null) {
                    dto.setUbicacion(dtoUbicacionNuevo);
                }
            }
            ubicacionService.updateUbicacion(publicacion.getUbicacion().getId(), dtoUbicacionNuevo);
        }
        List<Foto> fotosAEliminar = new ArrayList<>(publicacion.getFotos());

        // Limpiamos la colección. Esto es lo que activa orphanRemoval en la BD.
        if (files != null) {
            publicacion.getFotos().clear();
        }

        if (files != null && !files.isEmpty()) {

            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {

                    try {
                        // Crear la nueva entidad Foto
                        Foto nuevaFoto = new Foto();

                        // Asignar datos del archivo
                        nuevaFoto.setNombre(file.getOriginalFilename());
                        // Opción A: Guardar los bytes en la DB (Tu lógica anterior)
                        nuevaFoto.setContent(file.getBytes());

                        // Asignar las relaciones
                        nuevaFoto.setPublicacion(publicacion);

                        // Añadir la nueva foto a la colección.
                        // Se guardará automáticamente con el save de Publicacion (por CASCADE).
                        publicacion.addFoto(nuevaFoto);

                    } catch (IOException e) {
                        throw new RuntimeException("Error al leer el contenido del archivo: " + file.getOriginalFilename(), e);
                    } catch (java.io.IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return publicacionRepository.save(publicacion);
    }

    @Transactional
    public void delete(Long id, Usuario usuario) {
        Publicacion publicacion = publicacionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Publicación no encontrada"));
        validate(publicacion, usuario);
        publicacion.setDeletedAt(new Date());
        publicacionRepository.save(publicacion);
    }

    private void validate(Publicacion publicacion, Usuario usuario) {
        if (publicacion == null) {
            throw new NotFoundException();
        }

        if (usuario.getRol() != RolUsuarioEnum.ADMINISTRADOR && !publicacion.getUsuario().getId().equals(usuario.getId())) {
            throw new ForbiddenException();
        }
    }

}
