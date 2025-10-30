package persistence.impl;

import domain.models.Avistamiento;
import domain.models.Ubicacion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import persistence.EMF;
import persistence.dao.AvistamientoDAO;

import java.util.List;

public class AvistamientoDAOHibernateJPA extends GenericDAOHibernateJPA<Avistamiento> implements AvistamientoDAO {
    public AvistamientoDAOHibernateJPA() {
        super(Avistamiento.class);
    }


    @Override
    public List<Avistamiento> getByIdPublicacion(Long publicacionId) {
        EntityManager em = EMF.getEMF().createEntityManager();
        try {
            // Creamos la consulta JPQL.
            // "getPersistentClass().getSimpleName()" se resolverá a "Avistamiento".
            // "a.publicacion.id" navega la relación hasta el ID de la publicación.
            String jpql = "SELECT a FROM " + getPersistentClass().getSimpleName() +
                    " a WHERE a.publicacion.id = :pubId";

            Query consulta = em.createQuery(jpql);
            consulta.setParameter("pubId", publicacionId); // Asignamos el parámetro

            return (List<Avistamiento>) consulta.getResultList();

        } finally {
            em.close(); // Cerramos el EntityManager
        }
    }
}
