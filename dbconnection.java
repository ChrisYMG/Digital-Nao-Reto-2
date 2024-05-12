// @author: Christian Yael Mejía Galindo
// @description: Conecta a la base de datos
// @language: Java
// @return: Conecta a la base de datos
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class dbconnection {
    public static Connection connect() {
        Connection conn = null;
        try {
            // Load properties from the config.properties file.
            Properties prop = new Properties();
            InputStream input = new FileInputStream("config.properties");
            prop.load(input);

            // Register the JDBC driver.
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Get the properties.
            String dbUrl = prop.getProperty("DB_URL");
            String dbUser = prop.getProperty("DB_USER");
            String dbPass = prop.getProperty("DB_PASS");
            String dbName = prop.getProperty("DB_NAME");
            String dbTableName = prop.getProperty("DB_TABLE_NAME");

            // Establish the connection.
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            System.out.println("Database connection successful!");

            // Check if the table already exists.
            String checkSql = "SELECT count(*) FROM information_schema.TABLES WHERE (TABLE_SCHEMA = '" + dbName + "') AND (TABLE_NAME = '" + dbTableName + "')";
            Statement checkStmt = conn.createStatement();
            ResultSet rs = checkStmt.executeQuery(checkSql);
            rs.next();
            int count = rs.getInt(1);

            // If the table does not exist, create it.
            if (count == 0) {
                String sql = "CREATE TABLE " + dbTableName + " (" +
                    "name VARCHAR(50), " +
                    "author_id VARCHAR(15), " +
                    "email VARCHAR(50), " +
                    "articles VARCHAR(100))";

                Statement stmt = conn.createStatement();
                stmt.executeUpdate(sql);
                System.out.println("Table created successfully!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }
}