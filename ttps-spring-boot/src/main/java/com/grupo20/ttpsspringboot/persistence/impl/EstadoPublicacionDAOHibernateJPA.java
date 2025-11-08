package com.grupo20.ttpsspringboot.persistence.impl;

import com.grupo20.ttpsspringboot.domain.models.EstadoPublicacion;
import org.springframework.stereotype.Repository;
import com.grupo20.ttpsspringboot.persistence.dao.EstadoPublicacionDAO;

@Repository // Se añade la anotación
public class EstadoPublicacionDAOHibernateJPA  extends GenericDAOHibernateJPA<EstadoPublicacion> implements EstadoPublicacionDAO {

    public EstadoPublicacionDAOHibernateJPA() {
        super(EstadoPublicacion.class);
    }
}