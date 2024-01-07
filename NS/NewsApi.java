import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class NewsApiSearch {
    public static void main(String[] args) {
  
        String apiKey = "1277dcdf93f8462a96f2efd5778607ae";

        Scanner scanner = new Scanner(System.in);
        System.out.print("뉴스를 검색할 키워드를 입력하세요: ");
        String keyword = scanner.nextLine();

        String apiUrl = "https://newsapi.org/v2/everything?q=" + keyword + "&apiKey=" + apiKey;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            System.out.println(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close(); 
        }
    }
}

