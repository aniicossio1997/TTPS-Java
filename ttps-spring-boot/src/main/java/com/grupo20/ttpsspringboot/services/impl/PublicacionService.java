package com.grupo20.ttpsspringboot.services.impl;

import com.grupo20.ttpsspringboot.domain.constants.Puntuacion;
import com.grupo20.ttpsspringboot.domain.enums.EstadoPublicacionEnum;
import com.grupo20.ttpsspringboot.domain.enums.RolUsuarioEnum;
import com.grupo20.ttpsspringboot.domain.models.*;
import com.grupo20.ttpsspringboot.dtos.PublicacionCreateDTO;
import com.grupo20.ttpsspringboot.dtos.PublicacionFilterDTO;
import com.grupo20.ttpsspringboot.dtos.PublicacionUpdateDTO;
import com.grupo20.ttpsspringboot.exceptions.ForbiddenException;
import com.grupo20.ttpsspringboot.exceptions.NotFoundException;
import com.grupo20.ttpsspringboot.persistence.dao.UsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.grupo20.ttpsspringboot.persistence.dao.PublicacionDAO;
import com.grupo20.ttpsspringboot.services.UbicacionService;

import java.util.Date;
import java.util.List;

@Service
public class PublicacionService {

    @Autowired
    private PublicacionDAO publicacionDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private UbicacionService ubicacionService;

    @Autowired
    private PuntuacionService puntuacionService;

    @Transactional
    public Publicacion create(Usuario usuario, PublicacionCreateDTO dto) {
        Publicacion publicacion = dto.toEntity();
        publicacion.setUsuario(usuario);

        Ubicacion ubicacion = ubicacionService.crearUbicacion(dto.getUbicacion());

        publicacion.setUbicacion(ubicacion);

        publicacionDAO.persist(publicacion);

        puntuacionService.otorgarPuntosPorReporte(usuario);

        return publicacion;
    }

    @Transactional
    public Publicacion get(Long id) {
        Publicacion publicacion = publicacionDAO.get(id);
        if (publicacion == null) {
            throw new NotFoundException();
        }
        return publicacion;
    }

    @Transactional
    public List<Publicacion> getFiltered(PublicacionFilterDTO filter) {
        return publicacionDAO.getPublicacionesByCaracteristicas(filter);
    }

    @Transactional
    public Publicacion update(Long id, Usuario usuario, PublicacionUpdateDTO dto) {
        Publicacion publicacion = publicacionDAO.get(id);
        validate(publicacion, usuario);

        if (dto.getNombre() != null) publicacion.setNombre(dto.getNombre());
        if (dto.getDescripcion() != null) publicacion.setDescripcion(dto.getDescripcion());
        if (dto.getColor() != null) publicacion.setColor(dto.getColor());
        if (dto.getEspecie() != null) publicacion.setEspecie(dto.getEspecie());
        if (dto.getRaza() != null) publicacion.setRaza(dto.getRaza());
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
            ubicacionService.updateUbicacion(publicacion.getUbicacion().getId(), dto.getUbicacion());
        }

        return publicacionDAO.update(publicacion);
    }

    @Transactional
    public void delete(Long id, Usuario usuario) {
        Publicacion publicacion = publicacionDAO.get(id);
        validate(publicacion, usuario);
        publicacion.setDeletedAt(new Date());
        publicacionDAO.update(publicacion);
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
