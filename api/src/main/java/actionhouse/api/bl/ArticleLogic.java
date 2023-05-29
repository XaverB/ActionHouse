package actionhouse.api.bl;

import actionhouse.api.domain.Article;
import actionhouse.api.domain.ArticleStatus;

import java.util.List;

public interface ArticleLogic {
    List<Article> getArticles(ArticleStatus status, String searchTerm, Double maxPrice);
    Long createArticle(Article article, String seller);

    void deleteArticle(Long id, String email);

    void startAuction(Long id, String email);

    void stopAuction(Long id, String email);
}
