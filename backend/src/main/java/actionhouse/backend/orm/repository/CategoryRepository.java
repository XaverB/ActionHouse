package actionhouse.backend.orm.repository;

import actionhouse.backend.orm.domain.Category;
import jakarta.persistence.EntityManager;

public class CategoryRepository extends BaseRepository<Category> {
    public CategoryRepository(EntityManager entityManager) {
        super(Category.class, entityManager);
    }
}
