
package scheduler.member.login;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import scheduler.main.Main;
import scheduler.main.MainFrame;
import scheduler.member.SHA;

public class Login extends JFrame { // �α���
	Main main;

	JLabel la_id; // ���̵�
	JLabel la_pw; // ��й�ȣ
	JTextField t_id; // ���̵� �ؽ�Ʈ �ʵ�
	JPasswordField t_pw; // ��й�ȣ �ؽ�Ʈ �ʵ�
	JButton bt_add; // ���
	JButton bt_login; // �α���
	JPanel p_north; // ����
	JPanel p_center; // �����г�
	JPanel p_south; // ���� �г�
	JPanel[] pages = new JPanel[2];

	LoginKeyAdapter loginKeyAdapter;

	public Login(Main main) {
		setIconImage(main.getIcon()); // ������ ����
		setTitle(main.getTitle()); // Ÿ��Ʋ ����

		this.main = main;
		loginKeyAdapter = new LoginKeyAdapter(this);

		la_id = new JLabel("���̵�"); // ���̵�
		la_pw = new JLabel("��й�ȣ"); // ��й�ȣ
		t_id = new JTextField(10);
		t_pw = new JPasswordField(10);
		bt_add = new JButton("���");// ��Ϲ�ư
		bt_login = new JButton("�α���");// �α��ι�ư

		p_north = new JPanel();// ��
		p_center = new JPanel();// ���� �г�
		p_south = new JPanel(); // ���� �г�

		// �� ������
		Dimension dm = new Dimension(60, 20);
		la_id.setPreferredSize(dm);
		la_pw.setPreferredSize(dm);

		p_north.add(la_id); // ���̵� ��
		p_north.add(t_id); // ���̵� �ؽ�Ʈ�ʵ�
		p_center.add(la_pw); // ��й�ȣ ��
		p_center.add(t_pw); // ��й�ȣ �ؽ�Ʈ�ʵ�

		p_south.add(bt_add); // ��� ��ư
		p_south.add(bt_login); // �α��� ��ư

		add(p_north, BorderLayout.NORTH);
		add(p_center);
		add(p_south, BorderLayout.SOUTH);

		bt_add.addActionListener((e) -> {
			regist();
		});

		bt_login.addActionListener((e) -> {
			login();
		});

		t_id.addKeyListener(loginKeyAdapter);
		t_pw.addKeyListener(loginKeyAdapter);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				main.frameClose();
			}
		});

		setLocationRelativeTo(null);
		setSize(400, 150);
		this.setVisible(true);
	}

	public void regist() { // ���
		new RegistMember(main);
	}

	public void login() { // �α���
		String id = t_id.getText();
		String password = new String(t_pw.getPassword());

		String shapw = "";
		try {
			shapw = SHA.encrypt(password);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		String sql = "select * from member where id=? and password=?";
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, shapw);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int user_num = rs.getInt("member_no");
				String sql2 = "update member set last_login_date=now() where member_no=?";// ������ �α��νð�
				pstmt2 = con.prepareStatement(sql2);
				pstmt2.setInt(1, user_num);
				pstmt2.executeUpdate();
				JOptionPane.showMessageDialog(this, "�α��� ����");

				main.userInfo.setData(rs.getInt("member_no"), rs.getString("id"), rs.getString("name"),
						rs.getString("email"), rs.getString("phone"), rs.getBoolean("admin"));
				main.userInfo.setShowTrayMessage(true);
				main.setMainFrame(new MainFrame(main));
				this.setVisible(false);
			} else {
				JOptionPane.showMessageDialog(this, "���̵�� ��й�ȣ�� Ȯ�����ּ���");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt, rs);
			main.closeDB(pstmt2);
		}
	}
}

// �α��� �� ���� Ű �������� ó���ϴ� Ŭ����
class LoginKeyAdapter extends KeyAdapter {
	Login login;

	public LoginKeyAdapter(Login login) {
		this.login = login;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_ENTER) {
			login.login();
		}
	}
}