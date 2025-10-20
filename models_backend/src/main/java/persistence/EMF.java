package persistence;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;

/**
 * Clase utilitaria para gestionar el EntityManagerFactory (Singleton).
 * Basado en la teoría, página 7[cite: 246, 248].
 */
public class EMF {

    private static EntityManagerFactory em = null; // [cite: 249]

    static { // [cite: 250]
        try {
            // "unlp" es el nombre de la persistence-unit en persistence.xml [cite: 255, 283]
            em = Persistence.createEntityManagerFactory("unlp");
        } catch (PersistenceException e) { // [cite: 256]
            System.err.println("Error al crear EntityManagerFactory: " + e.getMessage()); // [cite: 257, 259]
            e.printStackTrace(); // [cite: 258]
        }
    }

    public static EntityManagerFactory getEMF() { // [cite: 260]
        return em; // [cite: 262]
    }

    private EMF() {} // [cite: 263]
}