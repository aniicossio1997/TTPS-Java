package com.grupo20.ttpsspringboot.persistence.dao;

import com.grupo20.ttpsspringboot.domain.models.Ubicacion;

import java.util.List;

/**
 * Interfaz específica para Ubicacion.
 * 
 */
public interface UbicacionDAO extends GenericDAO<Ubicacion> {


    public List<Ubicacion> findByProvincia(String provincia);

    public List<Ubicacion> findByCriteriaLike(String idExterno, String provincia, String ciudad, String barrio );

    public Ubicacion getByUsuarioId(Long usuarioId);
}