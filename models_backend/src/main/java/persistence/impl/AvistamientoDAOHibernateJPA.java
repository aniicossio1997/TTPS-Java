package persistence.impl;

import domain.models.Avistamiento;
import domain.models.Ubicacion;
import persistence.dao.AvistamientoDAO;

public class AvistamientoDAOHibernateJPA extends GenericDAOHibernateJPA<Avistamiento> implements AvistamientoDAO {
    public AvistamientoDAOHibernateJPA(Class<Avistamiento> clase) {
        super(Avistamiento.class);
    }
}
