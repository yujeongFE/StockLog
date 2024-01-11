/*
package stocklogmanipulation;

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

public class H2_PanelAction2_1 extends JPanel {

    public H2_PanelAction2_1(String stockName, DefaultCategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createLineChart(
                "일봉차트 - " + stockName, // 차트 제목
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
            H2_PanelAction2_1 chartApp = new H2_PanelAction2_1(stockName, dataset);
            JFrame frame = new JFrame("Daily Chart Example");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(chartApp);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static void addFunctionality(JPanel panel, String stockName) {
        // 패널에 차트를 추가하는 기능
        DefaultCategoryDataset dataset = fetchData(stockName);
        createChart(stockName, dataset);
        panel.add(new H2_PanelAction2_1(stockName, dataset));
    }

    private static DefaultCategoryDataset fetchData(String stockName) {
        String apiKey = "ZM0OCCQ902KM00LJ";
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        try {
            // API 엔드포인트 및 요청 URL 생성
            String apiUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + stockName + "&apikey=" + apiKey;
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
            JsonObject timeSeriesData = jsonResponse.getAsJsonObject("Time Series (Daily)");

            // 가져온 데이터를 데이터셋에 추가
            for (String date : timeSeriesData.keySet()) {
                String closingPrice = timeSeriesData.getAsJsonObject(date).get("4. close").getAsString();
                dataset.addValue(Double.parseDouble(closingPrice), "Closing Price", date);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataset;
    }
}
*/
