package actionhouse.backend.bl;

import actionhouse.backend.orm.domain.Article;
import actionhouse.backend.orm.domain.ArticleStatus;
import actionhouse.backend.orm.domain.Bid;
import actionhouse.backend.orm.repository.CustomerRepository;
import actionhouse.backend.util.JpaUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InsightsTest {

    private EntityManager getMockEntityManager() {
        var mock = mock(EntityManager.class);
        when(mock.getTransaction()).thenReturn(mock(EntityTransaction.class));
        return mock;
    }

    @Test
    public void getArticlePriceWithNonExistentArticleIdThrowsArticleNotFoundException() {

        Insights insights = new Insights(
                getMockEntityManager(),
                new ArticleRepositoryStub(new ArrayList<>()),
                new CustomerRepositoryStub()
        );

        assertThrows(ArticleNotFoundException.class, () -> {
            insights.getArticlePrice(1l);
        });
    }

    @Test
    public void getArticlePriceWithExistentArticleWithoutAuctionStartDateReturnsNull() throws ArticleNotFoundException {

        ArrayList<Article> articles = new ArrayList<Article>();
        articles.add(new Article(1l,
                null,
                null,
                "Test",
                null,
                null,
                null,
                null,
                ArticleStatus.NOT_SOLD));

        Insights insights = new Insights(
                getMockEntityManager(),
                new ArticleRepositoryStub(articles),
                new CustomerRepositoryStub()
        );

        assertEquals(null, insights.getArticlePrice(1l));
    }

    @Test
    public void getArticlePriceWithArticleInRunningAuctionReturnsHighestBid() throws ArticleNotFoundException {

        Article article = new Article(1l,
                LocalDateTime.MIN,
                null,
                "Test",
                null,
                null,
                null,
                null,
                ArticleStatus.AUCTION_RUNNING);


        Set<Bid> bids = new HashSet<Bid>();
        bids.add(new Bid(1l, 10l,
                LocalDateTime.of(2021, 1, 1, 1, 0),
                null,
                article));
        bids.add(new Bid(2l, 20l,
                LocalDateTime.of(2021, 1, 1, 2, 0),
                null,
                article));
        // highest bid
        bids.add(new Bid(3l, 50l,
                LocalDateTime.of(2021, 1, 1, 0, 0),
                null,
                article));

        bids.forEach(bid -> article.getBids().add(bid));


        ArrayList<Article> articles = new ArrayList<Article>();
        articles.add(article);

        Insights insights = new Insights(
                getMockEntityManager(),
                new ArticleRepositoryStub(articles),
                new CustomerRepositoryStub()
        );

        assertEquals(50l, insights.getArticlePrice(1l));
    }

    @Test
    public void getArticlePriceWithSoldArticleReturnsHammerPrice() throws ArticleNotFoundException {

        Article article = new Article(1l,
                LocalDateTime.MIN,
                LocalDateTime.MIN,
                "Test",
                null,
                500f,
                null,
                null,
                ArticleStatus.SOLD);

        ArrayList<Article> articles = new ArrayList<Article>();
        articles.add(article);

        Insights insights = new Insights(
                getMockEntityManager(),
                new ArticleRepositoryStub(articles),
                new CustomerRepositoryStub()
        );

        assertEquals(500l, insights.getArticlePrice(1l));
    }

    @Test
    public void getArticlePriceWithUnsoldArticleReturnsNull() throws ArticleNotFoundException {

        Article article = new Article(1l,
                LocalDateTime.MIN,
                LocalDateTime.MIN,
                "Test",
                null,
                null,
                null,
                null,
                ArticleStatus.NOT_SOLD);

        ArrayList<Article> articles = new ArrayList<Article>();
        articles.add(article);

        Insights insights = new Insights(
                getMockEntityManager(),
                new ArticleRepositoryStub(articles),
                new CustomerRepositoryStub()
        );

        assertEquals(null, insights.getArticlePrice(1l));
    }

    // Repository already tested -> checking sorting
    @Test
    public void findArticlesByDescriptionWithHammerPriceOrderReturnsOrderedArticles() {

        ArrayList<Article> articles = new ArrayList<Article>();

        articles.add(new Article(1l,
                LocalDateTime.MIN,
                LocalDateTime.MIN,
                "Test",
                null,
                500f,
                null,
                null,
                ArticleStatus.NOT_SOLD));
        articles.add(new Article(2l,
                LocalDateTime.MIN,
                LocalDateTime.MIN,
                "Test",
                null,
                100f,
                null,
                null,
                ArticleStatus.NOT_SOLD));
        articles.add(new Article(3l,
                LocalDateTime.MIN,
                LocalDateTime.MIN,
                "Test",
                null,
                1000f,
                null,
                null,
                ArticleStatus.NOT_SOLD));

        Insights insights = new Insights(
                getMockEntityManager(),
                new ArticleRepositoryStub(articles),
                new CustomerRepositoryStub()
        );

        var actual = insights.findArticlesByDescription("Test", null, ArticleOrder.HAMMER_PRICE);

        assertEquals(3, actual.size());
        assertEquals(100f, actual.get(0).getHammerPrice());
        assertEquals(500f, actual.get(1).getHammerPrice());
        assertEquals(1000f, actual.get(2).getHammerPrice());
    }

    @Test
    public void findArticlesByDescriptionWithReservePriceOrderReturnsOrderedArticles() {

        ArrayList<Article> articles = new ArrayList<Article>();

        articles.add(new Article(1l,
                LocalDateTime.MIN,
                LocalDateTime.MIN,
                "Test",
                500f,
                null,
                null,
                null,
                ArticleStatus.NOT_SOLD));
        articles.add(new Article(2l,
                LocalDateTime.MIN,
                LocalDateTime.MIN,
                "Test",
                100f,
                null,
                null,
                null,
                ArticleStatus.NOT_SOLD));
        articles.add(new Article(3l,
                LocalDateTime.MIN,
                LocalDateTime.MIN,
                "Test",
                1000f,
                null,
                null,
                null,
                ArticleStatus.NOT_SOLD));

        Insights insights = new Insights(
                getMockEntityManager(),
                new ArticleRepositoryStub(articles),
                new CustomerRepositoryStub()
        );

        var actual = insights.findArticlesByDescription("Test", null, ArticleOrder.RESERVE_PRICE);

        assertEquals(3, actual.size());
        assertEquals(100f, actual.get(0).getReservePrice());
        assertEquals(500f, actual.get(1).getReservePrice());
        assertEquals(1000f, actual.get(2).getReservePrice());
    }

    @Test
    public void findArticlesByDescriptionWithNameOrderReturnsOrderedArticles() {

        ArrayList<Article> articles = new ArrayList<Article>();

        articles.add(new Article(1l,
                LocalDateTime.MIN,
                LocalDateTime.MIN,
                "b",
                500f,
                null,
                null,
                null,
                ArticleStatus.NOT_SOLD));
        articles.add(new Article(2l,
                LocalDateTime.MIN,
                LocalDateTime.MIN,
                "a",
                100f,
                null,
                null,
                null,
                ArticleStatus.NOT_SOLD));
        articles.add(new Article(3l,
                LocalDateTime.MIN,
                LocalDateTime.MIN,
                "c",
                1000f,
                null,
                null,
                null,
                ArticleStatus.NOT_SOLD));

        Insights insights = new Insights(
                getMockEntityManager(),
                new ArticleRepositoryStub(articles),
                new CustomerRepositoryStub()
        );

        var actual = insights.findArticlesByDescription("", null, ArticleOrder.NAME);

        assertEquals(3, actual.size());
        assertEquals("a", actual.get(0).getDescription());
        assertEquals("b", actual.get(1).getDescription());
        assertEquals("c", actual.get(2).getDescription());
    }

    @Test
    public void findArticlesByDescriptionWithAuctionStartDateOrderReturnsOrderedArticles() {

        ArrayList<Article> articles = new ArrayList<Article>();

        articles.add(new Article(1l,
                LocalDateTime.of(2020, 1, 1, 0, 0),
                LocalDateTime.MIN,
                "b",
                500f,
                null,
                null,
                null,
                ArticleStatus.NOT_SOLD));
        articles.add(new Article(2l,
                LocalDateTime.of(1990, 1, 1, 0, 0),
                LocalDateTime.MIN,
                "a",
                100f,
                null,
                null,
                null,
                ArticleStatus.NOT_SOLD));
        articles.add(new Article(3l,
                LocalDateTime.of(2025, 1, 1, 0, 0),
                LocalDateTime.MIN,
                "c",
                1000f,
                null,
                null,
                null,
                ArticleStatus.NOT_SOLD));

        Insights insights = new Insights(
                getMockEntityManager(),
                new ArticleRepositoryStub(articles),
                new CustomerRepositoryStub()
        );

        var actual = insights.findArticlesByDescription("", null, ArticleOrder.NAME);

        assertEquals(3, actual.size());
        assertEquals(LocalDateTime.of(1990, 1, 1, 0, 0), actual.get(0).getAuctionStartDate());
        assertEquals(LocalDateTime.of(2020, 1, 1, 0, 0), actual.get(1).getAuctionStartDate());
        assertEquals(LocalDateTime.of(2025, 1, 1, 0, 0), actual.get(2).getAuctionStartDate());
    }
}