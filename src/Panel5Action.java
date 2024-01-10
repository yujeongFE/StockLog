package src;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class Panel5Action { // 주식 매매 기록
    static Object[] row = new Object[8];
    static DefaultTableModel tableModel = new DefaultTableModel();

    public static void addFunctionality(JPanel panel, String userId) {
        DBconnection dbConnector = new DBconnection();
        Connection connection = dbConnector.getConnection();

        String id = userId;
        String query = "SELECT s.NAME, l.COMPANY, l.BUYORSELL, l.DATE, l.PRICE, l.QTY, l.RRATIO, l.MEMO FROM stock s, log l WHERE s.CODE = l.CODE AND U_ID = '" + id + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            tableModel.addColumn("종목명");
            tableModel.addColumn("증권사");
            tableModel.addColumn("매도/매수");
            tableModel.addColumn("날짜");
            tableModel.addColumn("주식 단가");
            tableModel.addColumn("수량");
            tableModel.addColumn("매매비용(세금, 수수료)");
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


            JPanel searchResultsPanel = new JPanel(new GridLayout(itemList.getLength(), 1));

            for (int i = 0; i < itemList.getLength(); i++) {
                Node itemNode = itemList.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element itemElement = (Element) itemNode;
                    String itemName = itemElement.getElementsByTagName("itmsNm").item(0).getTextContent();


                    JLabel resultLabel = new JLabel(itemName);


                    resultLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {

                            handleResultLabelClick(itemElement, resultLabel);
                        }
                    });

                    searchResultsPanel.add(resultLabel);
                }
            }


            JFrame resultsFrame = new JFrame("검색 결과");
            resultsFrame.setLayout(new BorderLayout());
            resultsFrame.setSize(200, 200);

            resultsFrame.add(searchResultsPanel, BorderLayout.SOUTH);


            resultsFrame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "오류가 발생했습니다. 다시 시도해주세요.");
        }
    }

    private static void handleResultLabelClick(Element itemElement, JLabel resultLabel) {
        resultLabel.setForeground(Color.BLUE);
        SellBuy sb = new SellBuy();
        sb.setFrame(sb);
        String selectedStockName = itemElement.getElementsByTagName("itmsNm").item(0).getTextContent();
        sb.setSelectedStockName(selectedStockName);
        sb.openFrame(selectedStockName);
    }}
