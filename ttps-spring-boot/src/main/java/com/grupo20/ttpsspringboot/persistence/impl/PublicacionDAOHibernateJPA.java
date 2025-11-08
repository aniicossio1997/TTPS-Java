package com.grupo20.ttpsspringboot.persistence.impl;

import com.grupo20.ttpsspringboot.domain.models.Publicacion;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import com.grupo20.ttpsspringboot.persistence.dao.PublicacionDAO;

import java.util.Date;
import java.util.List;

@Repository // Se añade la anotación
public class PublicacionDAOHibernateJPA extends GenericDAOHibernateJPA<Publicacion> implements PublicacionDAO {

    public PublicacionDAOHibernateJPA() {
        super(Publicacion.class);
    }

    @Override
    public List<Publicacion> getPublicacionesByNombre(String nombre) {
        // Se elimina la creación manual del EntityManager y el try/finally
        TypedQuery<Publicacion> consulta = getEntityManager().createQuery(
                "SELECT p FROM Publicacion p WHERE LOWER(p.nombre) LIKE LOWER(:nombreParam)", Publicacion.class);
        consulta.setParameter("nombreParam", "%" + nombre + "%");
        return consulta.getResultList();
    }

    @Override
    public List<Publicacion> getPublicacionesByUsuario(Long usuarioId) {
        // Se elimina la creación manual del EntityManager y el try/finally
        TypedQuery<Publicacion> consulta = getEntityManager().createQuery(
                "SELECT p FROM Publicacion p WHERE p.usuario.id = :usuarioId", Publicacion.class);
        consulta.setParameter("usuarioId", usuarioId);
        return consulta.getResultList();
    }

    @Override
    public List<Publicacion> getPublicacionesByCaracteristicas(
            String nombre,
            String especie,
            String raza,
            String tamanio,
            String color,
            Date fechaDesde,
            Date fechaHasta,
            int offset,
            int maxResults
    ) {
        // Se elimina la creación manual del EntityManager y el try/finally
        String jpql = "SELECT p FROM Publicacion p WHERE 1=1";

        if (nombre != null && !nombre.isEmpty()) {
            jpql += " AND LOWER(p.nombre) LIKE LOWER(:nombreParam)";
        }
        // ... (resto de condiciones)
        if (especie != null && !especie.isEmpty()) {
            jpql += " AND p.especie = :especieParam";
        }
        if (raza != null && !raza.isEmpty()) {
            jpql += " AND p.raza = :razaParam";
        }
        if (tamanio != null && !tamanio.isEmpty()) {
            jpql += " AND p.tamanio = :tamanioParam";
        }
        if (color != null && !color.isEmpty()) {
            jpql += " AND p.color = :colorParam";
        }
        if (fechaDesde != null && fechaHasta != null) {
            jpql += " AND p.fecha BETWEEN :fechaDesdeParam AND :fechaHastaParam";
        }

        jpql += " ORDER BY p.fecha DESC";

        TypedQuery<Publicacion> query = getEntityManager().createQuery(jpql, Publicacion.class);

        // ... (asignación de parámetros)
        if (nombre != null && !nombre.isEmpty()) {
            query.setParameter("nombreParam", "%" + nombre + "%");
        }
        if (especie != null && !especie.isEmpty()) {
            query.setParameter("especieParam", especie);
        }
        if (raza != null && !raza.isEmpty()) {
            query.setParameter("razaParam", raza);
        }
        if (tamanio != null && !tamanio.isEmpty()) {
            query.setParameter("tamanioParam", tamanio);
        }
        if (color != null && !color.isEmpty()) {
            query.setParameter("colorParam", color);
        }
        if (fechaDesde != null && fechaHasta != null) {
            query.setParameter("fechaDesdeParam", fechaDesde);
            query.setParameter("fechaHastaParam", fechaHasta);
        }

        query.setFirstResult(offset);
        query.setMaxResults(maxResults);

        return query.getResultList();
    }
}