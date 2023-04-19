package actionhouse.backend.orm.repository;

import actionhouse.backend.orm.domain.*;
import actionhouse.backend.util.JpaUtil;
import org.dbunit.dataset.DataSetException;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ArticleRepositoryTest extends BaseRepositoryTest {
    private ArticleRepository articleRepository;

    public ArticleRepositoryTest() throws Exception {
        onSetUp();
    }

    @BeforeAll
    public static void init() {
        BaseRepositoryTest.init();
    }

    @AfterAll
    public static void cleanup() {
        JpaUtil.closeEntityManagerFactory();
    }

    @BeforeEach
    public void setUp() throws Exception {
        entityManager = JpaUtil.getTransactionalEntityManager();
        articleRepository = new ArticleRepository(entityManager);
        refreshDatabase();
    }

    @AfterEach
    public void tearDown() throws Exception {
        if(entityManager.getTransaction().isActive())
            entityManager.getTransaction().rollback();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7})
    public void getByIdWithValidIdReturnsCategory(int articleId) throws MalformedURLException, DataSetException {
        var expected = getDataSetArticle(articleId, "full.xml");

        var actual = articleRepository.getById(articleId);
        commit();

        assertArticle(expected, actual);
    }

    @Test
    public void saveArticleWithValidArticleSavesArticle() throws MalformedURLException, DataSetException {
        var expected = getDataSetArticle(8, "new_article.xml");
        expected.setId(null);
        expected.setSeller(null);
        expected.setBuyer(null);
        expected.getBids().clear();
        expected.getCategories().clear();
        expected.setDescription("asdasd");


        var actual = articleRepository.save(expected);
        entityManager.getTransaction().commit();

        assertArticle(expected, actual);
    }

    @Test
    public void deleteWithValidARticleDeletesArticle() {
        Article a = articleRepository.getById(1L);
        Assert.assertNotNull(a);

        articleRepository.delete(a);
        commit();

        entityManager = new JpaUtil().getTransactionalEntityManager();
        articleRepository = new ArticleRepository(entityManager);

        Article actual = articleRepository.getById(1L);

        Assert.assertNull(actual);
    }

    @Test
    public void updateWithValidArticleUpdatesArticle() throws MalformedURLException, DataSetException {
        // Arrange
        var expected = getDataSetArticle(1, "updated_article.xml");
        Article c = articleRepository.getById(1L);
        c.setDescription("Updated Description");
        c.setStatus(ArticleStatus.NOT_SOLD);
        c.getCategories().removeAll(c.getCategories());
        c.getBids().removeAll(c.getBids());
        Customer seller = c.getBuyer();
        c.setBuyer(c.getSeller());
        c.setSeller(seller);

        // Act
        articleRepository.update(c);
        commit();

        // Assert
        entityManager = JpaUtil.getTransactionalEntityManager();
        articleRepository = new ArticleRepository(entityManager);
        var actual = articleRepository.getById(1L);

        assertArticle(expected, actual);
    }

    @Test
    public void updateArticleWithAddedBidAddsBid() throws MalformedURLException, DataSetException {
        Customer bidder = getDataSetCustomer(2, "full.xml");

        Article article = articleRepository.getById(1L);
        Assert.assertEquals(4, article.getBids().size());

        article.removeBuyer();
        article.setStatus(ArticleStatus.NOT_SOLD);

        var bid = new Bid(null, 1000.0, LocalDateTime.of(2025, 1, 1, 1, 1),
                null,
                article);
        bidder.addBid(bid);
        article.addBid(bid);

        articleRepository.update(article);
        commit();

        entityManager = JpaUtil.getTransactionalEntityManager();
        articleRepository = new ArticleRepository(entityManager);
        var actual = articleRepository.getById(1L);

        var newBid = actual.getBids()
                .stream()
                .filter(b -> b.getBid() == 1000.0)
                .findFirst()
                .orElse(null);

        Assert.assertEquals(5, actual.getBids().size());
        Assert.assertNotNull(newBid);
        Assert.assertTrue(newBid.getId() != null);
    }

    @Test
    public void getTopArticlesWithThreeReturnsTopThreeArticles() {
        var articles = articleRepository.getTopArticles(3);
        commit();
        // <ARTICLE ID="5" AUCTIONENDDATE="2023-04-27 11:00:00.0" AUCTIONSTARTDATE="2023-04-20 11:00:00.0" DESCRIPTION="Apple iPhone 13 Pro Max" HAMMERPRICE="1500.0" RESERVEPRICE="1200.0" STATUS="1" BUYER_ID="2" SELLER_ID="1" />
        // <ARTICLE ID="1" AUCTIONENDDATE="2023-04-01 12:00:00.0" AUCTIONSTARTDATE="2023-03-27 12:00:00.0" DESCRIPTION="MacBook Pro" HAMMERPRICE="2000.0" RESERVEPRICE="1800.0" STATUS="1" SELLER_ID="1" BUYER_ID="2"/>
        // <ARTICLE ID="7" AUCTIONENDDATE="2023-04-28 11:00:00.0" AUCTIONSTARTDATE="2023-04-21 11:00:00.0" DESCRIPTION="Lego Star Wars Ultimate Millennium Falcon" HAMMERPRICE="800.0" RESERVEPRICE="600.0" STATUS="1" SELLER_ID="3" BUYER_ID="1"/>
        Assert.assertEquals(3, articles.size());
        Assert.assertNotNull(articles.stream().filter(a -> a.getId() == 5L).findFirst());
        Assert.assertNotNull(articles.stream().filter(a -> a.getId() == 1L).findFirst());
        Assert.assertNotNull(articles.stream().filter(a -> a.getId() == 7L).findFirst());
    }

    @Test
    public void findArticleByDescriptionWithValidDescriptionAndNullReservePriceReturnsArticle() throws MalformedURLException, DataSetException {
        var expected = getDataSetArticle(1, "full.xml");

        var actual = articleRepository.findArticlesByDescription("MacBook Pro", null);
        commit();

        Assert.assertEquals(1, actual.size());
        assertArticle(expected, actual.get(0));
    }

    @Test
    public void findArticleByDescriptionWithValidDescriptionAndZeroReservePriceReturnsArticle() throws MalformedURLException, DataSetException {
        var expected = getDataSetArticle(1, "full.xml");

        var actual = articleRepository.findArticlesByDescription("MacBook Pro", 0d);
        commit();

        Assert.assertEquals(1, actual.size());
        assertArticle(expected, actual.get(0));
    }

    @Test
    public void findArticleByDescriptionWithValidDescriptionAndZeroReservePriceReturnsArticles() throws MalformedURLException, DataSetException {

        ArrayList<Article> expected = new ArrayList<>();
        expected.add(getDataSetArticle(1, "full.xml"));
        expected.add(getDataSetArticle(2, "full.xml"));
        expected.add(getDataSetArticle(4, "full.xml"));
        expected.add(getDataSetArticle(5, "full.xml"));
        expected.add(getDataSetArticle(6, "full.xml"));
        expected.add(getDataSetArticle(7, "full.xml"));

        var actual = articleRepository.findArticlesByDescription("a", 0d);
        commit();

        Assert.assertEquals(expected.size(), actual.size());
        for(int i = 0; i < expected.size(); i++) {
            assertArticle(expected.get(i), actual.get(i));
        }
    }

    @Test
    public void findArticleByDescriptionWithValidDescriptionAndReservePriceReturnsArticles() throws MalformedURLException, DataSetException {
        ArrayList<Article> expected = new ArrayList<>();
        expected.add(getDataSetArticle(2, "full.xml"));
        expected.add(getDataSetArticle(4, "full.xml"));
        expected.add(getDataSetArticle(6, "full.xml"));

        var actual = articleRepository.findArticlesByDescription("a", 500d);
        commit();

        Assert.assertEquals(expected.size(), actual.size());
        assertArrayContainsInAnyOrder(expected, actual);
    }

    private void assertArticle(Article expected, Article actual) {
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getDescription(), actual.getDescription());
        assertArrayContainsInAnyOrder(expected.getBids(), actual.getBids());
        assertArrayContainsInAnyOrder(expected.getCategories(), actual.getCategories());
    }
}