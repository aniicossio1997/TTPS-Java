package com.grupo20.ttpsspringboot.persistence.dao;

import com.grupo20.ttpsspringboot.domain.models.Usuario;

public interface UsuarioDAO extends GenericDAO<Usuario>{

    public Usuario findByEmail(String email);
}
