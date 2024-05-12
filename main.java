// @author: Christian Yael Mejía Galindo
// @description: Busca autores en Google Scholar
// @param: api_key
// @param: mauthor
// @param: num
// @return: json
// @see: https://serpapi.com/google-scholar-profiles-api
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import org.json.JSONObject;
import org.json.JSONArray;
import java.sql.Connection;
import java.sql.PreparedStatement;

// Model
class ScholarSearch {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String apiKey;

    public ScholarSearch(String apiKey) {
        this.apiKey = apiKey;
    }

    public String search(String mauthor, Integer num) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://serpapi.com/search.json?engine=google_scholar_profiles&mauthors=" + mauthor + "&num=" + num + "&key=" + apiKey))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}

// View
class ConsoleView {
    public void display(String text) {
        System.out.println(text);
    }
}

// Controler
public class main {
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