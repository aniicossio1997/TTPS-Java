package com.grupo20.ttpsspringboot.persistence.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.transaction.annotation.Transactional;
import com.grupo20.ttpsspringboot.persistence.dao.GenericDAO;

import java.util.List;

/**
 * Implementación genérica del DAO gestionada por Spring.
 */
// 1. Hacemos que todos los métodos públicos de esta clase (y sus hijas)
//    sean transaccionales. 
@Transactional
public class GenericDAOHibernateJPA<T> implements GenericDAO<T> {

    protected Class<T> persistentClass;

    // 2. Inyectamos el EntityManager. Spring lo obtiene del

    @PersistenceContext
    private EntityManager entityManager;

    public GenericDAOHibernateJPA(Class<T> clase) {
        this.persistentClass = clase;
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    // --- Métodos CRUD ahora son mucho más simples ---
    // ¡Ya no necesitas EntityTransaction ni try-catch-finally para la tx!

    @Override
    public T persist(T entity) {
        // Spring ya inició la transacción
        this.entityManager.persist(entity);
        // Spring hará el commit (o rollback si hay error)
        return entity;
    }

    @Override
    public T update(T entity) {
        T entityMerged = this.entityManager.merge(entity);
        return entityMerged;
    }

    @Override
    public void delete(T entity) {
        this.entityManager.remove(this.entityManager.merge(entity));
    }

    @Override
    public void delete(Long id) {
        T entity = this.get(id);
        if (entity != null) {
            this.delete(entity);
        } else {
            throw new EntityNotFoundException(
                    "No se encontró la entidad " + persistentClass.getSimpleName() + " con ID: " + id
            );
        }
    }

    // 3. Los métodos de consulta no necesitan @Transactional
    //    pero lo heredan de la clase. Usamos @Transactional(readOnly = true)
    //    para optimizar la consulta.
    @Override
    @Transactional(readOnly = true)
    public T get(Long id) {
        // Ya no necesitas crear y cerrar el 'em'
        return this.entityManager.find(this.getPersistentClass(), id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getAll(String columnOrder) {
        String queryString = "SELECT e FROM " + getPersistentClass().getSimpleName() + " e";
        if (columnOrder != null && !columnOrder.isEmpty()) {
            queryString += " order by e." + columnOrder;
        }
        Query consulta = this.entityManager.createQuery(queryString);
        return (List<T>) consulta.getResultList();
    }

    public List<T> getAll() {
        return getAll(null);
    }

    // 4. (Opcional) Un getter para que las clases hijas usen el EM
    public EntityManager getEntityManager() {
        return entityManager;
    }
}