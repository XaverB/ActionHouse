package actionhouse.api;

import actionhouse.api.dao.ArticleRepository;
import actionhouse.api.dao.CustomerRepository;
import actionhouse.api.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@Slf4j
public class DataBaseInitializer implements CommandLineRunner {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public DataBaseInitializer() {
        log.info("DataBaseInitializer created");
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        var customer1 = new Customer(null, "John", "Doe", "John.Doe@spring.org",
                // String street, String city, String state, String zipCode, String country
                new Address("Street 4", "New York", "NY", "12345", "USA"),
                new Address("Street 4", "New York", "NY", "12345", "USA"));
        customerRepository.save(customer1);

        var customer2 = new Customer(null, "Jane", "Doe", "Jane.Doe@spring.org",
                new Address("Manhattenstreet 5", "New York", "NY", "12345", "USA"),
                new Address("Merelestreet 5", "New York", "NY", "12345", "USA"));
        customerRepository.save(customer2);

        log.info("Number of customers: {}", customerRepository.count());

        Article article1 = new Article(null, LocalDateTime.now(), LocalDateTime.now().plusDays(7), "Article 1 description", 100.0f, null, customer1, null, ArticleStatus.AUCTION_RUNNING);
        Article article2 = new Article(null, LocalDateTime.now(), LocalDateTime.now().plusDays(14), "Article 2 description", 200.0f, null, customer1, null, ArticleStatus.AUCTION_RUNNING);
        Article article3 = new Article(null, LocalDateTime.now(), LocalDateTime.now().plusDays(21), "Article 3 description", 300.0f, null, customer1, null, ArticleStatus.AUCTION_RUNNING);
        Article article4 = new Article(null, LocalDateTime.now(), LocalDateTime.now().plusDays(25), "Article 4 description", 500.0f, null, customer1, null, ArticleStatus.AUCTION_RUNNING);
        Article article5 = new Article(null, LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(2), "Article 5 description", 100.0f, 100.0, customer1, customer2, ArticleStatus.SOLD);
        Article article6 = new Article(null, LocalDateTime.now(), LocalDateTime.now().plusDays(25), "Article 6 description", 500.0f, null, customer1, null, ArticleStatus.LISTED);
        Article article7 = new Article(null, LocalDateTime.now(), LocalDateTime.now().plusDays(25), "Article 7 description", 500.0f, null, customer1, null, ArticleStatus.LISTED);
        Article article8 = new Article(null, LocalDateTime.now(), LocalDateTime.now().plusDays(25), "Article 8 description", 500.0f, null, customer1, null, ArticleStatus.AUCTION_RUNNING);
        Article article9 = new Article(null, LocalDateTime.now(), LocalDateTime.now().plusDays(25), "Article 9 description", 500.0f, null, customer1, null, ArticleStatus.AUCTION_RUNNING);

        article1.addBid(new Bid(null, 150.0f, java.time.LocalDateTime.now(), customer2, article1));
        article2.addBid(new Bid(null, 500.0f, java.time.LocalDateTime.now(), customer2, article1));
        article9.addBid(new Bid(null, 500.0f, java.time.LocalDateTime.now(), customer2, article9));

        articleRepository.save(article1);
        articleRepository.save(article2);
        articleRepository.save(article3);
        articleRepository.save(article4);
        articleRepository.save(article5);
        articleRepository.save(article6);
        articleRepository.save(article7);
        articleRepository.save(article8);
        articleRepository.save(article9);

        log.info("Number of articles: {}", articleRepository.count());

        log.info("Use the following credentials to login as seller:" +
                "\n\tusername: " + customer1.getEmail());

        log.info("Use the following credentials to login as buyer:" +
                "\n\tusername: " + customer2.getEmail());
    }
}
