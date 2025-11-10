package persistence.impl;
import domain.models.Usuario;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository; // Se agrega esta importación
import persistence.dao.UsuarioDAO;

@Repository // Se añade la anotación
public class UsuarioDAOHibernateJPA extends GenericDAOHibernateJPA<Usuario> implements UsuarioDAO {
    public UsuarioDAOHibernateJPA() {
        super(Usuario.class);
    }

    @Override
    public Usuario findByEmail(String email) {
        // Se elimina la creación manual del EntityManager y el try/finally
        try {
            TypedQuery<Usuario> consulta = getEntityManager().createQuery( // Se usa getEntityManager()
                    "SELECT u FROM Usuario u WHERE u.email = :emailParam", Usuario.class);
            consulta.setParameter("emailParam", email);

            return consulta.getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }
}