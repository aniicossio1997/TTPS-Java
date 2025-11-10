package com.grupo20.ttpsspringboot.persistence.impl;

import com.grupo20.ttpsspringboot.domain.models.Medalla;
import com.grupo20.ttpsspringboot.domain.models.Publicacion;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import com.grupo20.ttpsspringboot.persistence.dao.MedallaDAO;

import java.util.List;

@Repository // Se añade la anotación
public class MedallaDAOHibernateJPA extends GenericDAOHibernateJPA<Medalla> implements MedallaDAO {
    public MedallaDAOHibernateJPA() {
        super(Medalla.class);
    }

    @Override
    public List<Medalla> getByUsuarioId(Long usuarioId) {
        // Se elimina la creación manual del EntityManager y el try/finally
        TypedQuery<Medalla> query = getEntityManager().createQuery(
                "SELECT m FROM Medalla m WHERE m.usuario.id = :usuarioId", Medalla.class);
        query.setParameter("usuarioId", usuarioId);
        return query.getResultList();
    }
}