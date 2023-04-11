package actionhouse.backend.orm.repository;

import actionhouse.backend.bl.ArticleOrder;
import actionhouse.backend.orm.domain.Article;

import java.util.List;

public interface IArticleRepository extends IBaseRepository<Article> {
    List<Article> getTopArticles(int count);

    List<Article> findArticlesByDescription(String searchPhrase, Double
            maxReservePrice);
}
