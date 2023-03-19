package actionhouse.backend.orm.repository;

import actionhouse.backend.util.JpaUtil;
import jakarta.persistence.EntityManager;

/**
 * Base class for all repositories.
 * Provides basic CRUD operations.
 *
 * @param <T>
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


    public BaseRepository(Class<T> typeParameterClass, EntityManager entityManager) {
        this.typeParameterClass = typeParameterClass;
        this.entityManager = entityManager;
    }


    /**
     * Get entity by id in a transaction
     */
    public T getByIdTransactional(long id) {
        return JpaUtil.executeWithResultTransactional(entityManager,
                em -> em.find(typeParameterClass, id)
        );
    }

    /**
     * Get entity by id
     */
    public T getById(long id) {
        return entityManager.find(typeParameterClass, id);
    }

    /**
     * Save entity in a transaction
     */
    public void saveTransactional(T entity) {
        JpaUtil.executeTransactional(entityManager,
                em -> em.persist(entity)
        );
    }

    /**
     * Save entity
     */
    public void save(T entity) {
        entityManager.persist(entity);
    }

    /**
     * Update entity in a transaction
     */
    public T updateTransactional(T entity) {
        return JpaUtil.executeWithResultTransactional(entityManager,
                em -> em.merge(entity)
        );
    }

    /**
     * Update entity
     */
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    /**
     * Delete entity in a transaction
     */
    public void deleteTransactional(T entity) {
        JpaUtil.executeTransactional(entityManager,
                em -> em.remove(entity)
        );
    }

    /**
     * Delete entity
     */
    public void delete(T entity) {
        entityManager.remove(entity);
    }
}
