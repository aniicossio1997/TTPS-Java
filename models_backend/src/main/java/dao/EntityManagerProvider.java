package dao;

import jakarta.inject.Inject;
import org.glassfish.jersey.server.CloseableService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class EntityManagerProvider  {

    private final EntityManagerFactory emf;
    private final CloseableService closeableService;

    public EntityManagerProvider(EntityManagerFactory emf, CloseableService closeableService) {
        this.emf = emf;
        this.closeableService = closeableService;
    }

    public EntityManager provide() {
        final EntityManager em = emf.createEntityManager();
        closeableService.add(em::close);
        return em;
    }

    public void dispose(EntityManager entityManager) {
        entityManager.close();
    }

}