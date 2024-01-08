import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewsApi extends JFrame {

    private JTextArea resultTextArea;
    private JTextField keywordTextField;

    public NewsApiSearchPanel() {
        setTitle("News Search Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 키워드 입력 필드
        keywordTextField = new JTextField();
        add(keywordTextField, BorderLayout.NORTH);

        // 결과를 표시할 텍스트 영역
        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);

        // 스크롤이 가능한 패널에 텍스트 영역 추가
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        add(scrollPane, BorderLayout.CENTER);

        // 검색 버튼
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchNews();
            }
        });
        add(searchButton, BorderLayout.SOUTH);

        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void searchNews() {
        String apiKey = "1277dcdf93f8462a96f2efd5778607ae";
        String keyword = keywordTextField.getText().trim();

        if (!keyword.isEmpty()) {
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

                // 결과를 텍스트 영역에 표시
                resultTextArea.setText(response.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new NewsApiSearchPanel();
            }
        });
    }
}
