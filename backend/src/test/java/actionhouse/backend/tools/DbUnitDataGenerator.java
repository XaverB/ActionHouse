package actionhouse.backend.tools;

import org.apache.derby.iapi.jdbc.AutoloadedDriver;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbUnitDataGenerator {

    /**
     * Full export of the database
     * See https://www.dbunit.org/faq.html#extract
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException, DatabaseUnitException, IOException {

        Class driverClass = Class.forName("org.apache.derby.jdbc.ClientDriver");
        Connection jdbcConnection = DriverManager.getConnection(
                "jdbc:derby://localhost/AuctionHouseDb;currentSchema=APP","user","test");
        IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

        // https://dbunit.sourceforge.net/dbunit/integrationtests.html
        // https://stackoverflow.com/a/34165407
        Properties props = new Properties();
        props.put(DatabaseConfig.FEATURE_QUALIFIED_TABLE_NAMES, "true");
        connection.getConfig().setPropertiesByString(props);

        IDataSet fullDataSet = connection.createDataSet();
        FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));
    }
}
