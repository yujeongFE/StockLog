import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewsApiExampleGUI {

    private static JTextArea responseTextArea;

    public static void main(String[] args) {
        // GUI를 생성하는 메서드를 호출
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        // JFrame 생성 및 기본 설정
        JFrame frame = new JFrame("뉴스 API 예제");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // 응답을 표시할 JTextArea 생성 및 설정
        responseTextArea = new JTextArea();
        responseTextArea.setEditable(false);

        // JScrollPane을 사용하여 JTextArea를 스크롤 가능하도록 설정
        JScrollPane scrollPane = new JScrollPane(responseTextArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // 입력을 받을 JPanel 생성 및 설정
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        // API 키, 국가 코드, 카테고리를 입력받을 JTextField 생성
        JTextField apiKeyField = new JTextField(20);
        JTextField countryField = new JTextField(5);
        JTextField categoryField = new JTextField(10);

        // API 호출 버튼 생성
        JButton submitButton = new JButton("제출");

        // 버튼에 대한 ActionListener 설정
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 사용자가 입력한 값을 가져와서 API 호출 메서드 호출
                String apiKey = apiKeyField.getText();
                String country = countryField.getText();
                String category = categoryField.getText();

                if (!apiKey.isEmpty() && !country.isEmpty()) {
                    getTopHeadlines(apiKey, country);
                } else if (!apiKey.isEmpty() && !category.isEmpty()) {
                    getTopHeadlines(apiKey, category);
                }
            }
        });

        // 입력 관련 컴포넌트를 패널에 추가
        inputPanel.add(new JLabel("API 키:"));
        inputPanel.add(apiKeyField);
        inputPanel.add(new JLabel("국가 코드:"));
        inputPanel.add(countryField);
        inputPanel.add(new JLabel("카테고리:"));
        inputPanel.add(categoryField);
        inputPanel.add(submitButton);

        // 입력 패널을 프레임의 상단에 추가
        frame.add(inputPanel, BorderLayout.NORTH);

        // 프레임 크기 및 위치 설정
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        // 프레임을 표시
        frame.setVisible(true);
    }

    private static void getTopHeadlines(String apiKey, String parameter) {
        try {
            // News API의 엔드포인트 및 파라미터 설정
            String apiUrl = "https://newsapi.org/v2/top-headlines";
            String urlParameters = "apiKey=" + apiKey + "&" + parameter;
            URL url = new URL(apiUrl + "?" + urlParameters);

            // HTTP 연결 설정 및 GET 요청 전송
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // 응답을 읽기 위한 BufferedReader 생성
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            // 응답을 읽어와서 StringBuilder에 추가
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // 리소스 정리
            reader.close();
            connection.disconnect();

            // GUI 패널에 응답을 표시
            responseTextArea.setText("응답: " + response.toString());
        } catch (IOException e) {
            // 예외 발생 시 에러를 콘솔에 출력
            e.printStackTrace();
        }
    }
}
