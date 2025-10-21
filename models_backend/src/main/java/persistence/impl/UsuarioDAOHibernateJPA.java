package persistence.impl;
import domain.models.Usuario;
import persistence.dao.UsuarioDAO;

public class UsuarioDAOHibernateJPA extends GenericDAOHibernateJPA<Usuario> implements UsuarioDAO {
    public UsuarioDAOHibernateJPA() {
        super(Usuario.class);
    }

}
