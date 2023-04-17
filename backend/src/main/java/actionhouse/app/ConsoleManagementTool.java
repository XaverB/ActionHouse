package actionhouse.app;

import actionhouse.app.bl.CustomerBl;
import actionhouse.app.enums.CustomerMenu;
import actionhouse.app.enums.Menu;
import actionhouse.backend.util.JpaUtil;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

import java.io.File;
import java.io.FileInputStream;

import static actionhouse.app.util.MenuHelper.readCustomerMenu;
import static actionhouse.app.util.MenuHelper.readMenu;

public class ConsoleManagementTool {

    public static void main(String[] args) {
        ConsoleManagementTool consoleManagementTool = new ConsoleManagementTool();
        consoleManagementTool.run();
    }

    public void run() {
        System.out.println("--- ActionHouse Console Management Tool ---");

        try {
            // trigger hibernate DB creation first
            JpaUtil.getEntityManagerFactory();
            insertTestData();
        } catch (Exception e) {
            System.err.println("Error inserting test data: " + e.getMessage());
        }

        Menu menu;
        do {
            menu = readMenu();
            switch (menu) {
                case CUSTOMER:
                    CustomerMenu customerMenu;
                    do {
                        customerMenu = readCustomerMenu();
                        customerAction(customerMenu);
                    } while (customerMenu != CustomerMenu.EXIT);
                    break;
                case ARTICLE:
                    break;
                case BIDS:
                    break;
                case EXIT:
                    break;
            }
        } while (menu != Menu.EXIT);
    }

    private void insertTestData() throws Exception {
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, "org.apache.derby.jdbc.ClientDriver");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, "jdbc:derby://localhost/AuctionHouseDb;currentSchema=APP");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "user");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "test");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA, "APP");

        IDatabaseTester databaseTester = new JdbcDatabaseTester(
                "org.apache.derby.jdbc.ClientDriver",
                "jdbc:derby://localhost/AuctionHouseDb;currentSchema=APP",
                "user",
                "test");

        IDatabaseConnection dbUnitConn = new DatabaseConnection(databaseTester.getConnection().getConnection(), "APP");
        dbUnitConn.getConfig().setFeature(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
        DatabaseConfig dbCfg = dbUnitConn.getConfig();


        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);

        var dataSetPath = getPathToTestResources("full.xml");
        var dataSet = new FlatXmlDataSetBuilder().build(new FileInputStream(
            dataSetPath
        ));
        databaseTester.setDataSet(dataSet);
        DatabaseOperation.CLEAN_INSERT.execute(dbUnitConn, dataSet);
    }

    protected static String getPathToTestResources(String fileName) {
        return ClassLoader.getSystemClassLoader().getResource(fileName).getPath();
    }

    private void customerAction(CustomerMenu customerMenu) {
        CustomerBl customerBl = new CustomerBl();

        switch (customerMenu) {
            case CREATE:
                customerBl.createCustomer();
                break;
            case UPDATE:
                customerBl.updateCustomer();
                break;
            case DELETE:
                customerBl.deleteCustomer();
                break;
            case SHOW:
                customerBl.showCustomer();
                break;
            case SHOW_ALL:
                customerBl.showCustomers();
                break;
            case EXIT:
                System.out.println("Exit Customer");
                break;
        }
    }
}
