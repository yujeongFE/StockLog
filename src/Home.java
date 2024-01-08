import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

// 패널 3
import javax.swing.JFrame;
import javax.swing.JLabel;

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
/*
public class Panel3Action { // 관심주식
    static DBconnection dbConnector = new DBconnection(); // DB 연결 객체 생성

    // DB에서 관심 있는 주식을 가져와서 UI에 표시하는 로직

    private DefaultTableModel model;
    public static void displayInterestedStocks(Home home, DBconnection dbConnector) {

        // DB에서 가져오는 정보 NAME, CODE, CATEGORY, MEMO
        JPanel bottomLeftPanel = home.getBottomLeftPanel(); // Home 클래스의 getter 메서드로 bottomLeftPanel을 가져옵니다.
        try {
            Statement statement = dbConnector.getConnection().createStatement();
            String query = "SELECT s.NAME, s.CODE, i.CATEGORY, i.MEMO FROM stock s, interest i WHERE s.NAME = i.NAME AND U_ID = 'id'";
            ResultSet resultSet = statement.executeQuery(query);

            JTable stockTable = createStockTable(resultSet); // ResultSet에서 JTable 생성
            JScrollPane scrollPane = new JScrollPane(stockTable);

            bottomLeftPanel.add(scrollPane); // 생성된 JTable을 패널에 추가
            bottomLeftPanel.revalidate(); // 패널을 갱신하여 변경 사항을 반영

        } catch (SQLException e) {
            e.printStackTrace();
        }

        */
/*//*
/ model 컬럼명 생성성
        String[] columnName = { "종목명", "종목코드", "현재주가", "종류", "전일대비등락", "전일대비등락비", "메모"}; // 보유 여부는 나중에

        // 내부 컬럼 데이터 생성 [가져온 DB에 데이터 사이즈][컬럼 개수]
        String[][] rowData = new String[list.size()][columnName.length];
        for (int row = 0; row < rowData.length; row++) {
            rowData[row][0] = list.get(row).getNAME();
            rowData[row][1] = list.get(row).getCODE();
            rowData[row][2] = list.get(row).getKind_name(); // 현재 주가
            rowData[row][3] = list.get(row).getItem_name(); // 종류
            rowData[row][4] = Integer.toString(list.get(row).getPrice()); // 전일대비 등락
            rowData[row][5] = list.get(row).getQuantity(); // 전일대비등락비
            rowData[row][6] = list.get(row).getMEMO();
        } // end for

        // 모델 만들고 JTable에 모델 입력
        model = new DefaultTableModel(rowData, columnName);
        table = new JTable(model);
        scrollPane.setViewportView(table);*//*


        // 데이터베이스에서 목록을 가져와서 리스트에 추가
        // 쿼리 실행

        // 결과 처리

        // 리소스 해제

    }

    private static JTable createStockTable(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        DefaultTableModel tableModel = new DefaultTableModel();
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            tableModel.addColumn(metaData.getColumnLabel(columnIndex));
        }

        while (resultSet.next()) {
            Object[] rowData = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                rowData[i] = resultSet.getObject(i + 1);
            }
            tableModel.addRow(rowData);
        }

        return new JTable(tableModel);
    }

    public static void searchAndAddStock(JPanel panel) {
        JLabel stock_search = new JLabel("주식 검색 : ");
        JTextField text = new JTextField(15);

        JButton searchButton = new JButton("관심 주식 추가하기");

        searchButton.addActionListener(e -> {
            String stockName = text.getText();
            // 주식을 검색하고 DB에 추가하는 로직
            // boolean added = dbConnector.addStock(stockName); // 주식을 DB에 추가

            */
/*if (added) {
                JOptionPane.showMessageDialog(panel, "주식이 성공적으로 추가되었습니다.");
                // 추가되었으면 해당 주식을 UI에 추가하는 작업을 수행할 수 있음
                // displayInterestedStocks(panel); // 추가된 주식을 다시 UI에 표시할 수 있음 (선택적)
            } else {
                JOptionPane.showMessageDialog(panel, "주식 추가에 실패했습니다.");
            }*//*

        });

        panel.add(stock_search);
        panel.add(text);
        panel.add(searchButton);
    }
}
*/

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
    private JPanel bottomLeftPanel; // bottomLeftPanel 필드 추가
    public Home() {

        JFrame frame = new JFrame("주식 매매 관리 시스템");

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel topLeftPanel = createPanelWithBorder("1"); // 종목지수
        JPanel topRightPanel = createPanelWithBorder("2"); // 매도주식
        bottomLeftPanel = createPanelWithBorder("3"); // 관심주식
        JPanel bottomRightPanel = createPanelWithBorder("4"); // 보유주식
        JPanel rightPanel = createPanelWithBorder("5");

        JPanel bottomPanel = new JPanel(); // 하단바
        bottomPanel.setBackground(Color.GRAY); // 배경색 회색
        bottomPanel.setPreferredSize(new Dimension(frame.getWidth(), 50)); // 높이 50px

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // 패널에 기능 추가
        Panel1Action.addFunctionality(topLeftPanel); // 패널 1에 기능 추가
        Panel2Action.addFunctionality(topRightPanel); // 패널 2에 기능 추가
        // 패널 3에 기능 추가
        // Panel3Action.displayInterestedStocks(bottomLeftPanel); // 관심 주식 표시
        new Panel3Action();

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

    // 3패널 가져오기
    public JPanel getBottomLeftPanel() {
        return bottomLeftPanel;
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
        DBconnection dbConnector = new DBconnection();
        SwingUtilities.invokeLater(() -> {
            Home home = new Home();
            Panel3Action panel3Action = new Panel3Action();

            JPanel bottomLeftPanel = home.getBottomLeftPanel(); // Home 클래스의 bottomLeftPanel 가져오기

        });
    }
}
