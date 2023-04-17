package actionhouse.backend.orm.repository;

import java.util.List;

public interface IBaseRepository<T> {
    T getById(long id);

    List<T> getAll();

    T save(T entity);

    T update(T entity);

    void delete(T entity);
}
