package persistence.impl;
import domain.models.Publicacion;
import jakarta.persistence.*;
import persistence.EMF;
import persistence.dao.PublicacionDAO;

import java.util.Date;
import java.util.List;

public class PublicacionDAOHibernateJPA extends GenericDAOHibernateJPA<Publicacion> implements PublicacionDAO {

    public PublicacionDAOHibernateJPA() {
        super(Publicacion.class);
    }

    @Override
    public List<Publicacion> getPublicacionesByNombre(String nombre) {
        EntityManager em = EMF.getEMF().createEntityManager();
        try {
            // Se utiliza LIKE para buscar coincidencias parciales en el nombre
            TypedQuery<Publicacion> consulta = em.createQuery(
                    "SELECT p FROM Publicacion p WHERE LOWER(p.nombre) LIKE LOWER(:nombreParam)", Publicacion.class);
            consulta.setParameter("nombreParam", "%" + nombre + "%");
            return consulta.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Publicacion> getPublicacionesByUsuario(Long usuarioId) {
        EntityManager em = EMF.getEMF().createEntityManager();
        try {
            // Se busca por el ID de la relación ManyToOne 'usuario'
            TypedQuery<Publicacion> consulta = em.createQuery(
                    "SELECT p FROM Publicacion p WHERE p.usuario.id = :usuarioId", Publicacion.class);
            consulta.setParameter("usuarioId", usuarioId);
            return consulta.getResultList();
        } finally {
            em.close();
        }
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
        EntityManager em = EMF.getEMF().createEntityManager();
        try {
            String jpql = "SELECT p FROM Publicacion p WHERE 1=1";

            //  Agregar condiciones
            if (nombre != null && !nombre.isEmpty()) {
                jpql += " AND LOWER(p.nombre) LIKE LOWER(:nombreParam)";
            }
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

            TypedQuery<Publicacion> query = em.createQuery(jpql, Publicacion.class);

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

            // Aplicar Paginación
            query.setFirstResult(offset);
            query.setMaxResults(maxResults);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

}