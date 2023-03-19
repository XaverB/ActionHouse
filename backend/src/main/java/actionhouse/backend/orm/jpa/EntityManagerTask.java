package actionhouse.backend.orm.jpa;

import jakarta.persistence.EntityManager;

@FunctionalInterface
public interface EntityManagerTask {
    void execute(EntityManager em);
}
