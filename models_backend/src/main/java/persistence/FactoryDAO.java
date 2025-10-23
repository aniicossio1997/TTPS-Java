package persistence;

import persistence.dao.*;
import persistence.impl.*;

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

    public static FotoDAO getFotoDAO() {
        return new FotoDAOHibernateJPA();
    }
    public static AvistamientoDAO getAvistamientoDAO() {
        return new AvistamientoDAOHibernateJPA();
    }

    // Aquí agregarías métodos para otros DAOs
    // public static MedallaDAO getMedallaDAO() {
    //    return new MedallaDAOHibernateJPA();
    // }
}