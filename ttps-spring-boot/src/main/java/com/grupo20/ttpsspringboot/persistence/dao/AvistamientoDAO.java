package com.grupo20.ttpsspringboot.persistence.dao;


import com.grupo20.ttpsspringboot.domain.models.Avistamiento;

import java.util.List;

public interface AvistamientoDAO  extends GenericDAO<Avistamiento> {
    public List<Avistamiento> getByIdPublicacion(Long publicacionId);
}
