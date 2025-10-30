package persistence.impl;
import domain.models.Publicacion;
import domain.models.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import persistence.EMF;
import persistence.dao.UsuarioDAO;

import java.util.List;

public class UsuarioDAOHibernateJPA extends GenericDAOHibernateJPA<Usuario> implements UsuarioDAO {
    public UsuarioDAOHibernateJPA() {
        super(Usuario.class);
    }

    @Override
    public Usuario findByEmail(String email) {
        EntityManager em = EMF.getEMF().createEntityManager();
        try {
            TypedQuery<Usuario> consulta = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :emailParam", Usuario.class);
            consulta.setParameter("emailParam", email); // Asignamos el valor al parámetro

            // getSingleResult() lanza NoResultException si no encuentra nada
            return consulta.getSingleResult();

        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
