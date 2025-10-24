package persistence;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;

/**
 * Clase utilitaria para gestionar el EntityManagerFactory (Singleton).
 * Basado en la teoría, página 7[cite: 246, 248].
 */
public  class EMF {

    private static EntityManagerFactory em = null;

    static {
        try {
            // "unlp" es el nombre de la persistence-unit en persistence.xml [cite: 255, 283]
            em = Persistence.createEntityManagerFactory("miUP");
        } catch (PersistenceException e) { // [cite: 256]
            System.err.println("Error al crear EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static EntityManagerFactory getEMF() { // [cite: 260]
        return em;
    }

    private EMF() {}
}