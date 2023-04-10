package actionhouse.backend.orm.repository;

import actionhouse.backend.orm.domain.Customer;
import jakarta.persistence.EntityManager;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.List;

public class CustomerRepository extends BaseRepository<Customer> {
    public CustomerRepository(EntityManager entityManager) {
        super(Customer.class, entityManager);
    }

    /**
     * Liefert die top count Verkäufer basierend auf Ihrem Umsatz (Summe HammerPrice aller
     * verkauften Artikel).
     */
    public List<Customer> getTopSellers(int count) {
        return entityManager.createQuery(
                        "SELECT c FROM Customer c " +
                                "JOIN c.soldArticles sa " +
                                "GROUP BY c " +
                                "ORDER BY SUM(sa.hammerPrice) DESC", Customer.class)
                .setMaxResults(count)
                .getResultList();
    }
}
