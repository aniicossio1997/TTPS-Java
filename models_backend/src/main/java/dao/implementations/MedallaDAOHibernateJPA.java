package dao.implementations;

import dao.GenericDAOHibernateJPA;
import dao.interfaces.IMedallaDao;
import domain.Medalla;

public class MedallaDAOHibernateJPA extends GenericDAOHibernateJPA<Medalla> implements IMedallaDao {

    protected boolean getDeletable() {
        return true;
    }


}
