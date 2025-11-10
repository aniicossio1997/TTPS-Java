package com.grupo20.ttpsspringboot.persistence.impl;

import com.grupo20.ttpsspringboot.domain.models.Avistamiento;
import com.grupo20.ttpsspringboot.domain.models.Publicacion;
import com.grupo20.ttpsspringboot.dtos.AvistamientoFilterDTO;
import com.grupo20.ttpsspringboot.persistence.dao.AvistamientoDAO;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class AvistamientoDAOHibernateJPA extends GenericDAOHibernateJPA<Avistamiento> implements AvistamientoDAO {

    public AvistamientoDAOHibernateJPA() {
        super(Avistamiento.class);
    }

    @Override
    public List<Avistamiento> getByFilters(AvistamientoFilterDTO filter) {
        String jpql = "SELECT a FROM "  + getPersistentClass().getSimpleName() + " a WHERE 1=1";

        if (filter.usuarioId != null) {
            jpql += " AND a.usuario.id = :usuarioId";
        }

        if (filter.publicacionId != null) {
            jpql += " AND a.publicacion.id = :publicacionId AND a.publicacion.deletedAt IS NULL";
        }

        TypedQuery<Avistamiento> query = getEntityManager().createQuery(jpql, getPersistentClass());

        if (filter.usuarioId != null) {
            query.setParameter("usuarioId", filter.usuarioId);
        }

        if (filter.publicacionId != null) {
            query.setParameter("publicacionId", filter.publicacionId);
        }

        return query.getResultList();
    }
}