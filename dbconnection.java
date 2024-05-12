import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class dbconnection {
    public static void main(String[] args) {
        try {
            // Carga las propiedades desde el archivo config.properties.
            Properties prop = new Properties();
            InputStream input = new FileInputStream("config.properties");
            prop.load(input);

            // Registra el controlador JDBC.
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Obtiene las propiedades.
            String dbUrl = prop.getProperty("DB_URL");
            String dbUser = prop.getProperty("DB_USER");
            String dbPass = prop.getProperty("DB_PASS");

            // Establece la conexión.
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);

            System.out.println("Conexión exitosa!");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}