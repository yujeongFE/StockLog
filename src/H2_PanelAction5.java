import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javax.swing.SwingUtilities;

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

                    // JTextArea에 뉴스 정보 설정
                    newsTextArea.setText(response.toString());

                    // 검색 창의 텍스트를 자동으로 설정
                    // 만약 검색 창을 JTextField로 사용했다면 setText를 사용
                    // 여기에서는 JTextArea를 사용하므로 해당하는 메서드 사용
                    newsTextArea.append("\n\nSearch Term: " + userInterestStock);

                    // 패널을 다시 그리도록 갱신
                    panel.revalidate();
                    panel.repaint();
                }   catch (IOException ex) { // IOException 추가
                        ex.printStackTrace();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
