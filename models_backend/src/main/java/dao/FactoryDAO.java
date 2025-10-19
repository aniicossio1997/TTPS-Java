package dao;

import dao.implementations.MedallaDAOHibernateJPA;
import dao.interfaces.IMedallaDao;

public class FactoryDAO {

/*    public static IFamiliaProductoraDao createFamiliaProductoraDao() {
        return new FamiliaProductoraDao();
    }*/
    public static IMedallaDao createMedallaDao() {
        return new MedallaDAOHibernateJPA();
    }

}
