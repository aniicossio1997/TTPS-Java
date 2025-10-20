package persistence.impl;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import persistence.EMF;
import persistence.dao.GenericDAO;

import java.util.List;

/**
 * Implementación genérica del DAO usando Hibernate y JPA.
 * Basado en la teoría, página 5[cite: 99].
 */
public class GenericDAOHibernateJPA<T> implements GenericDAO<T> {

    protected Class<T> persistentClass; // [cite: 102]

    public GenericDAOHibernateJPA(Class<T> clase) {
        this.persistentClass = clase; // [cite: 103]
    }

    public Class<T> getPersistentClass() { // [cite: 104]
        return persistentClass; // [cite: 106]
    }

    @Override
    public T persist(T entity) { // [cite: 108]
        EntityManager em = EMF.getEMF().createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin(); // [cite: 112]
            em.persist(entity); // [cite: 113]
            tx.commit(); // [cite: 114]
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback(); // [cite: 116]
            throw e;
        } finally {
            em.close(); // [cite: 118]
        }
        return entity; // [cite: 119]
    }

    @Override
    public T update(T entity) { // [cite: 123]
        EntityManager em = EMF.getEMF().createEntityManager();
        EntityTransaction etx = em.getTransaction();
        T entityMerged = null;
        try {
            etx.begin(); // [cite: 126]
            entityMerged = em.merge(entity); // [cite: 127]
            etx.commit(); // [cite: 128]
        } catch (RuntimeException e) {
            if (etx != null && etx.isActive()) etx.rollback();
            throw e;
        } finally {
            em.close(); // [cite: 129]
        }
        return entityMerged; // [cite: 130]
    }

    @Override
    public void delete(T entity) { // [cite: 132]
        EntityManager em = EMF.getEMF().createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin(); // [cite: 138]
            em.remove(em.merge(entity)); // [cite: 139]
            tx.commit(); // [cite: 140]
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback(); // [cite: 142]
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long id) {
        T entity = this.get(id);
        if (entity != null) {
            this.delete(entity);
        }
    }

    @Override
    public T get(Long id) {
        EntityManager em = EMF.getEMF().createEntityManager();
        try {
            return em.find(this.getPersistentClass(), id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> getAll(String columnOrder) { // [cite: 153]
        EntityManager em = EMF.getEMF().createEntityManager();
        try {
            String queryString = "SELECT e FROM " + getPersistentClass().getSimpleName() + " e"; // [cite: 158, 159]
            if (columnOrder != null && !columnOrder.isEmpty()) {
                queryString += " order by e." + columnOrder; // [cite: 160]
            }
            Query consulta = em.createQuery(queryString);
            return (List<T>) consulta.getResultList(); // [cite: 161]
        } finally {
            em.close();
        }
    }

    public List<T> getAll() {
        // Llama al otro método 'getAll' pasándole null
        return getAll(null);
    }
}