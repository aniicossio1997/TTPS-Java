package com.grupo20.ttpsspringboot.persistence.impl;

import com.grupo20.ttpsspringboot.domain.models.Medalla;
import org.springframework.stereotype.Repository;
import com.grupo20.ttpsspringboot.persistence.dao.MedallaDAO;

@Repository // Se añade la anotación
public class MedallaDAOHibernateJPA extends GenericDAOHibernateJPA<Medalla> implements MedallaDAO {
    public MedallaDAOHibernateJPA() {
        super(Medalla.class);
    }
}