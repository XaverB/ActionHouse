package actionhouse.backend.orm.jpa;

import jakarta.persistence.EntityManager;

@FunctionalInterface

public interface EntityManagerTaskWithResult<T> {
        T execute(EntityManager em);
}
