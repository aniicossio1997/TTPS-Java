package persistence;

import persistence.dao.PublicacionDAO;
import persistence.dao.UbicacionDAO;
import persistence.dao.UsuarioDAO;
import persistence.impl.PublicacionDAOHibernateJPA;
import persistence.impl.UbicacionDAOHibernateJPA;
import persistence.impl.UsuarioDAOHibernateJPA;

public class FactoryDAO {

    public static UbicacionDAO getUbicacionDAO() { // [cite: 266]
        return new UbicacionDAOHibernateJPA(); // [cite: 268]
    }

    public static PublicacionDAO getPublicacionDAO() {
        return new PublicacionDAOHibernateJPA();
    }

    public static UsuarioDAO getUsuarioDAO() {
        return new UsuarioDAOHibernateJPA();
    }
    // Aquí agregarías métodos para otros DAOs
    // public static MedallaDAO getMedallaDAO() {
    //    return new MedallaDAOHibernateJPA();
    // }
}