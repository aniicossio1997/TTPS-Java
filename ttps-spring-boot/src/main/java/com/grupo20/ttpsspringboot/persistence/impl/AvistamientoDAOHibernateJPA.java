package com.grupo20.ttpsspringboot.persistence.impl;

import com.grupo20.ttpsspringboot.domain.models.Avistamiento;
import com.grupo20.ttpsspringboot.persistence.dao.AvistamientoDAO;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class AvistamientoDAOHibernateJPA extends GenericDAOHibernateJPA<Avistamiento> implements AvistamientoDAO {

    public AvistamientoDAOHibernateJPA() {
        super(Avistamiento.class);
    }

    @Override
    public List<Avistamiento> getByIdPublicacion(Long publicacionId) {
        // 2. Ya no creas un EntityManager, usas el que inyectó Spring
        //    en la clase padre.

        String jpql = "SELECT a FROM " + getPersistentClass().getSimpleName() +
                " a WHERE a.publicacion.id = :pubId";

        Query consulta = getEntityManager().createQuery(jpql); // Usas el EM del padre
        consulta.setParameter("pubId", publicacionId);

        return (List<Avistamiento>) consulta.getResultList();
    }
}