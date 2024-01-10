import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;

// 패널 5
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;

// 패널 5-1
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

// 패널 1에 대한 동작을 처리하는 클래스
class PanelAction1 { // 종목 지수
    public static void addFunctionality(JPanel panel) {
        // 패널 1에 추가할 기능 구현
    }
}

// 패널 2에 대한 동작을 처리하는 클래스
/* class H2_PanelAction2 { // 매도주식
    public static void addFunctionality(JPanel panel) {
        // 패널 2에 추가할 기능 구현
    }
} */

class PanelAction2_1 { // 매도주식
    public static void addFunctionality(JPanel panel) {
        // 패널 2에 추가할 기능 구현
    }
}

// 패널 5에 대한 동작을 처리하는 클래스
// H2_PanelAction5.java 에 있습니다.



// 패널 5-1에 대한 동작을 처리하는 클래스
// H2_PanelAction5_1.java 에 있습니다.


// 패널 6에 대한 동작을 처리하는 클래스
class PanelAction6 { // 특정 주식에 대한 매도, 매수 리스트
    static Object[] row = new Object[8];
    static DefaultTableModel tableModel = new DefaultTableModel();

    public static void addFunctionality(JPanel panel, String userId, String stockName) {
        DBconnection dbConnector = new DBconnection();
        Connection connection = dbConnector.getConnection();

        String id = userId;
        String query = "SELECT s.NAME, l.COMPANY, l.BUYORSELL, l.DATE, l.PRICE, l.QTY, l.RRATIO, l.MEMO FROM stock s, log l WHERE s.CODE = l.CODE AND U_ID = '" + id + "' AND s.Name = '" + stockName + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            tableModel.addColumn("종목명");
            tableModel.addColumn("증권사");
            tableModel.addColumn("매도/매수");
            tableModel.addColumn("날짜");
            tableModel.addColumn("주식 단가");
            tableModel.addColumn("수량");
            tableModel.addColumn("수익률");
            tableModel.addColumn("메모");

            while (resultSet.next()) {
                row[0] = resultSet.getObject(1);
                row[1] = resultSet.getObject(2);
                row[2] = resultSet.getObject(3);
                row[3] = resultSet.getObject(4);
                row[4] = resultSet.getObject(5);
                row[5] = resultSet.getObject(6);
                row[6] = resultSet.getObject(7);
                row[7] = resultSet.getObject(8);
                tableModel.addRow(row);
            }

            JButton searchButton = new JButton("매도/매수 기록 추가");
            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SellBuyFrame();
                }
            });
            panel.add(searchButton, BorderLayout.SOUTH);

            JLabel label = new JLabel("주식 매매 일지", SwingConstants.CENTER);
            panel.add(label, BorderLayout.NORTH);

            JTable table = new JTable(tableModel);

            // 주식 클릭하면 Home2 화면으로 이동
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 1) { // 클릭 확인
                        JTable target = (JTable) e.getSource();
                        int row = target.getSelectedRow();

                        // 여기서 선택된 행의 데이터를 얻을 수 있어요.
                        String stockName = (String) tableModel.getValueAt(row, 0); // 종목명은 첫 번째 열(인덱스 0)
                        // System.out.println(stockName);
                        new Home2(userId, stockName); // 종목명을 이용해 페이지를 열거나 처리하는 함수 호출
                    }
                }
            });

            table.setPreferredScrollableViewportSize(table.getPreferredSize());
            JScrollPane scrollPane = new JScrollPane(table);

            Dimension panelSize = panel.getPreferredSize();
            int newScrollPaneHeight = (int) (panelSize.height * 0.66);
            scrollPane.setPreferredSize(new Dimension(0, newScrollPaneHeight));

            panel.add(scrollPane, BorderLayout.CENTER);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            dbConnector.closeConnection();
        }
    }

    private static void SellBuyFrame() {
        JFrame SellBuyFrame = new JFrame("매도/매수 기록 추가");
        SellBuyFrame.setLocation(800, 400);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        JLabel l1 = new JLabel();
        JTextField text = new JTextField(15);
        JButton searchButton = new JButton("검색");

        inputPanel.add(l1);
        inputPanel.add(text);
        inputPanel.add(searchButton);

        panel.add(BorderLayout.CENTER, inputPanel);
        panel.add(BorderLayout.SOUTH, new JLabel());

        SellBuyFrame.getContentPane().add(panel);
        SellBuyFrame.setSize(400, 400);
        SellBuyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        SellBuyFrame.setVisible(true);
        SellBuyFrame.setLayout(new BorderLayout());

        // Add ActionListener to the searchButton
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = text.getText();
                if (!searchTerm.isEmpty()) {
                    performSearch(searchTerm);
                } else {
                    JOptionPane.showMessageDialog(SellBuyFrame, "Please enter a search term.");
                }
            }
        });
    }

    private static String[] getLastBusinessDayRange() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        } else if (dayOfWeek == Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, -2);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String todt = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.MONTH, -1);
        String frdt = dateFormat.format(calendar.getTime());

        return new String[]{frdt, todt};
    }

    private static void performSearch(String searchTerm) {
        try {
            String[] dateRange = getLastBusinessDayRange();
            String frdt = dateRange[0];
            String todt = dateRange[1];

            String urlStr = "https://api.odcloud.kr/api/GetStockSecuritiesInfoService/v1/getStockPriceInfo?";
            urlStr += "serviceKey=" + "1%2FWP%2BVc3M5kGU2bikqOuBl9hAtMQ7OeqB24EL0llGF9zC75kdgM1jbsTy90LiI9hmDwU7jeFjW8P%2B1VPFtc%2BDg%3D%3D";
            urlStr += "&beginBasDt=" + frdt;
            urlStr += "&endBasDt=" + todt;
            urlStr += "&likeItmsNm=" + URLEncoder.encode(searchTerm, "UTF-8");
            System.out.println(urlStr);

            HttpURLConnection connection = (HttpURLConnection) new URL(urlStr).openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            System.out.println("결과값: " + response.toString());

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(response.toString().getBytes("UTF-8")));

            NodeList itemList = document.getElementsByTagName("item");

            // 검색결과가 0인 경우
            if (itemList.getLength() == 0) {
                JOptionPane.showMessageDialog(null, "검색 결과가 없습니다.");
                return;
            }

            // 패널에 결과값 추가
            JPanel searchResultsPanel = new JPanel(new GridLayout(itemList.getLength(), 1));

            for (int i = 0; i < itemList.getLength(); i++) {
                Node itemNode = itemList.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element itemElement = (Element) itemNode;
                    String itemName = itemElement.getElementsByTagName("itmsNm").item(0).getTextContent();

                    // 검색 결과 항목마다 라벨 생성
                    JLabel resultLabel = new JLabel(itemName);

                    // 라벨에 ActionListener 추가
                    resultLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            // 라벨을 클릭했을 때 수행할 메서드 호출
                            handleResultLabelClick(itemElement, resultLabel); // 라벨과 함께 전달
                        }
                    });

                    searchResultsPanel.add(resultLabel);
                }
            }

            // 검색 결과 보여주는 프레임 생성
            JFrame resultsFrame = new JFrame("검색 결과");
            resultsFrame.setLayout(new BorderLayout());
            resultsFrame.setSize(200, 200);

            resultsFrame.add(searchResultsPanel, BorderLayout.SOUTH);

            // 프레임을 보이게 한다.
            resultsFrame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "오류가 발생했습니다. 다시 시도해주세요.");
        }
    }

    private static void handleResultLabelClick(Element itemElement, JLabel resultLabel) {
        // 이 부분에서 검색 결과 항목을 클릭했을 때 수행할 동작을 구현해주세요.
        resultLabel.setForeground(Color.BLUE);
    }
}

// 하단 바에 대한 동작을 처리하는 클래스
class PanelAction7 { // 매도주식
    public static void addFunctionality(JPanel panel) {
        // 하단바에 추가할 기능 구현
    }
}

public class Home2 {
    static String userId; // 사용자 id 저장 변수 추가
    static String stockName; // 주식종목명 저장 변수 추가

    public Home2(String userId, String stockName) {
        this.userId = userId;
        this.stockName = stockName;

        JFrame frame = new JFrame("주식 매매 관리 시스템");

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel topLeftPanel = createPanelWithBorder("1"); // 종목지수
        JPanel topRightPanel1 = createPanelWithBorder("2"); // 종목 차트 (분봉)
        JPanel topRightPanel2 = createPanelWithBorder("2-1"); // 종목 차트 (일봉)
        JPanel bottomLeftPanel = createPanelWithBorder("3"); // 관심주식
        JPanel bottomRightPanel = createPanelWithBorder("4"); // 보유주
        JPanel rightPanel1 = createPanelWithBorder("5"); // 투자자별 (기관, 외국인..)'
        JPanel rightPanel2 = createPanelWithBorder("5-1"); // 뉴스
        JPanel rightBottomPanel = createPanelWithBorder("6"); // 매도, 매수일지 (종목 하나에 관한)

        JPanel panel22_1 = new JPanel();
        panel22_1.setLayout(new GridLayout(2, 1));
        panel22_1.add(topRightPanel1);
        panel22_1.add(topRightPanel2);

        JPanel panel55_1 = new JPanel();
        panel55_1.setLayout(new GridLayout(2, 1));
        panel55_1.add(rightPanel1);
        panel55_1.add(rightPanel2);

        JPanel bottomPanel = new JPanel(); // 하단바
        bottomPanel.setBackground(Color.GRAY); // 배경색 회색
        bottomPanel.setPreferredSize(new Dimension(frame.getWidth(), 50)); // 높이 50px

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // 패널에 기능 추가
        PanelAction1.addFunctionality(topLeftPanel); // 패널 1에 기능 추가
        //H2_PanelAction2.addFunctionality(topRightPanel1); // 패널 2에 기능 추가
        PanelAction2_1.addFunctionality(topRightPanel2); // 패널 2-1에 기능 추가
        PanelAction3.addFunctionality(bottomLeftPanel, userId); // 관심 주식 표시
        PanelAction4.addFunctionality(bottomRightPanel, userId); // 보유 주식 표시
        H2_PanelAction5.addFunctionality(rightPanel1); // 패널 5에 기능 추가
        // PanelAction5.addFunctionality(rightPanel1);
        PanelAction6.addFunctionality(rightBottomPanel, userId, stockName); // 패널 6에 기능 추가
        PanelAction7.addFunctionality(bottomPanel); // 하단 바에 기능 추가

        // 패널 5-1에 기능 추가 // 다시 활성화해야함
        // H2_PanelAction5_1.addFunctionality(rightPanel2, bottomLeftPanel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // gbc.weightx = 1.0;은 해당 컴포넌트가 그리드의 가로 방향으로 가능한 최대 공간을 차지
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH; // 가로 및 세로 방향으로 모두 공간을 채움 // fill 속성은 해당 컴포넌트가 할당받은 공간을 어떻게 채울지를 지정
        mainPanel.add(topLeftPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(panel22_1, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(bottomLeftPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(bottomRightPanel, gbc);

        // 패널 5와 패널 5-1 설정
        gbc.gridx = 2;
        gbc.gridy = 0;
        mainPanel.add(panel55_1, gbc);

        // 패널 6 설정
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridheight = 1; // 1 행만 차지
        gbc.weighty = 0.5; // 세로 방향으로 50% 차지
        mainPanel.add(rightBottomPanel, gbc);
        frame.add(mainPanel);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private JPanel createPanelWithBorder(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);

        // 테두리 스타일 지정
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        panel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Home2 home2 = new Home2(userId, stockName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
