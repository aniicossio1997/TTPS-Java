package persistence.impl;
import domain.models.Publicacion;
import persistence.dao.PublicacionDAO;

public class PublicacionDAOHibernateJPA extends GenericDAOHibernateJPA<Publicacion> implements PublicacionDAO {
    public PublicacionDAOHibernateJPA() {
        super(Publicacion.class);
    }

}
