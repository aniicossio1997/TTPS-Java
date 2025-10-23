package persistence.impl;

import domain.models.Avistamiento;
import domain.models.Ubicacion;
import persistence.dao.AvistamientoDAO;

public class AvistamientoDAOHibernateJPA extends GenericDAOHibernateJPA<Avistamiento> implements AvistamientoDAO {
    public AvistamientoDAOHibernateJPA() {
        super(Avistamiento.class);
    }
}
