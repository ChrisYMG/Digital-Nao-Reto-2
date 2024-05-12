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

// Modelo
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

// Vista
class ConsoleView {
    public void display(String text) {
        System.out.println(text);
    }
}

// Controlador
public class main {
    public static void main(String[] args) throws Exception {
        Properties prop = new Properties();
        InputStream input = new FileInputStream("config.properties");
        prop.load(input);

        String api_key = prop.getProperty("API_KEY");

        String apiKey = api_key;
        ScholarSearch model = new ScholarSearch(apiKey);
        ConsoleView view = new ConsoleView();

        String result = model.search("unam", 10);
        view.display(result);
    }
}