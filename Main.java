import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import org.json.JSONObject;
import org.json.JSONArray;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws Exception {
        Properties prop = new Properties();
        InputStream input = new FileInputStream("config.properties");
        prop.load(input);

        String api_key = prop.getProperty("API_KEY");

        String apiKey = api_key;
        ScholarSearch model = new ScholarSearch(apiKey);
        ConsoleView view = new ConsoleView();

        // Database connection
        Connection conn = dbconnection.connect();

        // Check if there are records in the Authors table
        String checkSql = "SELECT COUNT(*) AS count FROM Authors";
        Statement checkStmt = conn.createStatement();
        ResultSet rs = checkStmt.executeQuery(checkSql);
        rs.next();
        int count = rs.getInt("count");

        // If there are records, delete them
        if (count > 0) {
            String deleteSql = "DELETE FROM Authors";
            Statement deleteStmt = conn.createStatement();
            deleteStmt.executeUpdate(deleteSql);
            view.display("Los datos se han borrado exitosamente en la tabla Authors.");
        }

        String result = model.search("unam", 10);
        
        // Parse the JSON response
        JSONObject json = new JSONObject(result);
        JSONArray profiles = json.getJSONArray("profiles");

        for (int i = 0; i < profiles.length(); i++) {
            JSONObject profile = profiles.getJSONObject(i);
            String name = profile.getString("name");
            String author_id = profile.getString("author_id");
            String email = profile.getString("email");

            String[] titles = new String[3];
            try {
                JSONArray interests = profile.getJSONArray("interests");
                for (int j = 0; j < 3 && j < interests.length(); j++) {
                    titles[j] = interests.getJSONObject(j).getString("title");
                }
            } catch (org.json.JSONException e) {
                // Interests not found, leave the titles array as it is
            }

            // Insert the extracted information into the database
            String sql = "INSERT INTO Authors (name, author_id, email, articles) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, author_id);
            pstmt.setString(3, email);
            pstmt.setString(4, String.join(", ", titles));
            pstmt.executeUpdate();

            // Display a success message
            view.display("Los datos se han registrado exitosamente en la tabla Authors.");

            // Display the extracted information
            view.display("Name: " + name);
            view.display("Author_ID: " + author_id);
            view.display("Email: " + email);
            view.display("Articulos: " + String.join(", ", titles));
            view.display("\n");
        }
    }
}