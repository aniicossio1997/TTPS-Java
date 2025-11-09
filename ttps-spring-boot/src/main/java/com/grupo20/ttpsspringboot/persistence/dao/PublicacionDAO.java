package com.grupo20.ttpsspringboot.persistence.dao;

import com.grupo20.ttpsspringboot.domain.models.Publicacion;
import com.grupo20.ttpsspringboot.dtos.PublicacionFilterDTO;

import java.util.Date;
import java.util.List;

public interface PublicacionDAO extends GenericDAO<Publicacion> {
    public List<Publicacion> getPublicacionesByNombre(String nombre);

    public List<Publicacion> getPublicacionesByUsuario(Long usuarioId);

    public List<Publicacion> getPublicacionesByCaracteristicas(PublicacionFilterDTO filter);
}