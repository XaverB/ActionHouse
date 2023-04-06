package actionhouse.backend.orm.repository;

import actionhouse.backend.util.JpaUtil;
import jakarta.persistence.EntityManager;

/**
 * Base class for all repositories.
 * Provides basic CRUD operations.
 *
 * @param <T> Type parameter class
 */
public abstract class BaseRepository<T> {
    // Java Generics do not know about runtime types, so we need to pass the type
    // parameter class to the constructor
    // See https://stackoverflow.com/a/3437930
    /**
     * Type parameter class
     */
    protected final Class<T> typeParameterClass;

    /**
     * Injected EntityManager for shared transactions
     */
    protected final EntityManager entityManager;

    /**
     * Constructor
     *
     * @param typeParameterClass Type parameter class
     * @param entityManager      Injected EntityManager for shared transactions
     */
    public BaseRepository(Class<T> typeParameterClass, EntityManager entityManager) {
        this.typeParameterClass = typeParameterClass;
        this.entityManager = entityManager;
    }


    /**
     * Get entity by id
     */
    public T getById(long id) {
        return entityManager.find(typeParameterClass, id);
    }

    /**
     * Save entity
     */
    public T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    /**
     * Update entity
     */
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    /**
     * Delete entity
     */
    public void delete(T entity) {
        entityManager.remove(entity);
    }
}
