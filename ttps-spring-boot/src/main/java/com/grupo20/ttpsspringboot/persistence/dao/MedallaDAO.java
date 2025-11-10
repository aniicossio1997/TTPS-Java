package com.grupo20.ttpsspringboot.persistence.dao;

import com.grupo20.ttpsspringboot.domain.models.Medalla;
import com.grupo20.ttpsspringboot.domain.models.Publicacion;

import java.util.List;

public interface MedallaDAO extends  GenericDAO<Medalla>{
    public List<Medalla> getByUsuarioId(Long usuarioId);
}
