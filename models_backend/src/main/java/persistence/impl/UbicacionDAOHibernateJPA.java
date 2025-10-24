package persistence.impl;

import domain.models.Ubicacion;
import jakarta.persistence.TypedQuery;
import persistence.EMF;
import persistence.dao.UbicacionDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<Ubicacion> findByCriteriaLike(String idExterno, String provincia, String ciudad, String barrio ) {
        EntityManager em = EMF.getEMF().createEntityManager();
        try {
            // Construcción dinámica de la consulta JPQL
            StringBuilder jpql = new StringBuilder("SELECT u FROM Ubicacion u WHERE 1=1");
            Map<String, String> parameters = new HashMap<>();

            if (provincia != null && !provincia.trim().isEmpty()) {
                jpql.append(" AND LOWER(u.provincia) LIKE LOWER(:provinciaParam)");
                parameters.put("provinciaParam", "%" + provincia.trim() + "%");
            }
            if (ciudad != null && !ciudad.trim().isEmpty()) {
                jpql.append(" AND LOWER(u.ciudad) LIKE LOWER(:ciudadParam)");
                parameters.put("ciudadParam", "%" + ciudad.trim() + "%");
            }
            if (barrio != null && !barrio.trim().isEmpty()) {
                jpql.append(" AND LOWER(u.barrio) LIKE LOWER(:barrioParam)");
                parameters.put("barrioParam", "%" + barrio.trim() + "%");
            }

            if (idExterno != null && !idExterno.trim().isEmpty()) {
                jpql.append(" AND LOWER(u.idExterno) LIKE LOWER(:idExternoParam)");
                parameters.put("idExternoParam", "%" + idExterno.trim() + "%");
            }
            // --- FIN CONDICIÓN ID EXTERNO ---

            // Crear la consulta tipada
            TypedQuery<Ubicacion> query = em.createQuery(jpql.toString(), Ubicacion.class);

            // Asignar los parámetros que se añadieron
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }

            return query.getResultList();

        } finally {
            em.close();
        }

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