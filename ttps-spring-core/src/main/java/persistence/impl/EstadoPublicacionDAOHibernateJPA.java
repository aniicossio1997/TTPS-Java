package persistence.impl;

import domain.models.EstadoPublicacion;
import org.springframework.stereotype.Repository; // Se agrega esta importación
import persistence.dao.EstadoPublicacionDAO;

@Repository // Se añade la anotación
public class EstadoPublicacionDAOHibernateJPA  extends GenericDAOHibernateJPA<EstadoPublicacion> implements EstadoPublicacionDAO {

    public EstadoPublicacionDAOHibernateJPA() {
        super(EstadoPublicacion.class);
    }
}