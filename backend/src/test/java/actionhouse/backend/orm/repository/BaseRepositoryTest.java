package actionhouse.backend.orm.repository;

import actionhouse.backend.orm.domain.*;
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
import org.dbunit.operation.DatabaseOperation;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.time.LocalDate;

public abstract class BaseRepositoryTest {

    protected IDatabaseTester databaseTester;
    protected EntityManager entityManager;

    public BaseRepositoryTest() {
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "org.apache.derby.jdbc.ClientDriver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:derby://localhost/AuctionHouseDb;currentSchema=APP");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "user");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "test");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA, "APP");
    }

    protected void onSetUp() throws Exception {
        databaseTester = new JdbcDatabaseTester(
                "org.apache.derby.jdbc.ClientDriver",
                "jdbc:derby://localhost/AuctionHouseDb;currentSchema=APP",
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
        DatabaseOperation.CLEAN_INSERT.execute(dbUnitConn, getDataSet());
    }

    protected void onTearDown() throws Exception {
        databaseTester.onTearDown();
    }

    protected void commit() {
        entityManager.getTransaction().commit();
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
        // db ids begin with 1, but fucking dbunit rows begin with 0
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

            if (paymentTable.getValue(i, "DTYPE").equals("BankPaymentOption"))
                if (Long.valueOf((String) paymentTable.getValue(i, "CUSTOMER_ID")).equals(c.getId())) {
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
