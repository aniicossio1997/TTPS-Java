package persistence.impl;

import domain.models.Avistamiento;
import jakarta.persistence.EntityManager; // Ya no lo importas
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository; // Importante
// import persistence.EMF; // ¡Ya no se usa!
import persistence.dao.AvistamientoDAO;

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

        // ---- CÓDIGO ANTIGUO ----
        // EntityManager em = EMF.getEMF().createEntityManager();
        // try {
        //    ...
        // } finally {
        //    em.close();
        // }

        // ---- CÓDIGO NUEVO ----
        String jpql = "SELECT a FROM " + getPersistentClass().getSimpleName() +
                " a WHERE a.publicacion.id = :pubId";

        Query consulta = getEntityManager().createQuery(jpql); // Usas el EM del padre
        consulta.setParameter("pubId", publicacionId);

        return (List<Avistamiento>) consulta.getResultList();
    }
}