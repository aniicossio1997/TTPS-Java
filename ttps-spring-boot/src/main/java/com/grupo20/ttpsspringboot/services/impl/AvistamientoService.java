package com.grupo20.ttpsspringboot.services.impl;

import com.grupo20.ttpsspringboot.domain.models.*;
import com.grupo20.ttpsspringboot.dtos.AvistamientoCreateDTO;
import com.grupo20.ttpsspringboot.dtos.AvistamientoDTO;
import com.grupo20.ttpsspringboot.dtos.AvistamientoFilterDTO;
import com.grupo20.ttpsspringboot.dtos.AvistamientoUpdateDTO;
import com.grupo20.ttpsspringboot.exceptions.NotFoundException;
import com.grupo20.ttpsspringboot.persistence.repository.AvistamientoRepository;
import com.grupo20.ttpsspringboot.persistence.repository.PublicacionRepository;
import com.grupo20.ttpsspringboot.services.IAvistamientoService;
import io.jsonwebtoken.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
public class AvistamientoService implements IAvistamientoService {

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private AvistamientoRepository avistamientoRepository;

    @Autowired
    private UbicacionService ubicacionService;

    @Transactional
    public Avistamiento create(Usuario usuario, AvistamientoCreateDTO dto,  List<MultipartFile> files) {
        Avistamiento avistamiento = dto.toEntity();
        avistamiento.setUsuario(usuario);

        avistamiento.setFecha(new Date());

        Publicacion publicacion = publicacionRepository.findById(dto.getPublicacionId())
                .orElseThrow(() -> new NotFoundException("Publicación no encontrada"));

        avistamiento.setPublicacion(publicacion);

        Ubicacion ubicacion = ubicacionService.crearUbicacion(dto.getUbicacion());

        avistamiento.setUbicacion(ubicacion);

        /*if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    try {
                        Foto nuevaFoto = new Foto();
                        nuevaFoto.setNombre(file.getOriginalFilename());
                        nuevaFoto.setContent(file.getBytes());
                        nuevaFoto.setAvistamiento(avistamiento);
                        avistamiento.addFoto(nuevaFoto);
                    } catch (IOException | java.io.IOException e) {
                        throw new RuntimeException("Error al leer el contenido del archivo: " + file.getOriginalFilename(), e);
                    }
                }
            }
        }*/

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    try {
                        Foto nuevaFoto = new Foto();

                        // Asignar los datos del archivo
                        nuevaFoto.setNombre(file.getOriginalFilename());
                        nuevaFoto.setContent(file.getBytes()); // Usando la lógica de guardar bytes

                        // Asignar las relaciones
                        nuevaFoto.setAvistamiento(avistamiento); // lado dueño

                        // Añadir la foto a la colección (setFotos de Avistamiento usa add/list)
                        avistamiento.getFotos().add(nuevaFoto);

                    } catch (Exception e) {
                        throw new RuntimeException("Error al leer el contenido del archivo: " + file.getOriginalFilename(), e);
                    }
                }
            }
        }

        avistamientoRepository.save(avistamiento);

        return avistamiento;
    }

    @Transactional
    public List<Avistamiento> getFiltered(AvistamientoFilterDTO dto) {
       return avistamientoRepository.getByFilters(dto);
    }

    @Transactional
    public Avistamiento get(Long id) {
        Avistamiento avistamiento = avistamientoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Avistamiento no encontrado"));;

        return  avistamiento;
    }

    @Transactional
    public AvistamientoDTO update(Long id, AvistamientoUpdateDTO dto, List<MultipartFile> files) {

        Avistamiento avistamiento = avistamientoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Avistamiento no encontrado con ID: " + id));

        if (dto.getDescripcion() != null) avistamiento.setDescripcion(dto.getDescripcion());
        if (dto.getFecha() != null) avistamiento.setFecha(dto.getFecha());
        if (dto.getAgradecimiento() != null) avistamiento.setAgradecimiento(dto.getAgradecimiento());

        // Actualizar Ubicación si viene en el DTO
        if (dto.getUbicacion() != null) {
            ubicacionService.updateUbicacion(avistamiento.getUbicacion().getId(), dto.getUbicacion());
        }

        // Limpiamos la colección. Esto es lo que activa orphanRemoval de JPA.
        avistamiento.getFotos().clear();

        if (files != null && !files.isEmpty()) {

            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    try {
                        Foto nuevaFoto = new Foto();
                        nuevaFoto.setNombre(file.getOriginalFilename());
                        nuevaFoto.setContent(file.getBytes());
                        nuevaFoto.setAvistamiento(avistamiento);

                        avistamiento.getFotos().add(nuevaFoto);

                    } catch (Exception e) {
                        throw new RuntimeException("Error al leer el contenido del archivo: " + file.getOriginalFilename(), e);
                    }
                }
            }
        }
        Avistamiento savedAvistamiento = avistamientoRepository.save(avistamiento);

        return AvistamientoDTO.fromEntity(savedAvistamiento);
    }

}
