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
import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class Panel3Action {
    static Object[] row = new Object[7];
    static DefaultTableModel tableModel = new DefaultTableModel();

    public static void addFunctionality(JPanel panel, String userId) {
        // 데이터베이스 연결
        DBconnection dbConnector = new DBconnection();
        Connection connection = dbConnector.getConnection();

        String id = userId;
        // SQL 쿼리 실행
        String query = "SELECT s.NAME, s.CODE, i.CATEGORY, i.MEMO FROM stock s, interest i WHERE s.CODE = i.CODE AND U_ID = '" + id + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // 원하는 컬럼 순서와 이름을 추가
            tableModel.addColumn("종목명");
            tableModel.addColumn("종목코드");
            tableModel.addColumn("현재주가");
            tableModel.addColumn("시장 구분");
            tableModel.addColumn("전일대비등락");
            tableModel.addColumn("전일대비등락비");
            tableModel.addColumn("메모");

            // 결과셋의 데이터를 테이블 모델에 추가
            String stockName = null;
            while (resultSet.next()) {
                row[0] = resultSet.getObject(1);
                stockName = resultSet.getObject(1).toString();
                row[1] = resultSet.getObject(2);
                row[3] = resultSet.getObject(3);
                row[6] = resultSet.getObject(4);
            }

            // 날짜 범위 설정
            String[] dateRange = getLastBusinessDayRange();
            String frdt = dateRange[0];
            String todt = dateRange[1];

            if (stockName != null) {
                // 종목명을 URL 인코딩하여 API 호출
                StringBuffer stockPriceData = getStockPrice(URLEncoder.encode(stockName, "UTF-8"), frdt, todt);

                if (stockPriceData.length() > 0) {
                    // 데이터 파싱 및 표로 정리하여 출력
                    printStockPriceTable(stockPriceData);
                } else {
                    System.out.println("No stock price data available for the specified parameters.");
                }
            } else {
            }

            // JButton 생성 및 패널에 추가
            JButton searchButton = new JButton("관심 주식 검색");
            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    InterestStockFrame();
                }
            });
            panel.add(searchButton, BorderLayout.SOUTH);  // Add the button to the SOUTH position of the panel

            // JLabel 생성 및 패널에 추가
            JLabel label = new JLabel("관심 주식", SwingConstants.CENTER);
            panel.add(label, BorderLayout.NORTH);

            // 테이블 생성 및 패널에 추가
            JTable table = new JTable(tableModel);
            table.setPreferredScrollableViewportSize(table.getPreferredSize());

            // JScrollPane으로 테이블을 감싸기
            JScrollPane scrollPane = new JScrollPane(table);

            // JScrollPane의 세로 크기를 조정하여 패널 세로 크기의 2/3로 설정
            Dimension panelSize = panel.getPreferredSize();
            int newScrollPaneHeight = (int) (panelSize.height * 0.66);

            scrollPane.setPreferredSize(new Dimension(0, newScrollPaneHeight));

            // 패널에 JScrollPane 추가
            panel.add(scrollPane, BorderLayout.CENTER);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 연결 닫기
            dbConnector.closeConnection();
        }
    }

    private static String[] getLastBusinessDayRange() {
        Calendar calendar = Calendar.getInstance();

        // 현재 날짜가 토요일이면 금요일로, 일요일이면 금요일로 되돌림
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        } else if (dayOfWeek == Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, -2);
        }

        // 현재 날짜를 todt로 설정
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String todt = dateFormat.format(calendar.getTime());

        // frdt를 todt 기준으로 설정
        calendar.add(Calendar.MONTH, -1);
        String frdt = dateFormat.format(calendar.getTime());

        return new String[]{frdt, todt};
    }

    private static StringBuffer getStockPrice(String likeSrtnCd, String frdt, String todt) throws Exception {
        BufferedReader in = null;
        StringBuffer strBuffer = new StringBuffer();

        try {
            // 외부 API 호출을 위한 URL 설정
            String urlStr = "https://api.odcloud.kr/api/GetStockSecuritiesInfoService/v1/getStockPriceInfo?";
            urlStr += "serviceKey=" + "1%2FWP%2BVc3M5kGU2bikqOuBl9hAtMQ7OeqB24EL0llGF9zC75kdgM1jbsTy90LiI9hmDwU7jeFjW8P%2B1VPFtc%2BDg%3D%3D";
            urlStr += "&beginBasDt=" + frdt;
            urlStr += "&endBasDt=" + todt;
            urlStr += "&itmsNm=" + likeSrtnCd;

            URL obj = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            // API 응답 읽기
            in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));

            String line;
            while ((line = in.readLine()) != null) {
                strBuffer.append(line);
            }

        } finally {
            // BufferedReader 리소스 닫기
            if (in != null) {
                in.close();
            }
        }

        return strBuffer;
    }

    private static void printStockPriceTable(StringBuffer xmlData) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        ByteArrayInputStream input = new ByteArrayInputStream(xmlData.toString().getBytes("UTF-8"));
        Document doc = dBuilder.parse(input);
        doc.getDocumentElement().normalize();

        // 파싱한 XML 데이터를 표 형식으로 출력
        NodeList nList = doc.getElementsByTagName("item");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                String[] rowData = {
                        eElement.getElementsByTagName("ITMS_NM").item(0).getTextContent(),
                        eElement.getElementsByTagName("ITMS_CD").item(0).getTextContent(),
                        eElement.getElementsByTagName("NOW_VAL").item(0).getTextContent(),
                        eElement.getElementsByTagName("MKT_NM").item(0).getTextContent(),
                        eElement.getElementsByTagName("FLUC_RT").item(0).getTextContent(),
                        eElement.getElementsByTagName("FLUC_RT_YTD").item(0).getTextContent(),
                        eElement.getElementsByTagName("MEMO").item(0).getTextContent()
                };

                tableModel.addRow(rowData);
            }
        }
    }

    private static void InterestStockFrame() {
        JFrame interestFrame = new JFrame("관심 주식 검색");
        interestFrame.setLocation(200, 400);
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

        interestFrame.getContentPane().add(panel);
        interestFrame.setSize(400, 400);
        interestFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        interestFrame.setVisible(true);
        interestFrame.setLayout(new BorderLayout());
    }

}
