package actionhouse.app.bl;

import actionhouse.backend.bl.ArticleOrder;
import actionhouse.backend.bl.Insights;
import actionhouse.backend.orm.domain.Article;
import actionhouse.backend.orm.repository.ArticleRepository;
import actionhouse.backend.orm.repository.CustomerRepository;
import actionhouse.backend.util.JpaUtil;
import jakarta.persistence.EntityManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import static actionhouse.app.util.MenuHelper.promptFor;

public class AnalyticsBl {

    /**
     * Reads an article description from the stdin and prints all articles that match the description.
     * Further filters the articles by a max reserve price.
     * Further orders the articles by a given order.
     */
    public void findArticleByDescription() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        List<Article> articles;

        try (EntityManager entityManager = JpaUtil.getTransactionalEntityManager()) {
            Insights insights = new Insights(entityManager);

            var tx = entityManager.getTransaction();
            try {

                var description = promptFor(in, "Enter a description to search for: ");
                var maxReservePriceS = promptFor(in, "Enter a max reserve price (leave empty for no max price): ");
                Double maxReservePrice = maxReservePriceS.isEmpty() ? null : Double.parseDouble(maxReservePriceS);

                ArticleOrder order = ArticleOrder.NAME;
                var articleOrderS = promptFor(in, "Enter an order - NAME | RESERVE_PRICE | HAMMER_PRICE | AUCTION_START_DATE (leave empty for NAME order): ");
                if (!articleOrderS.isEmpty())
                    order = ArticleOrder.valueOf(articleOrderS);


                insights.findArticlesByDescription(
                                description,
                                maxReservePrice,
                                order)
                        .forEach(
                                article -> System.out.println(article)
                        );

                tx.commit();
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
     * Reads an article id from the stdin and prints the price of the article.
     */
    public void getArticlePrice() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        List<Article> articles;

        try (EntityManager entityManager = JpaUtil.getTransactionalEntityManager()) {
            Insights insights = new Insights(entityManager);

            var tx = entityManager.getTransaction();
            try {

                var articleId = Long.parseLong(promptFor(in, "Enter an article id: "));
                System.out.printf("Article (%d) price: %.2f 🪙\n", articleId, insights.getArticlePrice(articleId));
                tx.commit();
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
        } catch (Exception ex) {
            System.out.println("Something bad happened 🥲: " + ex.getMessage());
        }
    }

    public void getTopSellers() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try (EntityManager entityManager = JpaUtil.getTransactionalEntityManager()) {
            Insights insights = new Insights(entityManager);

            var tx = entityManager.getTransaction();
            try {

                var count = Integer.parseInt(promptFor(in, "Enter a count: "));

                insights.getTopSellers(count)
                        .forEach(
                                System.out::println
                        );
                tx.commit();
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
        } catch (Exception ex) {
            System.out.println("Something bad happened 🥲: " + ex.getMessage());
        }
    }

    public void getTopArticles() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try (EntityManager entityManager = JpaUtil.getTransactionalEntityManager()) {
            Insights insights = new Insights(entityManager);

            var tx = entityManager.getTransaction();
            try {

                var count = Integer.parseInt(promptFor(in, "Enter a count: "));

                var articles = insights.getTopArticles(count);

                articles.forEach(System.out::println);

                tx.commit();
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
        } catch (Exception ex) {
            System.out.println("Something bad happened 🥲: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
