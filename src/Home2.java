import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

// 패널 3
import javax.swing.JFrame;
import javax.swing.JLabel;


// 패널 1에 대한 동작을 처리하는 클래스
class PanelAction1 { // 종목 지수
    public static void addFunctionality(JPanel panel) {
        // 패널 1에 추가할 기능 구현
    }
}

// 패널 2에 대한 동작을 처리하는 클래스
class PanelAction2 { // 매도주식
    public static void addFunctionality(JPanel panel) {
        // 패널 2에 추가할 기능 구현

    }
}

class PanelAction2_1 { // 매도주식
    public static void addFunctionality(JPanel panel) {
        // 패널 2에 추가할 기능 구현

    }
}

// 패널 3에 대한 동작을 처리하는 클래스
class PanelAction3 { // 관심 주식
    public static void addFunctionality(JPanel panel) {
        // 패널 3에 추가할 기능 구현
    }
}

// 패널 4에 대한 동작을 처리하는 클래스
class PanelAction4 { // 매도주식
    public static void addFunctionality(JPanel panel) {
        // 패널 4에 추가할 기능 구현
    }
}

// 패널 5에 대한 동작을 처리하는 클래스
class PanelAction5 { // 매도주식
    public static void addFunctionality(JPanel panel) {
        // 패널 5에 추가할 기능 구현
    }
}

class PanelAction5_1 { // 매도주식
    public static void addFunctionality(JPanel panel) {
        // 패널 5-1에 추가할 기능 구현
    }
}

// 하단바에 대한 동작을 처리하는 클래스
class PanelAction6 { // 매도주식
    public static void addFunctionality(JPanel panel) {
        // 하단바에 추가할 기능 구현
    }
}

public class Home2 {
    private JPanel bottomLeftPanel; // bottomLeftPanel 필드 추가
    public Home2() {

        JFrame frame = new JFrame("주식 매매 관리 시스템");

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel topLeftPanel = createPanelWithBorder("1"); // 종목지수
        JPanel topRightPanel1 = createPanelWithBorder("2"); // 종목 차트 (분봉)
        JPanel topRightPanel2 = createPanelWithBorder("2-1"); // 종목 차트 (일봉)
        bottomLeftPanel = createPanelWithBorder("3"); // 관심주식
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
        PanelAction2.addFunctionality(topRightPanel1); // 패널 2에 기능 추가
        PanelAction2_1.addFunctionality(topRightPanel2); // 패널 2-1에 기능 추가
        // 패널 3에 기능 추가
        PanelAction3.addFunctionality(rightBottomPanel); // 패널 3에 기능 추가
        PanelAction4.addFunctionality(bottomRightPanel); // 패널 4에 기능 추가
        PanelAction5.addFunctionality(rightPanel1); // 패널 5에 기능 추가
        PanelAction5_1.addFunctionality(rightPanel2); // 패널 5-1에 기능 추가
        PanelAction6.addFunctionality(bottomPanel); // 하단 바에 기능 추가

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
            try {
                Home2 home2 = new Home2();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}