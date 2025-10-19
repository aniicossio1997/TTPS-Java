package dao;

import dao.interfaces.IGenericDao;
import jakarta.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.lang.reflect.ParameterizedType;
import java.util.List;


public abstract class GenericDAOHibernateJPA<T> implements IGenericDao<T> {


    protected EntityManager em;

    protected boolean getDeletable() {
        return false;
    }

    public GenericDAOHibernateJPA() {
        super();
        //this.em = dao.DBConnection.getInstance().getEntityManager();
    }

    @SuppressWarnings("unchecked")
    protected Class<T> getGenericClass() {
        return (Class<T>)
                ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
    }

    protected Class<T> persistentClass; public GenericDAOHibernateJPA(Class<T> clase) { this.persistentClass = clase; } public Class<T> getPersistentClass() { return persistentClass; }

    public void save(T item) {
/*        EntityManager em = EMF.getEMF().createEntityManager();
        EntityTransaction tx = null;
        try { tx = em.getTransaction(); tx.begin();
            em.persist(entity); tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
        } throw e;*/

        // escribir en un log o mostrar mensaje } finally { em.close(); } return entity;

    }

    public T getById(Long id) {
        //JPAQL
        String query = "FROM " + this.getGenericClass().getName() + " i WHERE i.id = :id";
        if (getDeletable()) query += " AND i.fechaBaja is NULL";
        TypedQuery<T> q = em.createQuery(query, this.getGenericClass());
        q.setParameter("id", id);
        return q.getSingleResult();
    }

    public List<T> getAll() {
        String query = "FROM " + this.getGenericClass().getName() + " i";
        //if (getDeletable()) query += " WHERE i.fechaBaja is NULL";
        query += " ORDER By i.id DESC";
        TypedQuery<T> q = em.createQuery(query, this.getGenericClass());
        return q.getResultList();
    }

    public List<T> getAll(boolean isActive) {

            String query = "FROM " + this.getGenericClass().getName() + " i";
            //if (isActive) query += " WHERE i.fechaBaja is NULL";
            query += " ORDER By i.id DESC";
            TypedQuery<T> q = em.createQuery(query, this.getGenericClass());
            return q.getResultList();

    }
    public T getById(Long id, boolean isActive) {
        //JPAQL
        String query = "FROM " + this.getGenericClass().getName() + " i";
        //if (isActive) query += " WHERE i.fechaBaja is NULL and i.id = :id ";

        TypedQuery<T> q = em.createQuery(query, this.getGenericClass());

        q.setParameter("id", id);


        return q.getSingleResult();
    }

}
