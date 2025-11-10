package persistence.dao;

import java.util.List;

public interface GenericDAO<T> {

    public T persist(T entity); 

    public T update(T entity); 

    public void delete(T entity); 

    public void delete(Long id); 

    public T get(Long id); 

    public List<T> getAll(String columnOrder); 
    public List<T> getAll();
}
