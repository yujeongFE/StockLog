import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

// 패널 3
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

// 패널 1에 대한 동작을 처리하는 클래스
class Panel1Action { // 종목 지수
    public static void addFunctionality(JPanel panel) {
        // 패널 1에 추가할 기능 구현
    }
}

// 패널 2에 대한 동작을 처리하는 클래스
class Panel2Action { // 매도주식
    public static void addFunctionality(JPanel panel) {
        // 패널 2에 추가할 기능 구현

    }
}

// 패널 3에 대한 동작을 처리하는 클래스
class Panel3Action { // 관심주식
    public static void addFunctionality(JPanel panel) {
        // 패널 3에 추가할 기능 구현
        try {
            JLabel stock_search = new JLabel("주식 검색 : "); // 주식 검색
            JTextField text = new JTextField(15);
            String stockName = text.getText(); // 주식 이름 검색

            // 날짜 범위 설정
            String[] dateRange = getLastBusinessDayRange();
            String frdt = dateRange[0];
            String todt = dateRange[1];

            // String stockInfo = getStockInfo(stockName); // getStockInfo 함수는 주식 정보를 가져오는 메서드

            StringBuffer stockPriceData = getStockInfo(stockName, frdt, todt);

            if (stockPriceData.length() > 0) {
                // 데이터 파싱 및 표로 정리하여 출력
                printStockPriceTable(stockPriceData);
            } else {
                JOptionPane.showMessageDialog(panel, "해당 주식 데이터가 없습니다. 다시 입력하여 주세요");
            }

        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
        }
        /*JButton button = new JButton("주식 정보 가져오기");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 주식 정보를 받아오는 API 요청 예시 (실제 API 사용 시에는 해당 API의 URL이나 라이브러리를 사용해야 함)
                String stockName = JOptionPane.showInputDialog(panel, "주식 이름을 입력하세요:");
                // API 요청 후 받은 데이터를 처리하고 출력하는 예시
                String stockInfo = getStockInfo(stockName); // getStockInfo 함수는 주식 정보를 가져오는 메서드라고 가정
                JOptionPane.showMessageDialog(panel, stockInfo); // stock이 보여지도록함

                // 메모를 저장하는 코드 예시 (실제로는 저장 위치 등을 고려해야 함)
                String memo = JOptionPane.showInputDialog(panel, "메모를 작성하세요:");
                saveMemo(stockName, memo); // saveMemo 함수는 메모를 저장하는 메서드라고 가정
            }
        });
        panel.add(button);*/
    }

    // 날짜 조정하는 메소드
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

    // 주식 API 받아오는 메소드
    private static StringBuffer getStockInfo(String likeSrtnCd, String frdt, String todt) throws Exception {
        BufferedReader in = null;
        StringBuffer strBuffer = new StringBuffer();

        try {
            // 외부 API 호출을 위한 URL 설정
            String urlStr = "https://api.odcloud.kr/api/GetStockSecuritiesInfoService/v1/getStockPriceInfo?";
            urlStr += "serviceKey=" + "1%2FWP%2BVc3M5kGU2bikqOuBl9hAtMQ7OeqB24EL0llGF9zC75kdgM1jbsTy90LiI9hmDwU7jeFjW8P%2B1VPFtc%2BDg%3D%3D";  // API 키를 적절하게 설정
            urlStr += "&beginBasDt=" + frdt;
            urlStr += "&endBasDt=" + todt;
            urlStr += "&likeSrtnCd=" + likeSrtnCd;  // 변수명 수정

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
        StringBuilder output = new StringBuilder();
        output.append("기준일자\t종목단축코드\tISIN코드\t종목명\t시장구분\t종가\t전일대비등락\t전일대비등락비\t시가\t고가\n");

        for (int i = 0; i < itemList.getLength(); i++) {
            Node itemNode = itemList.item(i);
            if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                Element itemElement = (Element) itemNode;

                // 각 행 추가
                output.append(getValue("basDt", itemElement)).append("\t");
                output.append(getValue("srtnCd", itemElement)).append("\t");
                output.append(getValue("isinCd", itemElement)).append("\t");
                output.append(getValue("itmsNm", itemElement)).append("\t");
                output.append(getValue("mrktCtg", itemElement)).append("\t");
                output.append(getValue("clpr", itemElement)).append("\t");
                output.append(getValue("vs", itemElement)).append("\t");
                output.append(getValue("fltRt", itemElement)).append("\t");
                output.append(getValue("mkp", itemElement)).append("\t");
                output.append(getValue("hipr", itemElement)).append("\n");
            }
        }

        // 결과 출력
        System.out.println(output.toString());
    }
    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node.getNodeValue();
    }

    // 메모를 저장하는 메서드 예시
    /*private static void saveMemo(String stockName, String memo) {
        // 메모를 저장하는 코드 (파일이나 데이터베이스에 저장)
        // 실제로는 저장 위치나 방법을 고려하여 데이터를 저장해야 합니다.
        System.out.println("주식: " + stockName + ", 메모: " + memo + " 저장 완료");
    }*/
}

// 패널 4에 대한 동작을 처리하는 클래스
class Panel4Action { // 매도주식
    public static void addFunctionality(JPanel panel) {
        // 패널 4에 추가할 기능 구현
    }
}

// 패널 5에 대한 동작을 처리하는 클래스
class Panel5Action { // 매도주식
    public static void addFunctionality(JPanel panel) {
        // 패널 5에 추가할 기능 구현
    }
}

// 하단바에 대한 동작을 처리하는 클래스
class Panel6Action { // 매도주식
    public static void addFunctionality(JPanel panel) {
        // 하단바에 추가할 기능 구현
    }
}



public class Home {
    public Home() {
        JFrame frame = new JFrame("주식 매매 관리 시스템");

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel topLeftPanel = createPanelWithBorder("1"); // 종목지수
        JPanel topRightPanel = createPanelWithBorder("2"); // 매도주식
        JPanel bottomLeftPanel = createPanelWithBorder("3"); // 관심주식
        JPanel bottomRightPanel = createPanelWithBorder("4"); // 보유주식
        JPanel rightPanel = createPanelWithBorder("5");

        JPanel bottomPanel = new JPanel(); // 하단바
        bottomPanel.setBackground(Color.GRAY); // 배경색 회색
        bottomPanel.setPreferredSize(new Dimension(frame.getWidth(), 50)); // 높이 50px

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // 패널에 기능 추가
        Panel1Action.addFunctionality(topLeftPanel); // 패널 1에 기능 추가
        Panel2Action.addFunctionality(topRightPanel); // 패널 2에 기능 추가
        Panel3Action.addFunctionality(bottomLeftPanel); // 패널 3에 기능 추가
        Panel4Action.addFunctionality(bottomRightPanel); // 패널 4에 기능 추가
        Panel5Action.addFunctionality(rightPanel); // 패널 5에 기능 추가
        Panel6Action.addFunctionality(bottomPanel); // 하단 바에 기능 추가

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // gbc.weightx = 1.0;은 해당 컴포넌트가 그리드의 가로 방향으로 가능한 최대 공간을 차지
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH; // 가로 및 세로 방향으로 모두 공간을 채움 // fill 속성은 해당 컴포넌트가 할당받은 공간을 어떻게 채울지를 지정
        mainPanel.add(topLeftPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(topRightPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(bottomLeftPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;

        mainPanel.add(bottomRightPanel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH; // 가로 및 세로 방향으로 모두 공간을 채움
        mainPanel.add(rightPanel, gbc);

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
        DBconnection dbConnector = new DBconnection(); // DB 연결 객체 생성

        SwingUtilities.invokeLater(Home::new);
    }
}
