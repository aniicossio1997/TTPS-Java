package persistence;

import persistence.dao.*;
import persistence.impl.*;

public class FactoryDAO {

    public static UbicacionDAO getUbicacionDAO() { 
        return new UbicacionDAOHibernateJPA(); 
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

    public static MedallaDAO getMedallaDAO() {
       return new MedallaDAOHibernateJPA();
    }

    public static EstadoPublicacionDAO getEstadoPublicacionDAO() {
        return new EstadoPublicacionDAOHibernateJPA();
    }
}