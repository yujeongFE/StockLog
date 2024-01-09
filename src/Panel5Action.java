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

class Panel5Action { // 주식 매매 기록
    static Object[] row = new Object[8];
    // 데이터를 담을 테이블 모델 생성
    static DefaultTableModel tableModel = new DefaultTableModel();

    public static void addFunctionality(JPanel panel, String userId) {
        // 데이터베이스 연결
        DBconnection dbConnector = new DBconnection();
        Connection connection = dbConnector.getConnection();

        String id = userId;
        // SQL 쿼리 실행
        String query = "SELECT s.NAME, l.COMPANY, l.BUYORSELL, l.DATE, l.PRICE, l.QTY, l.RRATIO, l.MEMO FROM stock s, log l WHERE s.CODE = l.CODE AND U_ID = '" + id + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // 원하는 컬럼 순서와 이름을 추가
            tableModel.addColumn("종목명");
            tableModel.addColumn("증권사");
            tableModel.addColumn("매도/매수");
            tableModel.addColumn("날짜");
            tableModel.addColumn("주식 단가");
            tableModel.addColumn("수량");
            tableModel.addColumn("수익률");
            tableModel.addColumn("메모");

            // 결과셋의 데이터를 테이블 모델에 추가
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

            // JButton 생성 및 패널에 추가
            JButton searchButton = new JButton("매도/매수 기록 추가");
            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SellBuyFrame();
                }
            });
            panel.add(searchButton, BorderLayout.SOUTH);  // Add the button to the SOUTH position of the panel

            // JLabel 생성 및 패널에 추가
            JLabel label = new JLabel("주식 매매 일지", SwingConstants.CENTER); // SwingConstants.CENTER로 가운데 정렬
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 연결 닫기
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
    }
}