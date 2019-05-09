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

public class RegistMember extends Frame {// 회원가입
	Main main;

	// 디자인 명 멤버 변수 주석 전까지
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
	JButton bt_check; // 중복체크
	JButton bt_add; // 등록

	CustomTextField t_email;
	CustomTextField t_phone;

	// 멤버 변수 DB연결 전까지
	boolean idChecked = false; // id 체크여부
	boolean flag = true;

	public RegistMember(Main main) {

		setIconImage(main.getIcon()); // 아이콘 설정
		setTitle(main.getTitle()); // 타이틀 설정

		this.main = main;

		this.setLayout(new BorderLayout());
		p_east = new JPanel(); // 동
		p_west = new JPanel(); // 서
		p_center = new JPanel(); // 센터
		p_south = new JPanel(); // 남
		la_id = new JLabel("아이디");
		la_name = new JLabel("이름");
		la_password = new JLabel("비밀번호");
		la_password2 = new JLabel("비밀번호 확인");
		la_email = new JLabel("이메일");
		la_phone = new JLabel("전화번호");
		t_id = new JTextField(15);
		PromptSupport.setPrompt("아이디 3글자 이상", t_id);
		t_name = new JTextField(15);
		PromptSupport.setPrompt("홍길동", t_name);
		t_password = new JPasswordField(15);
		PromptSupport.setPrompt("비밀번호", t_password);
		t_password2 = new JPasswordField(15);
		PromptSupport.setPrompt("비밀번호 확인", t_password2);
		t_email = new CustomTextField(15);
		PromptSupport.setPrompt("example@address.com", t_email);
		t_phone = new CustomTextField(15);
		PromptSupport.setPrompt("000-0000-0000", t_phone);
		bt_check = new JButton("중복체크");
		bt_add = new JButton("등록");

		// 라벨 사이즈
		Dimension la_dm = new Dimension(100, 22);
		la_id.setPreferredSize(la_dm);
		la_name.setPreferredSize(la_dm);
		la_password.setPreferredSize(la_dm);
		la_password2.setPreferredSize(la_dm);
		la_email.setPreferredSize(la_dm);
		la_phone.setPreferredSize(la_dm);
		p_west.setPreferredSize(new Dimension(100, 300));

		// 센터 부착
		p_west.add(la_id);
		p_center.add(t_id);
		p_east.add(bt_check); // 중복체크 버튼
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
		// 남쪽 등록버튼
		p_south.add(bt_add);
		add(p_east, BorderLayout.EAST);
		add(p_west, BorderLayout.WEST);
		add(p_center);
		add(p_south, BorderLayout.SOUTH);

		// 등록
		bt_add.addActionListener((e) -> {
			regist();
		});

		// 등록에서 X버튼 누를시 창 닫는 이벤트
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});

		// 중복체크
		bt_check.addActionListener((e) -> {
			checkId();
		});

		setLocationRelativeTo(null);
		setSize(400, 300);
		setVisible(flag);

	}

	// 등록
	public void regist() {
		// 중복확인을 눌러야 가입가능
		if (idChecked == false) { // 중복 체크
			t_id.requestFocus();
			JOptionPane.showMessageDialog(this, "아이디 중복체크 하지않음");
			return;
		}

		String id = t_id.getText();
		String name = t_name.getText();
		String password = new String(t_password.getPassword());
		String password2 = new String(t_password2.getPassword());
		String email = t_email.getText();
		String phone = t_phone.getText();

		if (name.length() == 0) {
			JOptionPane.showMessageDialog(this, "이름을 입력하세요");
			t_name.requestFocus();// 커서 올리기
			return;
		}

		if (password.length() == 0) {
			JOptionPane.showMessageDialog(this, "비밀번호를 입력하세요");
			t_password.requestFocus();// 커서 올리기
			return;
		}

		if (password2.length() == 0) {
			JOptionPane.showMessageDialog(this, "비밀번호 확인을 입력하세요");
			t_password.requestFocus();// 커서 올리기
			return;
		}

		if (password.equals(password2)) {

		} else {
			JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다 ");
			t_password2.requestFocus();// 커서 올리기
			return;
		}

		if (MemberDataCheck.checkEmail(t_email.getText())) {
		} else {
			JOptionPane.showMessageDialog(this, "이메일을 올바른 형식으로 \n 예) abc@abc.com");
			t_email.requestFocus();// 커서 올리기
			return;
		}

		if (MemberDataCheck.checkPhone(t_phone.getText())) {
		} else {
			JOptionPane.showMessageDialog(this, "전화번호를 올바른 형식으로 입력하세요\n 예) 010-0000-0000");
			t_phone.requestFocus();// 커서 올리기
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
				JOptionPane.showMessageDialog(this, "가입실패");
			} else {
				JOptionPane.showMessageDialog(this, "가입성공");
				flag = false;
				setVisible(flag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt);
		}
	}

	// 중복체크
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
			if (rs.next()) {// true면 가입 불가
				JOptionPane.showMessageDialog(this, "이미 사용중인 아이디입니다");
			} else if (id.length() >= 3 && Pattern.matches("^[a-z0-9_]{3,12}$", t_id.getText())) {
				idChecked = true;
				JOptionPane.showMessageDialog(this, "사용 가능한 아이디입니다");
				t_id.setEnabled(false);
			} else {
				JOptionPane.showMessageDialog(this, "아이디는 영문 또는 영문 숫자 포함 3글자이상 입력하세요");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt, rs);
		}
	}
}
