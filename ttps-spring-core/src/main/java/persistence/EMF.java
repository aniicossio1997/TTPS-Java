package persistence;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;

/**
 * Clase utilitaria para gestionar el EntityManagerFactory (Singleton).
 * 
 */
public  class EMF {

    private static EntityManagerFactory em = null;

    static {
        try {
            em = Persistence.createEntityManagerFactory("miUP");
        } catch (PersistenceException e) { 
            System.err.println("Error al crear EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static EntityManagerFactory getEMF() { 
        return em;
    }

    private EMF() {}
}