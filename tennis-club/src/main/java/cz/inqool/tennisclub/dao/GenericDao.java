package cz.inqool.tennisclub.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void delete(T entity); // soft delete
}
