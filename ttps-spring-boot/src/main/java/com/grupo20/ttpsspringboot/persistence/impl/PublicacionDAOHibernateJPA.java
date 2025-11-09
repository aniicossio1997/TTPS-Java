package com.grupo20.ttpsspringboot.persistence.impl;

import com.grupo20.ttpsspringboot.domain.models.Publicacion;
import com.grupo20.ttpsspringboot.dtos.PublicacionFilterDTO;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import com.grupo20.ttpsspringboot.persistence.dao.PublicacionDAO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository // Se añade la anotación
public class PublicacionDAOHibernateJPA extends GenericDAOHibernateJPA<Publicacion> implements PublicacionDAO {

    public PublicacionDAOHibernateJPA() {
        super(Publicacion.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Publicacion get(Long id) {
        TypedQuery<Publicacion> consulta = getEntityManager().createQuery(
                "SELECT p FROM Publicacion p WHERE p.id = :id AND p.deletedAt IS NULL",
                Publicacion.class
        );
        consulta.setParameter("id", id);
        return consulta.getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<Publicacion> getPublicacionesByNombre(String nombre) {
        // Se elimina la creación manual del EntityManager y el try/finally
        TypedQuery<Publicacion> consulta = getEntityManager().createQuery(
                "SELECT p FROM Publicacion p WHERE p.usuario.id = :usuarioId AND p.deletedAt IS NULL", Publicacion.class);
        consulta.setParameter("nombreParam", "%" + nombre + "%");
        return consulta.getResultList();
    }

    @Override
    public List<Publicacion> getPublicacionesByUsuario(Long usuarioId) {
        // Se elimina la creación manual del EntityManager y el try/finally
        TypedQuery<Publicacion> consulta = getEntityManager().createQuery(
                "SELECT p FROM Publicacion p WHERE p.usuario.id = :usuarioId AND p.deletedAt IS NULL", Publicacion.class);
        consulta.setParameter("usuarioId", usuarioId);
        return consulta.getResultList();
    }

    @Override
    public List<Publicacion> getPublicacionesByCaracteristicas(PublicacionFilterDTO filter) {
        // Se elimina la creación manual del EntityManager y el try/finally
        String jpql = "SELECT p FROM Publicacion p WHERE p.deletedAt IS NULL";

        if (filter.nombre != null && !filter.nombre.isEmpty()) {
            jpql += " AND LOWER(p.nombre) LIKE LOWER(:nombreParam)";
        }
        // ... (resto de condiciones)
        if (filter.especie != null && !filter.especie.isEmpty()) {
            jpql += " AND p.especie = :especieParam";
        }
        if (filter.raza != null && !filter.raza.isEmpty()) {
            jpql += " AND p.raza = :razaParam";
        }
        if (filter.tamanio != null && !filter.tamanio.isEmpty()) {
            jpql += " AND p.tamanio = :tamanioParam";
        }
        if (filter.color != null && !filter.color.isEmpty()) {
            jpql += " AND p.color = :colorParam";
        }

        if (filter.usuarioId != null) {
            jpql += " AND p.usuario.id = :usuarioId";
        }

        if (filter.fechaDesde != null && filter.fechaHasta != null) {
            jpql += " AND p.fecha BETWEEN :fechaDesdeParam AND :fechaHastaParam";
        }

        jpql += " ORDER BY p.fecha DESC";

        TypedQuery<Publicacion> query = getEntityManager().createQuery(jpql, Publicacion.class);

        // ... (asignación de parámetros)
        if (filter.nombre != null && !filter.nombre.isEmpty()) {
            query.setParameter("nombreParam", "%" + filter.nombre + "%");
        }
        if (filter.especie != null && !filter.especie.isEmpty()) {
            query.setParameter("especieParam", filter.especie);
        }
        if (filter.raza != null && !filter.raza.isEmpty()) {
            query.setParameter("razaParam", filter.raza);
        }
        if (filter.tamanio != null && !filter.tamanio.isEmpty()) {
            query.setParameter("tamanioParam", filter.tamanio);
        }
        if (filter.color != null && !filter.color.isEmpty()) {
            query.setParameter("colorParam", filter.color);
        }

        if (filter.usuarioId != null) {
            query.setParameter("usuarioId", filter.usuarioId);
        }

        if (filter.fechaDesde != null && filter.fechaHasta != null) {
            query.setParameter("fechaDesdeParam", filter.fechaDesde);
            query.setParameter("fechaHastaParam", filter.fechaHasta);
        }

        query.setFirstResult(filter.offset);
        query.setMaxResults(filter.maxResults);

        return query.getResultList();
    }
}