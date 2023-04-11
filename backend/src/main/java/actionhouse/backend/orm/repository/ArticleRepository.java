package actionhouse.backend.orm.repository;

import actionhouse.backend.bl.ArticleOrder;
import actionhouse.backend.orm.domain.Article;
import actionhouse.backend.orm.domain.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.List;

public class ArticleRepository extends BaseRepository<Article> implements IArticleRepository {
    public ArticleRepository(EntityManager entityManager) {
        super(Article.class, entityManager);
    }

    public List<Article> getTopArticles(int count) {
        return entityManager.createQuery("SELECT a FROM Article a ORDER BY a.hammerPrice - a.reservePrice DESC"
                        , Article.class)
                .setMaxResults(count)
                .getResultList();
    }

    @Override
    public List<Article> findArticlesByDescription(String searchPhrase, Double maxReservePrice) {
        TypedQuery<Article> query = null;

        boolean hasPriceCondition = maxReservePrice != null && maxReservePrice > 0;
        if (hasPriceCondition) {
            query = entityManager.createQuery("SELECT a " +
                                    "FROM Article a " +
                                    "WHERE a.description LIKE :searchPhrase " +
                                    "AND a.reservePrice <= :maxReservePrice "
                            , Article.class)
                    .setParameter("searchPhrase", "%" + searchPhrase + "%")
                    .setParameter("maxReservePrice", maxReservePrice);

        } else {
            query = entityManager.createQuery("SELECT a " +
                                    "FROM Article a " +
                                    "WHERE a.description LIKE :searchPhrase "
                            , Article.class)
                    .setParameter("searchPhrase", "%" + searchPhrase + "%");

        }

        return query.getResultList();
    }

}
