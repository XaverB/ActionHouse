package actionhouse.backend.orm.repository;

import actionhouse.backend.orm.domain.Article;
import jakarta.persistence.EntityManager;

public class ArticleRepository extends BaseRepository<Article> {
    public ArticleRepository(EntityManager entityManager) {
        super(Article.class, entityManager);
    }
}
