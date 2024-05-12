import java.sql.Connection;
import java.sql.DriverManager;
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
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            System.out.println("Conexión a la base de datos exitosa!");

            // Crea la tabla.
            String sql = "CREATE TABLE IF NOT EXISTS Authors (" +
                         "name VARCHAR(50), " +
                         "author_id VARCHAR(15), " +
                         "email VARCHAR(50), " +
                         "articles VARCHAR(100))";

            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("Tabla creada exitosamente!");

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