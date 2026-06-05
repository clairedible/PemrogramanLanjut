package repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
    boolean existsById(ID id);
    void save(T entity);
    void update(ID id, T entity);
    void deleteById(ID id);
}
