package actionhouse.backend.bl;

import actionhouse.backend.orm.domain.Article;
import actionhouse.backend.orm.domain.Customer;
import actionhouse.backend.orm.repository.ArticleRepository;
import actionhouse.backend.orm.repository.CustomerRepository;
import actionhouse.backend.util.JpaUtil;
import jakarta.persistence.EntityManager;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.List;

public class Insights {

    private final EntityManager entityManager;
    private final ArticleRepository articleRepository;
    private final CustomerRepository customerRepository;

    public Insights() {
        entityManager = JpaUtil.getTransactionalEntityManager();
        articleRepository = new ArticleRepository(entityManager);
        customerRepository = new CustomerRepository(entityManager);
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
        throw new NotYetImplementedException();
    }

    /**
     * wirft eine ArticleNotFoundException falls der Artikel nicht existiert.
     * liefert null, sollte die Auktion noch nicht gestartet werden.
     * liefert den aktuell höchsten Gebotspreis, wenn die Auktion noch läuft.
     * liefert den HammerPrice, sollte die Auktion beendet sein und der Artikel erfolgreich versteigert
     * worden sein.
     * liefert null, sollte die Auktion beendet sein und der Artikel NICHT versteigert worden sein.
     * @throws ArticleNotFoundException
     */
    public double getArticlePrice(Article Id) throws ArticleNotFoundException {
        throw new NotYetImplementedException();
    }

    /**
     * Liefert die top count Verkäufer basierend auf Ihrem Umsatz (Summe HammerPrice aller
     * verkauften Artikel).
     */
    public List<Customer> getTopSellers(int count) {
        var topSellers =  customerRepository.getTopSellers(count);
        entityManager.getTransaction().commit();
        return topSellers;
    }

    /**
     * Liefert die top count versteigerten Artikel basierend auf der Preisdifferenz zwischen HammerPrice
     * und ReservePrice.
     * @param count
     * @return
     */
    public List<Article> getTopArticles(int count) {
        var articles = articleRepository.getTopArticles(count);
        entityManager.getTransaction().commit();
        return articles;
    }

}
