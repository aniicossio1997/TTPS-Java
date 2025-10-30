package persistence.impl;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import persistence.EMF;
import persistence.dao.GenericDAO;

import java.util.List;

/**
 * Implementación genérica del DAO usando Hibernate y JPA.
 */
public class GenericDAOHibernateJPA<T> implements GenericDAO<T> {

    protected Class<T> persistentClass; 

    public GenericDAOHibernateJPA(Class<T> clase) {
        this.persistentClass = clase; 
    }

    public Class<T> getPersistentClass() { 
        return persistentClass; 
    }

    @Override
    public T persist(T entity) { 
        EntityManager em = EMF.getEMF().createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin(); 
            em.persist(entity); 
            tx.commit(); 
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback(); 
            throw e;
        } finally {
            em.close(); 
        }
        return entity; 
    }

    @Override
    public T update(T entity) { 
        EntityManager em = EMF.getEMF().createEntityManager();
        EntityTransaction etx = em.getTransaction();
        T entityMerged = null;
        try {
            etx.begin(); 
            entityMerged = em.merge(entity); 
            etx.commit(); 
        } catch (RuntimeException e) {
            if (etx != null && etx.isActive()) etx.rollback();
            throw e;
        } finally {
            em.close(); 
        }
        return entityMerged; 
    }

    @Override
    public void delete(T entity) { 
        EntityManager em = EMF.getEMF().createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin(); 
            em.remove(em.merge(entity)); 
            tx.commit(); 
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback(); 
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
        } else {
            // ¡NUEVO! Lanza un error si no se encuentra la entidad
            throw new EntityNotFoundException(
                    "No se encontró la entidad " + persistentClass.getSimpleName() + " con ID: " + id
            );
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
    public List<T> getAll(String columnOrder) { 
        EntityManager em = EMF.getEMF().createEntityManager();
        try {
            String queryString = "SELECT e FROM " + getPersistentClass().getSimpleName() + " e";
            if (columnOrder != null && !columnOrder.isEmpty()) {
                queryString += " order by e." + columnOrder; 
            }
            Query consulta = em.createQuery(queryString);
            return (List<T>) consulta.getResultList(); 
        } finally {
            em.close();
        }
    }

    public List<T> getAll() {
        // Llama al otro método 'getAll' pasándole null
        return getAll(null);
    }
}