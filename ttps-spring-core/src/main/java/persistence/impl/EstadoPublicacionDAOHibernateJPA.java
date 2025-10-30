package persistence.impl;

import domain.models.EstadoPublicacion;
import persistence.dao.EstadoPublicacionDAO;

public class EstadoPublicacionDAOHibernateJPA  extends GenericDAOHibernateJPA<EstadoPublicacion> implements EstadoPublicacionDAO {

    public EstadoPublicacionDAOHibernateJPA() {
        super(EstadoPublicacion.class);
    }

}
