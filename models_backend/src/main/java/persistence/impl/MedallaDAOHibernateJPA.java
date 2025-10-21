package persistence.impl;

import domain.models.Medalla;
import persistence.dao.MedallaDAO;

public class MedallaDAOHibernateJPA extends GenericDAOHibernateJPA<Medalla> implements MedallaDAO {
    public MedallaDAOHibernateJPA() {
        super(Medalla.class);
    }

}
