import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class Login{
    public static void main(String[] args) {
        JFrame loginFrame = new JFrame("로그인창");
        JPanel panel = new JPanel(new GridBagLayout());

        JLabel l1 = new JLabel("ID");
        JTextField text = new JTextField(15);

        JLabel l2 = new JLabel("passwd");
        JPasswordField value = new JPasswordField(15);

        ((AbstractDocument) text.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text.matches("[a-zA-Z]*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        ((AbstractDocument) value.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text.matches("[a-zA-Z]*")) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

        JButton loginButton = new JButton("로그인");

        JLabel notMemberLabel = new JLabel("아직 회원이 아니신가요?");
        notMemberLabel.setForeground(Color.GRAY);

        JLabel signUpLabel = new JLabel("회원가입");
        signUpLabel.setForeground(Color.BLACK);
        signUpLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signUpLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame signUpFrame = new JFrame("회원가입");
                JPanel signUpPanel = new JPanel(new GridBagLayout());

                JLabel l11 = new JLabel("ID");
                JTextField signUpID = new JTextField(15);

                JLabel l12 = new JLabel("passwd");
                JPasswordField signUpPasswd = new JPasswordField(15);

                JLabel l13 = new JLabel("passwd 확인");
                JPasswordField signUpPasswdConfirm = new JPasswordField(15);

                ((AbstractDocument) signUpID.getDocument()).setDocumentFilter(new DocumentFilter() {
                    @Override
                    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                            throws BadLocationException {
                        if (text.matches("[a-zA-Z]*")) {
                            super.replace(fb, offset, length, text, attrs);
                        }
                    }
                });

                ((AbstractDocument) signUpPasswd.getDocument()).setDocumentFilter(new DocumentFilter() {
                    @Override
                    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                            throws BadLocationException {
                        if (text.matches("[a-zA-Z]*")) {
                            super.replace(fb, offset, length, text, attrs);
                        }
                    }
                });

                ((AbstractDocument) signUpPasswdConfirm.getDocument()).setDocumentFilter(new DocumentFilter() {
                    @Override
                    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                            throws BadLocationException {
                        if (text.matches("[a-zA-Z]*")) {
                            super.replace(fb, offset, length, text, attrs);
                        }
                    }
                });

                JButton signUpButton = new JButton("회원가입");
                signUpButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String id = signUpID.getText();
                        String passwd = new String(signUpPasswd.getPassword());
                        String confirmPasswd = new String(signUpPasswdConfirm.getPassword());

                        if (id.isEmpty() || passwd.isEmpty() || confirmPasswd.isEmpty()) {
                            JOptionPane.showMessageDialog(signUpFrame, "모든 입력란을 채워주세요.");
                        } else if (!passwd.equals(confirmPasswd)) {
                            JOptionPane.showMessageDialog(signUpFrame, "입력한 passwd 값이 다릅니다.");
                        } else {
                            String data = id + "님 회원가입에 성공하셨습니다.";
                            JOptionPane.showMessageDialog(signUpFrame, data);
                            signUpFrame.dispose();
                            loginFrame.setVisible(true);
                        }
                    }
                });

                GridBagConstraints signUpGbc = new GridBagConstraints();
                signUpGbc.insets = new Insets(5, 5, 5, 5);

                signUpGbc.gridx = 0;
                signUpGbc.gridy = 0;
                signUpPanel.add(l11, signUpGbc);

                signUpGbc.gridx = 1;
                signUpGbc.gridy = 0;
                signUpPanel.add(signUpID, signUpGbc);

                signUpGbc.gridx = 0;
                signUpGbc.gridy = 1;
                signUpPanel.add(l12, signUpGbc);

                signUpGbc.gridx = 1;
                signUpGbc.gridy = 1;
                signUpPanel.add(signUpPasswd, signUpGbc);

                signUpGbc.gridx = 0;
                signUpGbc.gridy = 2;
                signUpPanel.add(l13, signUpGbc);

                signUpGbc.gridx = 1;
                signUpGbc.gridy = 2;
                signUpPanel.add(signUpPasswdConfirm, signUpGbc);

                signUpGbc.gridx = 1;
                signUpGbc.gridy = 3;
                signUpPanel.add(signUpButton, signUpGbc);

                signUpFrame.add(signUpPanel);
                signUpFrame.setSize(400, 300);
                signUpFrame.setLocationRelativeTo(loginFrame);
                signUpFrame.setVisible(true);
                signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(l1, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(text, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(l2, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(value, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(loginButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(notMemberLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(signUpLabel, gbc);

        loginFrame.add(panel);

        loginFrame.setSize(420, 350);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String id = text.getText();
                String passwd = new String(value.getPassword());

                if (id.isEmpty() || passwd.isEmpty()) {
                    JOptionPane.showMessageDialog(loginFrame, "로그인에 실패했습니다. ID와 비밀번호를 모두 입력하세요.");
                } else {
                    String data = id + "님 로그인에 성공하셨습니다.";
                    JOptionPane.showMessageDialog(loginFrame, data);
                }
            }
        });

        loginFrame.setVisible(true);
    }
}
