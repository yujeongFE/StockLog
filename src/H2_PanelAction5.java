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

class H2_PanelAction5 {
    private static JButton searchButton;
    private static JTextArea newsTextArea;

    public static void addFunctionality(JPanel panel) {
        // 패널 5에 추가할 기능 구현
        searchButton = new JButton("검색");

        // 검색 버튼의 ActionListener에서 사용자로부터 주식 기호를 입력 받지 않고,
        // 바로 패널에 있는 검색 창의 텍스트를 사용하도록 수정
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 사용자로부터 주식 기호를 입력 받지 않고,
                // 바로 패널에 있는 검색 창의 텍스트를 사용
                String userInterestStock = JOptionPane.showInputDialog("Enter the stock you are interested in:");
                if (userInterestStock != null && !userInterestStock.isEmpty()) {
                    // 검색 창의 텍스트를 사용하여 뉴스 검색 수행
                    performNewsSearch(userInterestStock, panel);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a valid stock symbol.");
                }
            }
        });

        newsTextArea = new JTextArea();
        newsTextArea.setEditable(false);  // 편집 불가능하도록 설정

        // 패널에 레이아웃을 BorderLayout으로 설정
        panel.setLayout(new BorderLayout());

        // 패널에 검색 버튼 추가
        panel.add(searchButton, BorderLayout.NORTH);

        // JScrollPane을 생성하고 JTextArea를 넣어줌
        JScrollPane scrollPane = new JScrollPane(newsTextArea);

        // 패널의 가운데 영역에 JScrollPane 추가
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    private static void performNewsSearch(String userInterestStock, JPanel panel) {
        String apiKey = "1277dcdf93f8462a96f2efd5778607ae";
        String apiUrl = "https://newsapi.org/v2/everything?q=" + userInterestStock + "&apiKey=" + apiKey;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
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
                    connection.disconnect();

                    // JSON 파싱을 통해 필요한 정보 추출
                    String newsTable = parseJson(response.toString());

                    // JTextArea에 뉴스 정보 설정
                    newsTextArea.setText(newsTable);

                    // 검색 창의 텍스트를 자동으로 설정
                    newsTextArea.append("\n\nSearch Term: " + userInterestStock);

                    // 패널을 다시 그리도록 갱신
                    panel.revalidate();
                    panel.repaint();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // 사용자에게 알림
                    JOptionPane.showMessageDialog(null, "Error: Unable to fetch news data. Please check your internet connection.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    // 사용자에게 알림
                    JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please try again later.");
                }
            }
        });
    }

    private static String parseJson(String jsonString) {
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

        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> descriptions = new ArrayList<>();
        ArrayList<String> authors = new ArrayList<>();

        while (titleMatcher.find()) {
            titles.add(titleMatcher.group(1));
        }

        while (descriptionMatcher.find()) {
            descriptions.add(descriptionMatcher.group(1));
        }

        while (authorMatcher.find()) {
            authors.add(authorMatcher.group(1));
        }

        // 표 형식으로 데이터 정리
        StringBuilder newsTable = new StringBuilder();
        newsTable.append(String.format("%-50s%-100s%-30s%n", "Title", "Description", "Author"));

        int maxSize = Math.max(Math.max(titles.size(), descriptions.size()), authors.size());
        for (int i = 0; i < maxSize; i++) {
            String title = i < titles.size() ? titles.get(i) : "";
            String description = i < descriptions.size() ? descriptions.get(i) : "";
            String author = i < authors.size() ? authors.get(i) : "";

            newsTable.append(String.format("%-50s%-100s%-30s%n", title, description, author));
        }

        return newsTable.toString();
    }
}
