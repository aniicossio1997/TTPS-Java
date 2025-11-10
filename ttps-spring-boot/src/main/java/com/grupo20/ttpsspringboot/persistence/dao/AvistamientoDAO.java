package com.grupo20.ttpsspringboot.persistence.dao;


import com.grupo20.ttpsspringboot.domain.models.Avistamiento;
import com.grupo20.ttpsspringboot.dtos.AvistamientoFilterDTO;

import java.util.List;

public interface AvistamientoDAO  extends GenericDAO<Avistamiento> {

    public List<Avistamiento> getByFilters(AvistamientoFilterDTO filter);
}
