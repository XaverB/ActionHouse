package actionhouse.app.bl;

import actionhouse.backend.orm.domain.Article;
import actionhouse.backend.orm.domain.Bid;
import actionhouse.backend.orm.domain.Customer;
import actionhouse.backend.orm.repository.ArticleRepository;
import actionhouse.backend.orm.repository.BidRepository;
import actionhouse.backend.orm.repository.CustomerRepository;
import actionhouse.backend.util.JpaUtil;
import jakarta.persistence.EntityManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

import static actionhouse.app.util.MenuHelper.promptFor;

public class BidBl {

    /**
     * Reads a bid from the stdin, creates a new bid and saves it to the database.
     */
    public void createBid() {
        Bid bid = null;
        try (EntityManager em = JpaUtil.getTransactionalEntityManager()) {
            var tx = em.getTransaction();
            try {
                bid = readBidFromStdin(em);

                BidRepository bidRepository = new BidRepository(em);
                bid = bidRepository.save(bid);
                tx.commit();
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
            System.out.println("🆕 Created bid: " + bid.getId());
        } catch (Exception ex) {
            System.out.println("Something bad happened 🥲: " + ex.getMessage());
        }
    }

    /**
     * Reads a bid from the stdin, updates an existing bid and saves it to the database.
     */
    public void deleteBid() {
        var bidId = readBidIdFromStdin();

        try (EntityManager em = JpaUtil.getTransactionalEntityManager()) {
            var tx = em.getTransaction();
            try {
                BidRepository bidRepository = new BidRepository(em);
                Bid bid = bidRepository.getById(bidId);
                if (bid == null) {
                    System.out.println("Bid not found");
                    return;
                }

                bidRepository.delete(bid);
                tx.commit();
            } finally {
                if (tx.isActive()) {
                    tx.rollback();
                }
            }
            System.out.println("🗑️ Deleted bid: " + bidId);
        } catch (Exception ex) {
            System.out.println("Something bad happened 🥲: " + ex.getMessage());
        }
    }

    private Long readBidIdFromStdin() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("🦲 Enter bid id:");
        // read customer from stdin
        return Long.parseLong(promptFor(in, "Id"));
    }

    private Bid readBidFromStdin(EntityManager entityManager) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        CustomerRepository customerRepository = new CustomerRepository(entityManager);
        ArticleRepository articleRepository = new ArticleRepository(entityManager);

        System.out.println("🦲 Enter bidder id:");
        String input = promptFor(in, "Id");
        Long bidderId = Long.parseLong(input);
        Customer bidder = customerRepository.getById(bidderId);
        if(bidder == null)
            throw new IllegalArgumentException("Bidder not found");


        System.out.println("🦲 Enter article id:");
        input = promptFor(in, "Id");
        Long articleId = Long.parseLong(input);
        Article article = articleRepository.getById(articleId);
        if(article == null)
            throw new IllegalArgumentException("Article not found");

        return new Bid(null,
                Double.parseDouble(promptFor(in, "Amount")),
                LocalDateTime.now(),
                bidder,
                article
                );
    }
}
