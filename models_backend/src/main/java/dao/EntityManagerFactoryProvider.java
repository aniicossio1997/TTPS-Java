package dao;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerFactoryProvider  {
    protected EntityManagerFactory emf;
    private static final String UNIDAD_DE_PERSISTENCIA = "miUP";

    @PostConstruct
    public void setup() {
        emf = Persistence.createEntityManagerFactory(UNIDAD_DE_PERSISTENCIA);
    }

    public EntityManagerFactory provide() {
        return emf;
    }

    public void dispose(EntityManagerFactory instance) {
    }

}
