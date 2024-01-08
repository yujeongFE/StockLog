import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.sql.*;

// 패널 3
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.table.*;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;

public class Panel3Action extends JFrame{ // 관심주식
    DBconnection dbConnector = new DBconnection(); // DB 연결 객체 생성
    Statement statement; // sql
    PreparedStatement prestat; // sql 준비
    ResultSet resultSet; //sql

    JLabel label, name, code, price, category, deungrak, deungrakbi, memo ; // 관심 주식 라벨
    JPanel center; // panel
    JScrollPane sp;
    Vector outer, title, noresult, msg; // noresult는 검색 결과 없음, msg는 테이블에 들어갈 vector
    JTable table;
    DefaultTableModel model; // table 상으로 db 모델 불러옴

    static String id; // 받아온 id
    public Panel3Action(){
        makeGUI(); // 화면 구성
        // prepareDB(); // db 준비작업
        display(null); // 첫화면에서 테이블의 모든 내용 보여주기 위해 select하는 함수
        model.setDataVector(outer, title);

        // 테이블의 사이즈 조정
        table.setPreferredScrollableViewportSize(new Dimension(table.getPreferredSize().width, table.getPreferredSize().height));
        // 위에서 너비는 500으로, 높이는 테이블의 높이

    }

    public void makeGUI(){
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // 보유 주식
        label = new JLabel("관심 주식", JLabel.CENTER);
        topPanel.add(label);

        title = new Vector(); // 보여지는 속성
        outer = new Vector();
        noresult = new Vector();
        msg = new Vector();

        // "종목명", "종목코드", "현재주가", "종류", "전일대비등락", "전일대비등락비", "메모"
        title.add("종목명");
        title.add("종목코드");
        // title.add("현재주가");
        title.add("종류");
        // title.add("전일대비등락");
        // title.add("전일대비등락비");
        title.add("메모");

        noresult.add("관심 주식이 없습니다. 관심 주식을 추가해 주세요."); // 결과 없을 때

        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent w){
                try{
                    resultSet.close();
                    statement.close();
                    // conn은 어떻게 닫지?

                    setVisible(false);
                    dispose();
                    System.exit(0);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                super.windowClosing(w);
            }
        });

        model = new DefaultTableModel(); // defaulttablemodel 생성
        table = new JTable(model); // model 사용하여 Jtable 생성

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me){
                // 관심 있는 주식 행의 index 알아내기 : JTable의 메소드
                int index = table.getSelectedRow();

                // index 사용하여 out 안의 작은 벡터 in 꺼내기
                Vector msg = (Vector)outer.get(index);

                // in 안의 주식 종목 코드를 변수에 저장
                String code = (String)msg.get(2);
            }
        });

        sp = new JScrollPane(table); // table panel

        center = new JPanel(new BorderLayout());
        center.add(sp, BorderLayout.CENTER); // 가운데에 추가

        Container c = getContentPane(); //?
        c.add(label, BorderLayout.NORTH);
        c.add(center, BorderLayout.CENTER);
    }

    public void display(String query){
        try {
            // DB 연결 확인
            Connection connection = dbConnector.getConnection();
            if (connection == null) {
                System.out.println("DB 연결 실패");
                return;
            }

            // SQL 쿼리 실행
            Statement statement = dbConnector.getConnection().createStatement();
            query = "SELECT s.NAME, s.CODE, i.CATEGORY, i.MEMO FROM stock s, interest i WHERE s.CODE = i.CODE AND U_ID = '" + id + "'";
            ResultSet resultSet = statement.executeQuery(query);

            // 데이터 모델 초기화
            model = new DefaultTableModel();
            table.setModel(model);

            // 테이블 헤더 설정
            model.setColumnIdentifiers(title);

            outer.clear();
            while(resultSet.next()){
                msg = new Vector<String>();
                msg.add(resultSet.getString(1));
                msg.add(resultSet.getString(2));
                msg.add(resultSet.getString(3));
                msg.add(resultSet.getString(4));
                outer.add(msg);
            }

            // 데이터 모델 업데이트
            model.setDataVector(outer, title);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*//
    // Panel3Action 클래스에 displayInterestedStocks 메서드 추가
    public void displayInterestedStocks(JPanel bottomLeftPanel, DBconnection dbConnector) {
        try {
            // 기존의 DB 연결 확인 및 쿼리 실행 부분
            Connection connection = dbConnector.getConnection();
            if (connection == null) {
                System.out.println("DB 연결 실패");
                return;
            }

            String query = "SELECT s.NAME, s.CODE, i.CATEGORY, i.MEMO FROM stock s, interest i WHERE s.NAME = i.NAME AND U_ID = 'id'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // ResultSet 초기화
            this.resultSet = resultSet;

            // 데이터 모델 초기화
            model = new DefaultTableModel();
            table.setModel(model);

            // 테이블 헤더 설정
            model.setColumnIdentifiers(title);

            outer.clear();
            while (resultSet.next()) {
                msg = new Vector<String>();
                msg.add(resultSet.getString(1));
                msg.add(resultSet.getString(2));
                msg.add(resultSet.getString(3));
                msg.add(resultSet.getString(4));
                outer.add(msg);
            }

            // 데이터 모델 업데이트
            model.setDataVector(outer, title);

            bottomLeftPanel.add(getContentPane()); // 혹은 원하는 패널을 추가하는 로직 구현

            adjustTableWidth(bottomLeftPanel.getWidth());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    // 테이블 크기 조절
    public void adjustTableWidth(int panelWidth) {
        if (table != null) {
            table.setPreferredScrollableViewportSize(new Dimension(panelWidth, table.getPreferredSize().height));
        }
    }
}