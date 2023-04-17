package actionhouse.app.util;

import actionhouse.backend.util.JpaUtil;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

import java.io.FileInputStream;

public class Seeder {
    public static void setupDatabase() {
        try {
            // trigger hibernate DB creation first
            JpaUtil.getEntityManagerFactory();
            Seeder.insertTestData();
        } catch (Exception e) {
            System.err.println("Error inserting test data: " + e.getMessage());
        }
    }

    private static void insertTestData() throws Exception {
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

    private static String getPathToTestResources(String fileName) {
        return ClassLoader.getSystemClassLoader().getResource(fileName).getPath();
    }
}
