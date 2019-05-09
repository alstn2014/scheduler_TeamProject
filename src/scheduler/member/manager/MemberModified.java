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

public class MemberModified extends JPanel { // 정보수정
	Main main;
	Connection con;
	// 디자인 명
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
	JButton bt_modified; // 수정버튼
	JButton bt_remove; // 탈퇴

	String shaPw = null; // 회원 수정, 탈퇴에서 사용
	String shaPassword = null; // 회원 수정, 탈퇴에서 사용

	String memberId; // 현제 접속중인 유저
	String memberName;
	String memberPassword;
	String memberEmail;
	String memberPhone;

	public MemberModified(Main main) {
		this.main = main;
		con = main.getCon();

		this.setLayout(new BorderLayout());
		p_east = new JPanel(); // 동
		p_west = new JPanel(); // 서
		p_center = new JPanel(); // 센터
		p_south = new JPanel(); // 남
		la_id = new JLabel("아이디");
		la_name = new JLabel("이름");
		la_password = new JLabel("비밀번호");
		la_password2 = new JLabel("변경할 비밀번호");
		la_password3 = new JLabel("변경할 비밀번호 확인");
		la_email = new JLabel("이메일");
		la_phone = new JLabel("전화번호");
		t_id = new JTextField(15);
		t_name = new JTextField(15);
		t_password = new JPasswordField(15);
		t_password2 = new JPasswordField(15);
		t_password3 = new JPasswordField(15);
		t_email = new JTextField(15);
		t_phone = new JTextField(15);
		bt_modified = new JButton("수정");
		bt_remove = new JButton("회원 탈퇴");

		// 라벨 사이즈
		Dimension la_dm = new Dimension(100, 22);
		la_id.setPreferredSize(la_dm);
		la_name.setPreferredSize(la_dm);
		la_password.setPreferredSize(la_dm);
		la_password2.setPreferredSize(la_dm);
		la_password3.setPreferredSize(la_dm);
		la_email.setPreferredSize(la_dm);
		la_phone.setPreferredSize(la_dm);
		p_west.setPreferredSize(new Dimension(200, 300));

		// 센터 부착
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

		// 남쪽 등록버튼
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
		// 패스워드
		t_password.setText(memberPassword);
		t_email.setText(memberEmail);
		t_phone.setText(memberPhone);

		// 수정
		bt_modified.addActionListener((e) -> {
			modified();
		});

		bt_remove.addActionListener((e) -> {
			remove();
		});

		this.setPreferredSize(new Dimension(400, 300));
	}

	// 회원탈퇴
	public void remove() {
		/*------------------------암호화---------------------------------*/
		shaPassword = new String(t_password.getPassword());
		try {
			shaPw = SHA.encrypt(shaPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*------------------------쿼리-----------------------------------*/
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		String sql = "select password from member where password=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, shaPw);
			rs = pstmt.executeQuery();

			if (rs.next() && rs.getString("password").equals(shaPw)) {
				if (JOptionPane.showConfirmDialog(this, "탈퇴하시겠습니까?\n탈퇴 후 프로그램이 종료됩니다.", "회원 탈퇴",
						JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
					// 삭제진행 딜리트 쿼리
					String sql2 = "delete from member where member_no=?";
					JOptionPane.showMessageDialog(this, "탈퇴 완료");
					pstmt2 = con.prepareStatement(sql2);
					pstmt2.setInt(1, main.userInfo.getMember_no());
					pstmt2.executeUpdate();
					main.frameClose();
				}

			} else {
				JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.");
			}
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt, rs);
		}

	}

	// 회원정보 수정
	public void modified() { // update 쿼리
		shaPassword = new String(t_password.getPassword()); // 현재 비번
		String shaPassword2 = new String(t_password2.getPassword());// 변경 비번
		String shaPassword3 = new String(t_password3.getPassword());// 변경 비번 확인

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
			if (rs.next()) {// 비밀번호 맞으면 아래 구문 수행
				if (shaPassword2.equals("") && shaPassword3.equals("")) {// 변경할 비밀번호가 입력되어 있지 않으면 정보만 수정
					if (MemberDataCheck.checkEmail(t_email.getText())
							&& MemberDataCheck.checkPhone(t_phone.getText())) {

						String sql2 = "update member set email=?, phone=? where member_no=?";
						pstmt2 = con.prepareStatement(sql2);
						pstmt2.setString(1, t_email.getText());
						pstmt2.setString(2, t_phone.getText());
						pstmt2.setInt(3, main.userInfo.getMember_no());
						pstmt2.executeUpdate();
						t_password.setText("");
						JOptionPane.showMessageDialog(this, "수정성공");
					} else {
						JOptionPane.showMessageDialog(this, "전화번호 또는 이메일 형식을 올바르게 작성하세요");
					}
				} else if (shaPw2.equals(shaPw3)) { // 변경할 비밀번호가 입력되어있고, 두 비밀번호가 일치되어 있으면 비밀번호 변경
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

						JOptionPane.showMessageDialog(this, "수정성공");
					} else {
						JOptionPane.showMessageDialog(this, "전화번호 또는 이메일 형식을 올바르게 작성하세요");
					}
				} else { // 변경할 비밀번호가 입력되었으나, 일치하지 않으면 비밀번호 변경 안함
					JOptionPane.showMessageDialog(this, "변경할 비밀번호가 일치하지 않습니다.");
				}
			} else {
				JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다");
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
