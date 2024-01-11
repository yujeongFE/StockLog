package stocklogmanipulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

class H2_PanelAction5_1 {
    private static JTextArea responseTextArea;

    public static void addFunctionality(JPanel panel) {
        // 패널 5에 추가할 기능 구현

        // input 버튼 설정
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JTextField categoryTextField = new JTextField(20);
        JButton fetchButton = new JButton("Fetch News");

        // 첫 번째 레이블
        JLabel firstLabel = new JLabel("Enter the category :");
        inputPanel.add(firstLabel);

        // 두 번째 레이블
        JLabel secondLabel = new JLabel("(입력 예시 : category = general)");
        inputPanel.add(secondLabel);

        // 세 번째 레이블
        JLabel thirdLabel = new JLabel("e.g.,business, general, sports, technology, sources, etc.,):");
        inputPanel.add(thirdLabel);

        inputPanel.add(categoryTextField);
        inputPanel.add(fetchButton);

        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String apiKey = "1277dcdf93f8462a96f2efd5778607ae";
                String category = categoryTextField.getText();

                // 특정 카테고리의 인기 헤드라인 출력
                getTopHeadlines(apiKey, category);
            }
        });

        responseTextArea = new JTextArea();
        responseTextArea.setEditable(false);

        // 패널에 레이아웃을 BorderLayout으로 설정 //
        panel.setLayout(new BorderLayout());

        // JScrollPane을 생성하고 JTextArea를 넣어줌
        JScrollPane scrollPane = new JScrollPane(responseTextArea);

        panel.add(inputPanel, BorderLayout.NORTH);
        // 패널의 가운데 영역에 JScrollPane 추가
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    // 뉴스 API로부터 헤드라인을 가져오는 메서드
    private static void getTopHeadlines(String apiKey, String parameter) {
        try {
            String apiUrl = "https://newsapi.org/v2/top-headlines";
            String urlParameters = "apiKey=" + apiKey + "&" + parameter;
            URL url = new URL(apiUrl + "?" + urlParameters);

            System.out.println("Request URL: " + url); // 로깅 추가

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            // JSON 파싱 후 표시
            showNews(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
            // 사용자에게 알림
            JOptionPane.showMessageDialog(null, "Error: Unable to fetch news data. Please check your internet connection.");
        } catch (Exception e) {
            e.printStackTrace();
            // 사용자에게 알림
            JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please try again later.");
        }
    }

    // JSON 데이터를 파싱하여 JTextArea에 표시하는 메서드
    private static void showNews(String jsonString) {
        try {
            // JSON 객체 생성
            JSONObject json = new JSONObject(jsonString);

            // "articles" 배열에서 뉴스 정보 추출
            JSONArray articles = json.getJSONArray("articles");

            java.util.List<String> titles = new ArrayList<>();
            java.util.List<String> descriptions = new ArrayList<>();
            java.util.List<String> authors = new ArrayList<>();

            // 정규식을 사용하여 JSON 데이터 파싱
            String titlePattern = "\"title\":\"([^\"]*)\"";
            String descriptionPattern = "\"description\":\"([^\"]*)\"";
            String authorPattern = "\"author\":\"([^\"]*)\"";

            Pattern titleRegex = Pattern.compile(titlePattern);
            Pattern descriptionRegex = Pattern.compile(descriptionPattern);
            Pattern authorRegex = Pattern.compile(authorPattern);

            Matcher titleMatcher = titleRegex.matcher(jsonString);
            Matcher descriptionMatcher = descriptionRegex.matcher(jsonString);
            Matcher authorMatcher = authorRegex.matcher(jsonString);

            // 뉴스 정보 추출
            while (titleMatcher.find()) {
                titles.add(titleMatcher.group(1));
            }

            while (descriptionMatcher.find()) {
                descriptions.add(descriptionMatcher.group(1));
            }

            while (authorMatcher.find()) {
                authors.add(authorMatcher.group(1));
            }

            // JTextArea에 표시
            StringBuilder displayText = new StringBuilder();
            displayText.append(String.format("%-70s%-120s%-45s%n", "기사 제목", "기사 내용", "기자 이름"));

            int maxSize = Math.max(Math.max(titles.size(), descriptions.size()), authors.size());
            for (int i = 0; i < maxSize; i++) {
                String title = i < titles.size() ? titles.get(i) : "";
                String description = i < descriptions.size() ? descriptions.get(i) : "";
                String author = i < authors.size() ? authors.get(i) : "";

                displayText.append(String.format("%-70s%-120s%-45s%n", title, description, author));
            }

            responseTextArea.setText(displayText.toString());
        } catch (Exception e) {
            e.printStackTrace();
            // 사용자에게 알림
            JOptionPane.showMessageDialog(null, "Error: Unable to parse news data.");
        }
    }

}
