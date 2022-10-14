package carsharing.dao;

import java.util.List;
import java.util.Optional;

public interface Jpa <T> {
    Optional<T> findById(long id);
    Optional<T> findByName(String name);
    List<T> findAll();
    T insert(T entity);
    boolean delete(long id);
    boolean update(T entity);
}
