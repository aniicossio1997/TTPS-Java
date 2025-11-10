package com.grupo20.ttpsspringboot.persistence.dao;

import com.grupo20.ttpsspringboot.domain.models.Foto;

import java.util.List;

public interface FotoDAO extends GenericDAO<Foto> {
    public List<Foto> getFotosByPublicacion(Long publicacionId);

    public List<Foto> getFotosByAvistamiento(Long avistamientoId);

    public Foto getFotoByUsuario(Long usuarioId);
}