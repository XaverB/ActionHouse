package actionhouse.backend.orm.repository;

import actionhouse.backend.orm.domain.Article;
import actionhouse.backend.orm.domain.Customer;
import jakarta.persistence.EntityManager;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.List;

public class ArticleRepository extends BaseRepository<Article> {
    public ArticleRepository(EntityManager entityManager) {
        super(Article.class, entityManager);
    }

    public List<Article> getTopArticles(int count) {
        return entityManager.createQuery("SELECT a FROM Article a ORDER BY a.hammerPrice - a.reservePrice DESC"
                        , Article.class)
                .setMaxResults(count)
                .getResultList();
    }

}
