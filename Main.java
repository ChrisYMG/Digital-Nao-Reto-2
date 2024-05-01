import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// Modelo
class ScholarSearch {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String apiKey;

    public ScholarSearch(String apiKey) {
        this.apiKey = apiKey;
    }

    public String search(String query, Integer num) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://serpapi.com/search.json?engine=google_scholar&q=" + query + "&num=" + num + "&key=" + apiKey))
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
public class Main {
    public static void main(String[] args) throws Exception {
        String apiKey = "ea4c06631dea41c9c6b1558264ec199448bcc278cd2ea8f206fdf67bde5c7164";
        ScholarSearch model = new ScholarSearch(apiKey);
        ConsoleView view = new ConsoleView();

        String result = model.search("biology", 5);
        view.display(result);
    }
}