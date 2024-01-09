import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KOSDAQ {
    public static void main(String[] args) {
        try {
            // 시작 및 종료 날짜를 Unix 타임스탬프로 변환
            long 시작날짜 = convertDateToTimestamp("20240101");
            long 종료날짜 = convertDateToTimestamp("20240107");

            // 업데이트된 날짜 범위로 Yahoo Finance API URL 작성 (코스닥 심볼: ^KQ11)
            String apiUrl = "https://query1.finance.yahoo.com/v8/finance/chart/^KQ11?period1=" + 시작날짜 +
                    "&period2=" + 종료날짜 + "&interval=1d";

            // URL 객체 생성
            URL url = new URL(apiUrl);

            // 연결 열기
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 요청 방법 설정
            connection.setRequestMethod("GET");

            // 응답 코드 가져오기
            int 응답코드 = connection.getResponseCode();

            if (응답코드 == HttpURLConnection.HTTP_OK) {
                // 응답 읽기
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                // 응답 처리 및 JFreeChart를 사용하여 타임스탬프 및 종가 값 플로팅
                plotStockChart(response.toString(), "KOSDAQ 차트");
            } else {
                System.out.println("HTTP request failed with response code: " + 응답코드);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void plotStockChart(String jsonResponse, String chartTitle) {
        // JSON 파싱 및 타임스탬프 및 종가 데이터 추출
        List<Long> 타임스탬프데이터 = new ArrayList<>();
        List<Double> 종가데이터 = new ArrayList<>();

        // simple.json 라이브러리를 사용하여 JSON 파싱
        JSONParser parser = new JSONParser();

        try {
            JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
            JSONObject chartObject = (JSONObject) jsonObject.get("chart");
            JSONArray resultArray = (JSONArray) chartObject.get("result");
            JSONObject resultObject = (JSONObject) resultArray.get(0);

            // get 메서드를 사용하여 중첩된 JSON 객체 추출
            JSONObject indicatorsObject = (JSONObject) resultObject.get("indicators");
            JSONArray quoteArray = (JSONArray) indicatorsObject.get("quote");
            JSONObject quoteObject = (JSONObject) quoteArray.get(0);

            JSONArray timestampArray = (JSONArray) resultObject.get("timestamp");
            JSONArray closeArray = (JSONArray) quoteObject.get("close");

            // 타임스탬프데이터 채우기
            for (Object timestamp : timestampArray) {
                타임스탬프데이터.add((Long) timestamp);
            }

            // 종가데이터 채우기
            for (Object close : closeArray) {
                종가데이터.add((Double) close);
            }

            // 데이터셋 생성
            XYSeries series = new XYSeries("종가");

            // 타임스탬프가 초 단위로 주어진 경우 여부 확인 (필요시 1000.0으로 나누지 않음)
            boolean 밀리초단위인지 = 타임스탬프데이터.get(0) > 1_000_000_000;
            double 시간단위 = 밀리초단위인지 ? 1000.0 : 1.0;

            // 타임스탬프를 double로 변환하여 x값으로 사용
            for (int i = 0; i < 타임스탬프데이터.size(); i++) {
                double x값 = 타임스탬프데이터.get(i) / 시간단위; // 초로 변환 필요시에만 나누기
                System.out.println("타임스탬프: " + 타임스탬프데이터.get(i) + ", 변환된 x값: " + x값);
                series.add(x값, 종가데이터.get(i));
            }

            XYSeriesCollection dataset = new XYSeriesCollection(series);

            // 차트 생성
            JFreeChart chart = ChartFactory.createTimeSeriesChart(
                    chartTitle, // 차트 제목
                    "날짜", // X축 레이블
                    "종가", // Y축 레이블
                    dataset, // 데이터셋
                    true, // 범례 표시
                    true, // 툴팁 사용
                    false // URL 생성 설정
            );

            // x축 날짜 표시 형식 지정
            DateAxis dateAxis = new DateAxis("날짜");
            dateAxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); // 날짜 및 시간 형식 설정
            chart.getXYPlot().setDomainAxis(dateAxis);

            // y축 범위 설정
            NumberAxis rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
            rangeAxis.setAutoRange(true); // y축 범위 자동 설정
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); // 정수 단위로 표시

            // 차트를 표시할 패널 생성
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(800, 600));

            // 차트를 표시할 JFrame 생성
            JFrame frame = new JFrame(chartTitle);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(chartPanel);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 날짜 문자열을 Unix 타임스탬프로 변환하는 메서드
    private static long convertDateToTimestamp(String dateStr) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = dateFormat.parse(dateStr);
        return date.getTime() / 1000; // 밀리초를 초로 변환
    }
}
