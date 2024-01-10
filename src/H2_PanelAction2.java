import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class H2_PanelAction2 extends JPanel {

    public H2_PanelAction2(String stockName, DefaultCategoryDataset dataset) {

        String chartTitle = "Time Series (1Min)";
        JFreeChart chart = ChartFactory.createLineChart(
                chartTitle, // 차트 제목
                "Time", // x축 레이블
                "Closing Price", // y축 레이블
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        add(chartPanel);
    }

    public static void createChart(String stockName, DefaultCategoryDataset dataset) {
        // 차트 생성 및 표시
        SwingUtilities.invokeLater(() -> {
            H2_PanelAction2 chartApp = new H2_PanelAction2(stockName, dataset);
            JFrame frame = new JFrame("Intraday chart"); // 프레임을 새로 생성
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(chartApp);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static void addFunctionality(JPanel panel, String stockName) {
        // 패널에 차트를 추가하는 기능
        String interval = "1min";
        DefaultCategoryDataset dataset = fetchData(stockName, interval);
        createChart(stockName, dataset);
        panel.add(new H2_PanelAction2(stockName, dataset));
    }

    private static DefaultCategoryDataset fetchData(String stockName, String interval) {
        // stockName이 비어있으면 기본값 "AAPL" 설정
        if (stockName == null || stockName.isEmpty()) {
            stockName = "AAPL";
        }

        String apiKey = "H82GV03TBEHBQRNB";
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            // API 엔드포인트 및 요청 URL 생성
            String apiUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + stockName + "&interval=" + interval + "&apikey=" + apiKey;
            URL url = new URL(apiUrl);

            // HTTP 요청 보내기
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // 응답 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            // 응답을 JSON으로 파싱
            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();

            // 시계열 데이터에 해당하는 부분 가져오기
            //JsonObject timeSeriesData = jsonResponse.getAsJsonObject("Time Series (1Min)");

            JsonObject timeSeriesData = jsonResponse.getAsJsonObject("Time Series (Daily)");


            // 에러 처리: Time Series (1Min)이 없으면 종료
if (timeSeriesData == null) {
                System.out.println("Error: 'Time Series (1Min)' not found in API response.");
                return dataset;
            }


            // 가져온 데이터를 데이터셋에 추가
            for (String time : timeSeriesData.keySet()) {
                String closingPrice = timeSeriesData.getAsJsonObject(time).get("4. close").getAsString();
                dataset.addValue(Double.parseDouble(closingPrice), "Closing Price", time);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataset;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test");
            JPanel panel = new JPanel();
            H2_PanelAction2_1.addFunctionality(panel, null); // stockName이 없는 경우
            frame.add(panel);
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
