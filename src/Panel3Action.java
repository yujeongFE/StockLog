import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

// 패널 3에 대한 동작을 처리하는 클래스
class Panel3Action { // 관심주식
    static Object[] row = new Object[7];
    // 데이터를 담을 테이블 모델 생성
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
            tableModel.addColumn("종목명"); //
            tableModel.addColumn("종목코드"); //
            tableModel.addColumn("현재주가");
            tableModel.addColumn("시장 구분"); //
            tableModel.addColumn("전일대비등락");
            tableModel.addColumn("전일대비등락비");
            tableModel.addColumn("메모"); //

            // 결과셋의 데이터를 테이블 모델에 추가
            String stockName = null; // 변수를 루프 바깥에 선언하고 초기화
            while (resultSet.next()) {
                row[0] = resultSet.getObject(1);
                stockName = resultSet.getObject(1).toString(); // Object를 String으로 변환하여 stockName에 저장
                row[1] = resultSet.getObject(2);
                row[3] = resultSet.getObject(3);
                row[6] = resultSet.getObject(4);
                // tableModel.addRow(row);
            }

            // 날짜 범위 설정
            String[] dateRange = getLastBusinessDayRange();
            String frdt = dateRange[0];
            String todt = dateRange[1];

            if(stockName != null){
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

            // JLabel 생성 및 패널에 추가
            JLabel label = new JLabel("관심 주식", SwingConstants.CENTER); // SwingConstants.CENTER로 가운데 정렬
            panel.add(label, BorderLayout.NORTH); // BorderLayout의 NORTH 위치에 추가

            // 테이블 생성 및 패널에 추가
            JTable table = new JTable(tableModel);

            // 테이블 크기 조정
            table.setPreferredScrollableViewportSize(table.getPreferredSize());

            // JScrollPane으로 테이블을 감싸기
            JScrollPane scrollPane = new JScrollPane(table);

            // JScrollPane의 세로 크기를 조정하여 패널 세로 크기의 2/3로 설정
            Dimension panelSize = panel.getPreferredSize();
            int newScrollPaneHeight = (int) (panelSize.height * 0.66); // 2/3의 크기

            System.out.println(newScrollPaneHeight);

            scrollPane.setPreferredSize(new Dimension(0, newScrollPaneHeight)); // 가로 크기는 자동으로 조정됨

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
            urlStr += "serviceKey=" + "1%2FWP%2BVc3M5kGU2bikqOuBl9hAtMQ7OeqB24EL0llGF9zC75kdgM1jbsTy90LiI9hmDwU7jeFjW8P%2B1VPFtc%2BDg%3D%3D";  // API 키를 적절하게 설정
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

        NodeList itemList = doc.getElementsByTagName("item");

        // 출력 행 구성

        Node itemNode = itemList.item(0);
        if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
            Element itemElement = (Element) itemNode;
            row[2] = getValue("clpr", itemElement);
            row[4] = getValue("vs", itemElement);
            row[5] = getValue("fltRt", itemElement);
            tableModel.addRow(row);
        }

    }
    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }
}