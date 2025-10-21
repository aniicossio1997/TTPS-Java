package persistence.impl;

import domain.models.Ubicacion;
import persistence.EMF;
import persistence.dao.UbicacionDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.util.List;

/**
 * Implementación específica para Ubicacion[cite: 63].
 * Extiende la genérica e implementa la interfaz específica[cite: 186, 188].
 */
public class UbicacionDAOHibernateJPA extends GenericDAOHibernateJPA<Ubicacion> implements UbicacionDAO {

    public UbicacionDAOHibernateJPA() {
        super(Ubicacion.class);
    }

    @Override
    public List<Ubicacion> findByProvincia(String provincia) {
        EntityManager em = EMF.getEMF().createEntityManager();
        try {
            Query consulta = em.createQuery("SELECT u FROM " + getPersistentClass().getSimpleName() +
                    " u WHERE u.provincia = :prov");
            consulta.setParameter("prov", provincia);
            return (List<Ubicacion>) consulta.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Ubicacion findByIdExterno(String idExterno) {
        EntityManager em = EMF.getEMF().createEntityManager();
        Ubicacion ubi = null;
        try {
            // Lógica similar a getByEmail de la teoría [cite: 196, 199]
            Query consulta = em.createQuery("SELECT u FROM " + getPersistentClass().getSimpleName() +
                    " u WHERE u.idExterno = :idExt");
            consulta.setParameter("idExt", idExterno);
            ubi = (Ubicacion) consulta.getSingleResult(); // [cite: 199]
        } catch (NoResultException e) {
            ubi = null; // [cite: 201]
        } finally {
            em.close(); // [cite: 203]
        }
        return ubi; // [cite: 205]
    }
}