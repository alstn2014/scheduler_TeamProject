package scheduler.member.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.poi.sl.usermodel.Placeholder;
import org.jdesktop.swingx.prompt.PromptSupport;

import scheduler.main.Main;
import scheduler.member.SHA;
import scheduler.member.manager.MemberDataCheck;

public class RegistMember extends Frame {// ȸ������
	Main main;

	// ������ �� ��� ���� �ּ� ������
	JPanel p_east;
	JPanel p_west;
	JPanel p_center;
	JPanel p_south;
	JLabel la_id;
	JLabel la_name;
	JLabel la_password;
	JLabel la_password2;
	JLabel la_email;
	JLabel la_phone;
	JTextField t_id;
	JTextField t_name;
	JPasswordField t_password;
	JPasswordField t_password2;
	JButton bt_check; // �ߺ�üũ
	JButton bt_add; // ���

	CustomTextField t_email;
	CustomTextField t_phone;

	// ��� ���� DB���� ������
	boolean idChecked = false; // id üũ����
	boolean flag = true;

	public RegistMember(Main main) {

		setIconImage(main.getIcon()); // ������ ����
		setTitle(main.getTitle()); // Ÿ��Ʋ ����

		this.main = main;

		this.setLayout(new BorderLayout());
		p_east = new JPanel(); // ��
		p_west = new JPanel(); // ��
		p_center = new JPanel(); // ����
		p_south = new JPanel(); // ��
		la_id = new JLabel("���̵�");
		la_name = new JLabel("�̸�");
		la_password = new JLabel("��й�ȣ");
		la_password2 = new JLabel("��й�ȣ Ȯ��");
		la_email = new JLabel("�̸���");
		la_phone = new JLabel("��ȭ��ȣ");
		t_id = new JTextField(15);
		PromptSupport.setPrompt("���̵� 3���� �̻�", t_id);
		t_name = new JTextField(15);
		PromptSupport.setPrompt("ȫ�浿", t_name);
		t_password = new JPasswordField(15);
		PromptSupport.setPrompt("��й�ȣ", t_password);
		t_password2 = new JPasswordField(15);
		PromptSupport.setPrompt("��й�ȣ Ȯ��", t_password2);
		t_email = new CustomTextField(15);
		PromptSupport.setPrompt("example@address.com", t_email);
		t_phone = new CustomTextField(15);
		PromptSupport.setPrompt("000-0000-0000", t_phone);
		bt_check = new JButton("�ߺ�üũ");
		bt_add = new JButton("���");

		// �� ������
		Dimension la_dm = new Dimension(100, 22);
		la_id.setPreferredSize(la_dm);
		la_name.setPreferredSize(la_dm);
		la_password.setPreferredSize(la_dm);
		la_password2.setPreferredSize(la_dm);
		la_email.setPreferredSize(la_dm);
		la_phone.setPreferredSize(la_dm);
		p_west.setPreferredSize(new Dimension(100, 300));

		// ���� ����
		p_west.add(la_id);
		p_center.add(t_id);
		p_east.add(bt_check); // �ߺ�üũ ��ư
		p_west.add(la_name);
		p_center.add(t_name);
		p_west.add(la_password);
		p_west.add(la_password2);
		p_center.add(t_password);
		p_center.add(t_password2);
		p_west.add(la_email);
		p_center.add(t_email);
		p_west.add(la_phone);
		p_center.add(t_phone);
		// ���� ��Ϲ�ư
		p_south.add(bt_add);
		add(p_east, BorderLayout.EAST);
		add(p_west, BorderLayout.WEST);
		add(p_center);
		add(p_south, BorderLayout.SOUTH);

		// ���
		bt_add.addActionListener((e) -> {
			regist();
		});

		// ��Ͽ��� X��ư ������ â �ݴ� �̺�Ʈ
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});

		// �ߺ�üũ
		bt_check.addActionListener((e) -> {
			checkId();
		});

		setLocationRelativeTo(null);
		setSize(400, 300);
		setVisible(flag);

	}

	// ���
	public void regist() {
		// �ߺ�Ȯ���� ������ ���԰���
		if (idChecked == false) { // �ߺ� üũ
			t_id.requestFocus();
			JOptionPane.showMessageDialog(this, "���̵� �ߺ�üũ ��������");
			return;
		}

		String id = t_id.getText();
		String name = t_name.getText();
		String password = new String(t_password.getPassword());
		String password2 = new String(t_password2.getPassword());
		String email = t_email.getText();
		String phone = t_phone.getText();

		if (name.length() == 0) {
			JOptionPane.showMessageDialog(this, "�̸��� �Է��ϼ���");
			t_name.requestFocus();// Ŀ�� �ø���
			return;
		}

		if (password.length() == 0) {
			JOptionPane.showMessageDialog(this, "��й�ȣ�� �Է��ϼ���");
			t_password.requestFocus();// Ŀ�� �ø���
			return;
		}

		if (password2.length() == 0) {
			JOptionPane.showMessageDialog(this, "��й�ȣ Ȯ���� �Է��ϼ���");
			t_password.requestFocus();// Ŀ�� �ø���
			return;
		}

		if (password.equals(password2)) {

		} else {
			JOptionPane.showMessageDialog(this, "��й�ȣ�� ��ġ���� �ʽ��ϴ� ");
			t_password2.requestFocus();// Ŀ�� �ø���
			return;
		}

		if (MemberDataCheck.checkEmail(t_email.getText())) {
		} else {
			JOptionPane.showMessageDialog(this, "�̸����� �ùٸ� �������� \n ��) abc@abc.com");
			t_email.requestFocus();// Ŀ�� �ø���
			return;
		}

		if (MemberDataCheck.checkPhone(t_phone.getText())) {
		} else {
			JOptionPane.showMessageDialog(this, "��ȭ��ȣ�� �ùٸ� �������� �Է��ϼ���\n ��) 010-0000-0000");
			t_phone.requestFocus();// Ŀ�� �ø���
			return;
		}
		String shaPw = null;
		try {
			shaPw = SHA.encrypt(password);
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		String sql = "insert into member(id,name,password,email,phone) values(?,?,?,?,?)";

		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, name);
			pstmt.setString(3, shaPw);
			pstmt.setString(4, email);
			pstmt.setString(5, phone);
			int result = pstmt.executeUpdate();
			if (result == 0) {
				JOptionPane.showMessageDialog(this, "���Խ���");
			} else {
				JOptionPane.showMessageDialog(this, "���Լ���");
				flag = false;
				setVisible(flag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt);
		}
	}

	// �ߺ�üũ
	public void checkId() {

		String id = t_id.getText();
		String sql = "select * from member where id=?";
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {// true�� ���� �Ұ�
				JOptionPane.showMessageDialog(this, "�̹� ������� ���̵��Դϴ�");
			} else if (id.length() >= 3 && Pattern.matches("^[a-z0-9_]{3,12}$", t_id.getText())) {
				idChecked = true;
				JOptionPane.showMessageDialog(this, "��� ������ ���̵��Դϴ�");
				t_id.setEnabled(false);
			} else {
				JOptionPane.showMessageDialog(this, "���̵�� ���� �Ǵ� ���� ���� ���� 3�����̻� �Է��ϼ���");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt, rs);
		}
	}
}
