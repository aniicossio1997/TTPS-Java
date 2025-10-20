package persistence;

import persistence.dao.UbicacionDAO;
import persistence.impl.UbicacionDAOHibernateJPA;

public class FactoryDAO {

    public static UbicacionDAO getUbicacionDAO() { // [cite: 266]
        return new UbicacionDAOHibernateJPA(); // [cite: 268]
    }

    // Aquí agregarías métodos para otros DAOs
    // public static MedallaDAO getMedallaDAO() {
    //    return new MedallaDAOHibernateJPA();
    // }
}