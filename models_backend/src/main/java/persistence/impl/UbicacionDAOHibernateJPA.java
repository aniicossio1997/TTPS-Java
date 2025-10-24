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
    public Ubicacion update(Ubicacion u) {
        _validarParaCrear(u); // misma validación que en create
        return super.update(u); // si todo OK, usa la lógica genérica de actualización
    }
    // --------- VALIDACIÓN DE CREATE ---------
    @Override
    public Ubicacion persist(Ubicacion u) {
        _validarParaCrear(u);
        return super.persist(u); // si todo OK, usa la lógica genérica de persistencia
    }
    private void _validarParaCrear(Ubicacion u) {
        if (u == null) {
            throw new IllegalArgumentException("La ubicación no puede ser null.");
        }
        // Solo lat/long son obligatorios en tu entidad actual
        if (u.getLatitud() == null) {
            throw new IllegalArgumentException("Latitud obligatoria (no puede ser null).");
        }
        if (u.getLongitud() == null) {
            throw new IllegalArgumentException("Longitud obligatoria (no puede ser null).");
        }

    }
}