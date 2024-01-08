import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

// 패널 3
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.text.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.*;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTable;

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

            // 데이터를 담을 테이블 모델 생성
            DefaultTableModel tableModel = new DefaultTableModel();

            // 원하는 컬럼 순서와 이름을 추가
            tableModel.addColumn("종목명");
            tableModel.addColumn("종목코드");
            // tableModel.addColumn("현재주가");
            tableModel.addColumn("종류");
            // tableModel.addColumn("전일대비등락");
            // tableModel.addColumn("전일대비등락비");
            tableModel.addColumn("메모");

            // 결과셋의 데이터를 테이블 모델에 추가
            while (resultSet.next()) {
                Object[] row = new Object[4];
                for (int i = 0; i < 4; i++) {
                    row[i] = resultSet.getObject(i + 1);
                }
                tableModel.addRow(row);
            }

            // 테이블 생성 및 패널에 추가
            JTable table = new JTable(tableModel);

            // 테이블 크기 조정
            table.setPreferredScrollableViewportSize(table.getPreferredSize());

            // JScrollPane으로 테이블을 감싸기
            JScrollPane scrollPane = new JScrollPane(table);

            // 패널의 레이아웃을 BorderLayout으로 설정
            panel.setLayout(new BorderLayout());

            // JScrollPane의 세로 크기를 조정하여 패널 세로 크기의 2/3로 설정
            Dimension panelSize = panel.getPreferredSize();
            int newScrollPaneHeight = (int) (panelSize.height * 0.66); // 2/3의 크기
            scrollPane.setPreferredSize(new Dimension(0, newScrollPaneHeight)); // 가로 크기는 자동으로 조정됨

            // 패널에 JScrollPane 추가
            panel.add(scrollPane, BorderLayout.CENTER);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 연결 닫기
            dbConnector.closeConnection();
        }
    }
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
    static String userId; // 사용자 id 저장 변수 추가

    public Home(String userId) {
        this.userId = userId;

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
        Panel3Action.addFunctionality(bottomLeftPanel, userId); // 관심 주식 표시
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

    /*// 3패널 가져오기
    public JPanel getBottomLeftPanel() {
        return bottomLeftPanel;
    }*/

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
            Home home = new Home(userId);

            //
            /*PanelAction panel3Action = new PanelAction();
            JPanel bottomLeftPanel = home.getBottomLeftPanel(); // Home 클래스의 bottomLeftPanel 가져오기*/

        });
    }
}
