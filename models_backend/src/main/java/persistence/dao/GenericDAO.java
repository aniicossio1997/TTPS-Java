package persistence.dao;

import java.util.List;

public interface GenericDAO<T> {

    public T persist(T entity); // [cite: 86]

    public T update(T entity); // [cite: 87]

    public void delete(T entity); // [cite: 70]

    public void delete(Long id); // [cite: 70]

    public T get(Long id); // [cite: 84]

    public List<T> getAll(String columnOrder); // [cite: 85]
    public List<T> getAll();
}
