package actionhouse.backend.bl;

import actionhouse.backend.orm.domain.Article;

public class ArticleNotFoundException extends Exception {
    public ArticleNotFoundException(Article article) {
        super("Article not found: " + article);
    }

}
