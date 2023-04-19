package actionhouse.backend.orm.repository;

import actionhouse.backend.orm.domain.*;
import actionhouse.backend.util.JpaUtil;
import jakarta.persistence.EntityManager;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mssql.InsertIdentityOperation;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseRepositoryTest {

    protected IDatabaseTester databaseTester;
    protected EntityManager entityManager;

    protected static void init() {
        // hard coded string should be in config file
        JpaUtil.PU_Name = "AuctionHousePUInMemory";
        JpaUtil.getEntityManagerFactory();
    }

    public BaseRepositoryTest() {
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "org.apache.derby.jdbc.EmbeddedDriver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:derby:memory:AuctionHouseDb;create=true;currentSchema=APP");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "user");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "test");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA, "APP");
    }

    protected void onSetUp() throws Exception {
        databaseTester = new JdbcDatabaseTester(
                "org.apache.derby.jdbc.EmbeddedDriver",
                "jdbc:derby:memory:AuctionHouseDb;create=true;currentSchema=APP",
                "user",
                "test");

        IDatabaseConnection dbUnitConn = new DatabaseConnection(databaseTester.getConnection().getConnection(), "APP");
        dbUnitConn.getConfig().setFeature(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
        DatabaseConfig dbCfg = dbUnitConn.getConfig();


        databaseTester.setSetUpOperation(getSetUpOperation());
        databaseTester.setTearDownOperation(getTearDownOperation());

        databaseTester.setDataSet(getDataSet());
        DatabaseOperation.CLEAN_INSERT.execute(dbUnitConn, getDataSet());
    }

    protected void refreshDatabase() throws Exception {
        IDatabaseConnection dbUnitConn = new DatabaseConnection(databaseTester.getConnection().getConnection(), "APP");
        dbUnitConn.getConfig().setFeature(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
//        DatabaseOperation.CLEAN_INSERT.execute(dbUnitConn, getDataSet());
        DatabaseOperation.DELETE_ALL.execute(dbUnitConn, getDataSet());
        DatabaseOperation.CLEAN_INSERT.execute(dbUnitConn, getDataSet());
//        new InsertIdentityOperation(DatabaseOperation.CLEAN_INSERT).execute(dbUnitConn, getDataSet());
    }

    protected void onTearDown() throws Exception {
        databaseTester.onTearDown();
    }

    protected void commit() {
        entityManager.getTransaction().commit();
    }

    protected static <T> void assertArrayContainsInAnyOrder(Collection<T> expected, Collection<T> actual) {
        Assert.assertEquals(expected.size(), actual.size());
        Assert.assertTrue(expected.containsAll(actual));
        Assert.assertTrue(actual.containsAll(expected));
    }

    protected static String getPathToTestResources(String fileName) {
        String path = "src/test/resources/" + fileName;
        File file = new File(path);
        return file.getAbsolutePath();
    }

    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(new FileInputStream(
                getPathToTestResources("full.xml")
        ));
    }

    protected DatabaseOperation getSetUpOperation() {
        return DatabaseOperation.CLEAN_INSERT;
    }

    protected DatabaseOperation getTearDownOperation() {
        return DatabaseOperation.DELETE_ALL;
    }

    protected static Set<Bid> getDataSetBidsForArticle(int articleId, String dataSetName) throws MalformedURLException, DataSetException {
        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File(getPathToTestResources(dataSetName)));
        var bidTable = expectedDataSet.getTable("BID");

        Set<Bid> bids = new HashSet<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

        for (int i = 0; i < bidTable.getRowCount(); i++) {
            if (Integer.valueOf((String) bidTable.getValue(i, "ARTICLE_ID")) == articleId) {
                bids.add(
                        new Bid(
                                Long.valueOf((String) bidTable.getValue(i, "ID")),
                                Float.valueOf((String) bidTable.getValue(i, "BID")),
                                LocalDateTime.parse((String) bidTable.getValue(i, "DATE"), formatter),
                                getDataSetCustomer(Integer.valueOf((String) bidTable.getValue(i, "BIDDER_ID")), dataSetName),
                                // do not include bids or we will end up in endless recursion
                                getDataSetArticleWithoutBids(articleId, dataSetName)
                        )
                );
            }
        }
        return bids;
    }

    protected static Article getDataSetArticle(int index, String dataSetName) throws MalformedURLException, DataSetException {
        // db ids begin with 1, but fucking dbunit rows begin with 0
        index = index - 1;

        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File(getPathToTestResources(dataSetName)));
        var articleTable = expectedDataSet.getTable("ARTICLE");

        var sellerId = Integer.valueOf((String) articleTable.getValue(index, "SELLER_ID"));
        var buyerId = Integer.valueOf((String) articleTable.getValue(index, "BUYER_ID"));

        Customer seller = getDataSetCustomer(sellerId, dataSetName);
        Customer buyer = getDataSetCustomer(buyerId, dataSetName);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");


        Set<Bid> bids = getDataSetBidsForArticle(
                Integer.valueOf((String) articleTable.getValue(index, "ID")),
                dataSetName);

        var article = new Article(
                Long.valueOf((String) articleTable.getValue(index, "ID")),
                LocalDateTime.parse((String) articleTable.getValue(index, "AUCTIONSTARTDATE"), formatter),
                LocalDateTime.parse((String) articleTable.getValue(index, "AUCTIONENDDATE"), formatter),
                (String) articleTable.getValue(index, "DESCRIPTION"),
                Float.valueOf((String) articleTable.getValue(index, "RESERVEPRICE")),
                Float.valueOf((String) articleTable.getValue(index, "HAMMERPRICE")),
                seller,
                buyer,
                ArticleStatus.values()[Integer.valueOf((String) articleTable.getValue(index, "STATUS"))]
        );
        article.setBids(bids);
        article.setCategories(getDataSetCategoriesForArticle(Math.toIntExact(article.getId()), dataSetName));

        return article;
    }

    protected static Article getDataSetArticleWithoutBids(int index, String dataSetName) throws MalformedURLException, DataSetException {
        // db ids begin with 1, but fucking dbunit rows begin with 0
        index = index - 1;

        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File(getPathToTestResources(dataSetName)));
        var articleTable = expectedDataSet.getTable("ARTICLE");

        var sellerId = Integer.valueOf((String) articleTable.getValue(index, "SELLER_ID"));
        var buyerId = Integer.valueOf((String) articleTable.getValue(index, "BUYER_ID"));

        Customer seller = getDataSetCustomer(sellerId, dataSetName);
        Customer buyer = getDataSetCustomer(buyerId, dataSetName);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

        var article = new Article(
                Long.valueOf((String) articleTable.getValue(index, "ID")),
                LocalDateTime.parse((String) articleTable.getValue(index, "AUCTIONSTARTDATE"), formatter),
                LocalDateTime.parse((String) articleTable.getValue(index, "AUCTIONENDDATE"), formatter),
                (String) articleTable.getValue(index, "DESCRIPTION"),
                Float.valueOf((String) articleTable.getValue(index, "RESERVEPRICE")),
                Float.valueOf((String) articleTable.getValue(index, "HAMMERPRICE")),
                seller,
                buyer,
                ArticleStatus.values()[Integer.valueOf((String) articleTable.getValue(index, "STATUS"))]
        );
        article.setBids(new HashSet<>());
        article.setCategories(getDataSetCategoriesForArticle(Math.toIntExact(article.getId()), dataSetName));

        return article;
    }

    protected static Set<Category> getDataSetCategoriesForArticle(int articleId, String dataSetName) throws MalformedURLException, DataSetException {
        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File(getPathToTestResources(dataSetName)));
        var articleCategoryTable = expectedDataSet.getTable("ARTICLE_CATEGORY");

        Set<Category> categories = new HashSet<>();

        for (int i = 0; i < articleCategoryTable.getRowCount(); i++) {
            if (Integer.valueOf((String) articleCategoryTable.getValue(i, "ARTICLE_ID")) == articleId) {
                categories.add(
                        getDataSetCategory(Integer.valueOf((String) articleCategoryTable.getValue(i, "CATEGORIES_ID")), dataSetName)
                );
            }
        }
        return categories;
    }

    protected static Category getDataSetCategory(int index, String dataSetName) throws MalformedURLException, DataSetException {
        // db ids begin with 1, but fucking dbunit rows begin with 0
        index = index - 1;

        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File(getPathToTestResources(dataSetName)));
        var categoryTable = expectedDataSet.getTable("CATEGORY");

        return new Category(
                Long.valueOf((String) categoryTable.getValue(index, "ID")),
                (String) categoryTable.getValue(index, "NAME")
        );

    }

    protected static Customer getDataSetCustomer(int index, String dataSetName) throws MalformedURLException, DataSetException {
        index = index - 1;

        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(new File(getPathToTestResources(dataSetName)));
        var customerTable = expectedDataSet.getTable("CUSTOMER");
        var paymentTable = expectedDataSet.getTable("PAYMENTOPTION");

        var billingAddress = new Address((String) customerTable.getValue(index, "PAY_STREET"),
                (String) customerTable.getValue(index, "PAY_CITY"),
                (String) customerTable.getValue(index, "PAY_STATE"),
                (String) customerTable.getValue(index, "PAY_ZIPCODE"),
                (String) customerTable.getValue(index, "PAY_COUNTRY")
        );

        var shippingAddress = new Address(
                (String) customerTable.getValue(index, "SHIP_STREET"),
                (String) customerTable.getValue(index, "SHIP_CITY"),
                (String) customerTable.getValue(index, "SHIP_STATE"),
                (String) customerTable.getValue(index, "SHIP_ZIPCODE"),
                (String) customerTable.getValue(index, "SHIP_COUNTRY")
        );

        var c = new Customer(
                Long.valueOf((String) customerTable.getValue(index, "ID")),
                (String) customerTable.getValue(index, "FIRSTNAME"),
                (String) customerTable.getValue(index, "LASTNAME"),
                (String) customerTable.getValue(index, "EMAIL")
                , shippingAddress,
                billingAddress);

        for (int i = 0; i < paymentTable.getRowCount(); i++) {

            if (Long.valueOf((String) paymentTable.getValue(i, "CUSTOMER_ID")).equals(c.getId()))
                if (paymentTable.getValue(i, "DTYPE").equals("BankPaymentOption")) {
                    {
                        BankPaymentOption bankPayment = new BankPaymentOption(
                                Long.valueOf((String) paymentTable.getValue(i, "ID")),
                                (String) paymentTable.getValue(i, "OWNER"),
                                c,
                                (String) paymentTable.getValue(i, "BANKACCOUNTNUMBER"),
                                (String) paymentTable.getValue(i, "BANKIDENTIFIER")
                        );
                        c.addPaymentOption(bankPayment);
                    }
                } else {
                    CreditcardPaymentOption creditCardPayment = new CreditcardPaymentOption(
                            Long.valueOf((String) paymentTable.getValue(i, "ID")),
                            (String) paymentTable.getValue(i, "OWNER"),
                            c,
                            (String) paymentTable.getValue(i, "CREDITCARDNUMBER"),
                            LocalDate.parse((String) paymentTable.getValue(i, "CREDITCARDVALIDTO")),
                            (String) paymentTable.getValue(i, "CARDVERIFICATIONVALUE")
                    );
                    c.addPaymentOption(creditCardPayment);
                }

        }

        return c;
    }
}
