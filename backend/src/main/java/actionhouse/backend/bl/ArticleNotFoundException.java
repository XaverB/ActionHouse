package actionhouse.backend.bl;

import actionhouse.backend.orm.domain.Article;

public class ArticleNotFoundException extends Exception {
    public ArticleNotFoundException(Long id) {
        super("Article (%d) not found. ".formatted(id));
    }

}
