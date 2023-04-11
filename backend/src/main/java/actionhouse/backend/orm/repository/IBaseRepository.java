package actionhouse.backend.orm.repository;

public interface IBaseRepository<T> {
    T getById(long id);

    T save(T entity);

    T update(T entity);

    void delete(T entity);
}
