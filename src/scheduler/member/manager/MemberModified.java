package scheduler.member.manager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import scheduler.main.Main;
import scheduler.member.SHA;

public class MemberModified extends JPanel { // ��������
	Main main;
	Connection con;
	// ������ ��
	JPanel p_east;
	JPanel p_west;
	JPanel p_center;
	JPanel p_south;
	JLabel la_id;
	JLabel la_name;
	JLabel la_password;
	JLabel la_password2;
	JLabel la_password3;
	JLabel la_email;
	JLabel la_phone;
	JTextField t_id;
	JTextField t_name;
	JPasswordField t_password;
	JPasswordField t_password2;
	JPasswordField t_password3;
	JTextField t_email;
	JTextField t_phone;
	JButton bt_modified; // ������ư
	JButton bt_remove; // Ż��

	String shaPw = null; // ȸ�� ����, Ż�𿡼� ���
	String shaPassword = null; // ȸ�� ����, Ż�𿡼� ���

	String memberId; // ���� �������� ����
	String memberName;
	String memberPassword;
	String memberEmail;
	String memberPhone;

	public MemberModified(Main main) {
		this.main = main;
		con = main.getCon();

		this.setLayout(new BorderLayout());
		p_east = new JPanel(); // ��
		p_west = new JPanel(); // ��
		p_center = new JPanel(); // ����
		p_south = new JPanel(); // ��
		la_id = new JLabel("���̵�");
		la_name = new JLabel("�̸�");
		la_password = new JLabel("��й�ȣ");
		la_password2 = new JLabel("������ ��й�ȣ");
		la_password3 = new JLabel("������ ��й�ȣ Ȯ��");
		la_email = new JLabel("�̸���");
		la_phone = new JLabel("��ȭ��ȣ");
		t_id = new JTextField(15);
		t_name = new JTextField(15);
		t_password = new JPasswordField(15);
		t_password2 = new JPasswordField(15);
		t_password3 = new JPasswordField(15);
		t_email = new JTextField(15);
		t_phone = new JTextField(15);
		bt_modified = new JButton("����");
		bt_remove = new JButton("ȸ�� Ż��");

		// �� ������
		Dimension la_dm = new Dimension(100, 22);
		la_id.setPreferredSize(la_dm);
		la_name.setPreferredSize(la_dm);
		la_password.setPreferredSize(la_dm);
		la_password2.setPreferredSize(la_dm);
		la_password3.setPreferredSize(la_dm);
		la_email.setPreferredSize(la_dm);
		la_phone.setPreferredSize(la_dm);
		p_west.setPreferredSize(new Dimension(200, 300));

		// ���� ����
		p_west.add(la_id);
		p_center.add(t_id).setEnabled(false);
		p_west.add(la_name);
		p_center.add(t_name).setEnabled(false);
		p_west.add(la_password);
		p_center.add(t_password);
		p_west.add(la_password2);
		p_center.add(t_password2);
		p_west.add(la_password3);
		p_center.add(t_password3);
		p_west.add(la_email);
		p_center.add(t_email);
		p_west.add(la_phone);
		p_center.add(t_phone);

		// ���� ��Ϲ�ư
		p_south.add(bt_modified);
		p_south.add(bt_remove);
		add(p_east, BorderLayout.EAST);
		add(p_west, BorderLayout.WEST);
		add(p_center);
		add(p_south, BorderLayout.SOUTH);

		memberId = main.userInfo.getId();
		memberName = main.userInfo.getName();
		memberEmail = main.userInfo.getEmail();
		memberPhone = main.userInfo.getPhone();

		t_id.setText(memberId);
		t_name.setText(memberName);
		// �н�����
		t_password.setText(memberPassword);
		t_email.setText(memberEmail);
		t_phone.setText(memberPhone);

		// ����
		bt_modified.addActionListener((e) -> {
			modified();
		});

		bt_remove.addActionListener((e) -> {
			remove();
		});

		this.setPreferredSize(new Dimension(400, 300));
	}

	// ȸ��Ż��
	public void remove() {
		/*------------------------��ȣȭ---------------------------------*/
		shaPassword = new String(t_password.getPassword());
		try {
			shaPw = SHA.encrypt(shaPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*------------------------����-----------------------------------*/
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		String sql = "select password from member where password=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, shaPw);
			rs = pstmt.executeQuery();

			if (rs.next() && rs.getString("password").equals(shaPw)) {
				if (JOptionPane.showConfirmDialog(this, "Ż���Ͻðڽ��ϱ�?\nŻ�� �� ���α׷��� ����˴ϴ�.", "ȸ�� Ż��",
						JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
					// �������� ����Ʈ ����
					String sql2 = "delete from member where member_no=?";
					JOptionPane.showMessageDialog(this, "Ż�� �Ϸ�");
					pstmt2 = con.prepareStatement(sql2);
					pstmt2.setInt(1, main.userInfo.getMember_no());
					pstmt2.executeUpdate();
					main.frameClose();
				}

			} else {
				JOptionPane.showMessageDialog(this, "��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
			}
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt, rs);
		}

	}

	// ȸ������ ����
	public void modified() { // update ����
		shaPassword = new String(t_password.getPassword()); // ���� ���
		String shaPassword2 = new String(t_password2.getPassword());// ���� ���
		String shaPassword3 = new String(t_password3.getPassword());// ���� ��� Ȯ��

		shaPw = null;
		String shaPw2 = "";
		String shaPw3 = "";
		try {
			shaPw = SHA.encrypt(shaPassword);
			shaPw2 = SHA.encrypt(shaPassword2);
			shaPw3 = SHA.encrypt(shaPassword3);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		String sql = "select password from member where password='" + shaPw + "'";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (rs.next()) {// ��й�ȣ ������ �Ʒ� ���� ����
				if (shaPassword2.equals("") && shaPassword3.equals("")) {// ������ ��й�ȣ�� �ԷµǾ� ���� ������ ������ ����
					if (MemberDataCheck.checkEmail(t_email.getText())
							&& MemberDataCheck.checkPhone(t_phone.getText())) {

						String sql2 = "update member set email=?, phone=? where member_no=?";
						pstmt2 = con.prepareStatement(sql2);
						pstmt2.setString(1, t_email.getText());
						pstmt2.setString(2, t_phone.getText());
						pstmt2.setInt(3, main.userInfo.getMember_no());
						pstmt2.executeUpdate();
						t_password.setText("");
						JOptionPane.showMessageDialog(this, "��������");
					} else {
						JOptionPane.showMessageDialog(this, "��ȭ��ȣ �Ǵ� �̸��� ������ �ùٸ��� �ۼ��ϼ���");
					}
				} else if (shaPw2.equals(shaPw3)) { // ������ ��й�ȣ�� �ԷµǾ��ְ�, �� ��й�ȣ�� ��ġ�Ǿ� ������ ��й�ȣ ����
					if (MemberDataCheck.checkEmail(t_email.getText())
							&& MemberDataCheck.checkPhone(t_phone.getText())) {
						String sql2 = "update member  set password=?, email=?, phone=? where member_no=?";
						pstmt2 = con.prepareStatement(sql2);
						pstmt2.setString(1, shaPw3);
						pstmt2.setString(2, t_email.getText());
						pstmt2.setString(3, t_phone.getText());
						pstmt2.setInt(4, main.userInfo.getMember_no());
						pstmt2.executeUpdate();
						t_password.setText("");
						t_password2.setText("");
						t_password3.setText("");

						JOptionPane.showMessageDialog(this, "��������");
					} else {
						JOptionPane.showMessageDialog(this, "��ȭ��ȣ �Ǵ� �̸��� ������ �ùٸ��� �ۼ��ϼ���");
					}
				} else { // ������ ��й�ȣ�� �ԷµǾ�����, ��ġ���� ������ ��й�ȣ ���� ����
					JOptionPane.showMessageDialog(this, "������ ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
				}
			} else {
				JOptionPane.showMessageDialog(this, "��й�ȣ�� ��ġ���� �ʽ��ϴ�");
			}
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt, rs);
			main.closeDB(pstmt2);
		}
	}
}
