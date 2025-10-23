package persistence.impl;

import domain.models.Foto;
import jakarta.persistence.*;
import persistence.EMF;
import persistence.dao.FotoDAO;

import java.util.List;

public class FotoDAOHibernateJPA extends GenericDAOHibernateJPA<Foto> implements FotoDAO {
    public FotoDAOHibernateJPA() {
        super(Foto.class);
    }
    @Override
    public List<Foto> getFotosByPublicacion(Long publicacionId) {
        EntityManager em = EMF.getEMF().createEntityManager();
        try {
            TypedQuery<Foto> consulta = em.createQuery(
                    "SELECT f FROM Foto f WHERE f.publicacion.id = :pubId", Foto.class);
            consulta.setParameter("pubId", publicacionId);
            return consulta.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Foto> getFotosByAvistamiento(Long avistamientoId) {
        EntityManager em = EMF.getEMF().createEntityManager();
        try {
            TypedQuery<Foto> consulta = em.createQuery(
                    "SELECT f FROM Foto f WHERE f.avistamiento.id = :avistamientoId", Foto.class);
            consulta.setParameter("avistamientoId", avistamientoId);
            return consulta.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Foto getFotoByUsuario(Long usuarioId) {
        EntityManager em = EMF.getEMF().createEntityManager();
        Foto foto = null;
        try {
            TypedQuery<Foto> consulta = em.createQuery(
                    "SELECT f FROM Foto f WHERE f.usuario.id = :usuarioId", Foto.class);
            consulta.setParameter("usuarioId", usuarioId);
            foto = consulta.getSingleResult();
        } catch (NoResultException e) {
            foto = null;
        } finally {
            em.close();
        }
        return foto;
    }
}
