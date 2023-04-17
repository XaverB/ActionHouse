package actionhouse.app.bl;

import actionhouse.backend.orm.domain.*;
import actionhouse.backend.orm.repository.ArticleRepository;
import actionhouse.backend.orm.repository.CategoryRepository;
import actionhouse.backend.orm.repository.CustomerRepository;
import actionhouse.backend.util.JpaUtil;
import jakarta.persistence.EntityManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static actionhouse.app.util.MenuHelper.promptFor;

public class ArticleBl {
    /**
     * Reads an article from the stdin, creates a new article and saves it to the database.
     */
    public void createArticle() {
        Article article = null;
        try (EntityManager em = JpaUtil.getTransactionalEntityManager()) {
            var tx = em.getTransaction();
            try {
                article = readArticleFromStdin(em);

                ArticleRepository articleRepository = new ArticleRepository(em);
                article = articleRepository.save(article);
                tx.commit();
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
            System.out.println("🆕 Created article: " + article.getId());
        } catch (Exception ex) {
            System.out.println("Something bad happened 🥲: " + ex.getMessage());
        }
    }

    /**
     * Reads an article from the stdin, updates an existing article and saves it to the database.
     */
    public void updateArticle() {
        var articleId = readArticleIdFromStdin();
        Article article = null;

        try (EntityManager em = JpaUtil.getTransactionalEntityManager()) {
            var tx = em.getTransaction();
            try {
                article = readArticleFromStdin(em);
                article.setId(articleId);

                ArticleRepository articleRepository = new ArticleRepository(em);

                if (articleRepository.getById(articleId) == null) {
                    System.out.println("Article not found");
                    return;
                }

                article = articleRepository.update(article);
                tx.commit();
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
            System.out.println("🆕 Updated article: " + article.getId());
        } catch (Exception ex) {
            System.out.println("Something bad happened 🥲: " + ex.getMessage());
        }
    }

    /**
     * Reads an article id from the stdin, deletes an existing article and saves it to the database.
     */
    public void deleteArticle() {
        var articleId = readArticleIdFromStdin();

        try (EntityManager em = JpaUtil.getTransactionalEntityManager()) {
            var tx = em.getTransaction();
            try {
                ArticleRepository articleRepository = new ArticleRepository(em);
                Article article = articleRepository.getById(articleId);
                if (article == null) {
                    System.out.println("Article not found");
                    return;
                }

                articleRepository.delete(article);
                tx.commit();
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
            System.out.println("🗑️ Deleted article: " + articleId);
        } catch (Exception ex) {
            System.out.println("Something bad happened 🥲: " + ex.getMessage());
        }
    }

    /**
     * Reads an article id from the stdin, shows an existing article.
     */
    public void showArticle() {
        var articleId = readArticleIdFromStdin();

        try (EntityManager em = JpaUtil.getTransactionalEntityManager()) {
            var tx = em.getTransaction();
            try {
                ArticleRepository articleRepository = new ArticleRepository(em);
                Article article = articleRepository.getById(articleId);
                if (article == null) {
                    System.out.println("Article not found");
                    return;
                }

                PrintArticle(article);
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
        } catch (Exception ex) {
            System.out.println("Something bad happened 🥲: " + ex.getMessage());
        }
    }

    /**
     * Shows all articles.
     */
    public void showArticles() {
        try (EntityManager em = JpaUtil.getTransactionalEntityManager()) {
            var tx = em.getTransaction();
            try {
                ArticleRepository articleRepository = new ArticleRepository(em);
                var articles = articleRepository.getAll();
                for (Article article : articles) {
                    PrintArticle(article);
                }
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
        } catch (Exception ex) {
            System.out.println("Something bad happened 🥲: " + ex.getMessage());
        }
    }

    private void PrintArticle(Article article) {
        System.out.println("Article: " + article.getId());
        System.out.println("Description: " + article.getDescription());
        System.out.println("Hammer price: " + article.getHammerPrice());
        System.out.println("Reserve price: " + article.getReservePrice());
        System.out.println("Auction start date: " + article.getAuctionStartDate());
        System.out.println("Auction end date: " + article.getAuctionEndDate());
        System.out.println("Status: " + article.getStatus());
        System.out.println("Seller: " + article.getSeller());
        System.out.println("Buyer: " + article.getBuyer());

        System.out.println("Categories: ");
        for (Category category : article.getCategories()) {
            System.out.println("\t" + category);
        }

        System.out.println("Bids: ");
        for (Bid bid : article.getBids()) {
            System.out.println("\t" + bid);
        }
    }

    private Long readArticleIdFromStdin() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("🦲 Enter article id:");
        // read customer from stdin
        return Long.parseLong(promptFor(in, "Id"));
    }

    private Article readArticleFromStdin(EntityManager entityManager) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        // we are mixing domains here but whatever
        CustomerRepository customerRepository = new CustomerRepository(entityManager);

        System.out.println("🦲 Enter seller id:");
        String input = promptFor(in, "Id");
        Long sellerId = Long.parseLong(input);
        Customer seller = customerRepository.getById(sellerId);
        if(seller == null)
            throw new IllegalArgumentException("Seller not found");

        System.out.println("🦲 Enter buyer id (leave empty to skip):");
        input = promptFor(in, "Id");
        Long buyerId = null;
        Customer buyer = null;
        if (!input.isEmpty()) {
            buyerId = Long.parseLong(input);
            buyer = customerRepository.getById(buyerId);
            if(buyer == null)
                throw new IllegalArgumentException("Buyer not found");
        }

        System.out.println("🦲 Enter article data:");
        var article = new Article(
                null,
                LocalDateTime.parse(promptFor(in, "Auction start date (e.g. 2007-12-03T10:15:30)")),
                LocalDateTime.parse(promptFor(in, "Auction end date (e.g. 2007-12-03T10:15:30")),
                promptFor(in, "Description"),
                Float.parseFloat(promptFor(in, "Reserve price (e.g. 42.0)")),
                Float.parseFloat(promptFor(in, "Hammer price (e.g. 42.0)")),
                seller,
                buyer,
                ArticleStatus.valueOf(promptFor(in, "Status ( LISTED | AUCTION_RUNNING | SOLD | NOT_SOLD )"))
        );

        System.out.println("🦲 Enter article categories (leave empty to skip):");
        CategoryRepository categoryRepository = new CategoryRepository(entityManager);
        var categories = categoryRepository.getAll();
        for (Category category : categories) {
            System.out.println("\t" + category);
        }
        input = promptFor(in, "Categories (e.g. 1,2,3)");
        if (!input.isEmpty()) {

            for (String categoryId : input.split(",")) {
                var category = categoryRepository.getById(Long.parseLong(categoryId));
                if (category == null)
                    throw new IllegalArgumentException("Category not found");
                article.addCategory(category);
            }
        }

        return article;
    }
}
