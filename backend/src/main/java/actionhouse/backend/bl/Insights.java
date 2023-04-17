package actionhouse.backend.bl;

import actionhouse.backend.orm.domain.Article;
import actionhouse.backend.orm.domain.ArticleStatus;
import actionhouse.backend.orm.domain.Customer;
import actionhouse.backend.orm.repository.ArticleRepository;
import actionhouse.backend.orm.repository.CustomerRepository;
import actionhouse.backend.orm.repository.IArticleRepository;
import actionhouse.backend.orm.repository.ICustomerRepository;
import actionhouse.backend.util.JpaUtil;
import jakarta.persistence.EntityManager;
import org.hibernate.cfg.NotYetImplementedException;

import java.time.LocalDateTime;
import java.util.List;

public class Insights {

    private final EntityManager entityManager;
    private final IArticleRepository articleRepository;
    private final ICustomerRepository customerRepository;

    public Insights(EntityManager entityManager, IArticleRepository articleRepository, ICustomerRepository customerRepository) {
        this.entityManager = entityManager;
        this.articleRepository = articleRepository;
        this.customerRepository = customerRepository;
    }

    public Insights(EntityManager entityManager) {
        this(entityManager, new ArticleRepository(entityManager), new CustomerRepository(entityManager));
    }


    /**
     * Liefert alle Artikel bei denen die shortDescription auf die searchPhrase passt.
     * Wird wird ein maximaler Ausrufungspreis angegeben (maxReservePrice != null &&
     * maxReservePrice > 0) so werden NUR Artikel zurückgegeben deren reservePrice <=
     * maxReservePrice ist.
     * Wird eine Reihung angegeben (order != null) werden die Ergebnisse nach dem
     * entsprechenden Attribut sortiert. ArticleOrder ist ein Aufzählungsdatentyp (enum) mit
     * folgenden Werten: NAME, RESERVE_PRICE, HAMMER_PRICE, AUCTION_START_DATE.
     */
    public List<Article> findArticlesByDescription(String searchPhrase, Double
            maxReservePrice, ArticleOrder order) {
        var articles = articleRepository.findArticlesByDescription(searchPhrase, maxReservePrice);

        return order == null
                ? articles
                : articles.stream()
                .sorted((a1, a2) -> {
                    switch (order) {
                        case HAMMER_PRICE:
                            return Double.compare(a1.getHammerPrice(), a2.getHammerPrice());
                        case RESERVE_PRICE:
                            return Double.compare(a1.getReservePrice(), a2.getReservePrice());
                        case NAME:
                            return a1.getDescription().compareTo(a2.getDescription());
                        case AUCTION_START_DATE:
                            return a1.getAuctionStartDate().compareTo(a2.getAuctionStartDate());
                        default:
                            throw new IllegalArgumentException("Unknown order: " + order);
                    }
                })
                .toList();
    }

    /**
     * wirft eine ArticleNotFoundException falls der Artikel nicht existiert.
     * liefert null, sollte die Auktion noch nicht gestartet werden.
     * liefert den aktuell höchsten Gebotspreis, wenn die Auktion noch läuft.
     * liefert den HammerPrice, sollte die Auktion beendet sein und der Artikel erfolgreich versteigert
     * worden sein.
     * liefert null, sollte die Auktion beendet sein und der Artikel NICHT versteigert worden sein.
     *
     * @throws ArticleNotFoundException
     */
    public Double getArticlePrice(Long id) throws ArticleNotFoundException {
        Article article = articleRepository.getById(id);

        if (article == null) {
            throw new ArticleNotFoundException(id);
        }

        if (article.getAuctionStartDate() == null) {
            return null;
        }

        boolean isAuctionStillRunning = article.getAuctionEndDate() == null
                || article.getAuctionEndDate().isAfter(LocalDateTime.now());
        if (isAuctionStillRunning) {
            return article
                    .getBids()
                    .stream()
                    .mapToDouble(b -> b.getBid())
                    .max()
                    .orElse(0);
        }

        return article.getStatus() == ArticleStatus.SOLD
                // this cast is okay, because we are fine with a loss of precision
                ? (double) article.getHammerPrice()
                : null;
    }

    /**
     * Liefert die top count Verkäufer basierend auf Ihrem Umsatz (Summe HammerPrice aller
     * verkauften Artikel).
     */
    public List<Customer> getTopSellers(int count) {
        var topSellers = customerRepository.getTopSellers(count);
        return topSellers;
    }

    /**
     * Liefert die top count versteigerten Artikel basierend auf der Preisdifferenz zwischen HammerPrice
     * und ReservePrice.
     *
     * @param count
     * @return
     */
    public List<Article> getTopArticles(int count) {
        var articles = articleRepository.getTopArticles(count);
        return articles;
    }

}
