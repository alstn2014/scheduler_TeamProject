//등록페이지
package scheduler.schedule;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXDatePicker;

import scheduler.calendar.Cell;
import scheduler.calendar.MyCalendar;
import scheduler.main.Main;
import scheduler.main.MainFrame;

public class Schedule extends JPanel {
	Main main;

	Cell cell;
	MyCalendar cal;
	JTextField t_title; // 제목
	JTextField t_name; // 작성자
	JTextArea t_main; // 내용

	JTextField t_end; // 종료일
	JXDatePicker st_picker = new JXDatePicker();
	JXDatePicker end_picker = new JXDatePicker();
	Choice c_phase;

	JPanel p_north;
	JPanel p_center;
	JPanel p_souht;

	String name;
	String file_loc;
	JButton bt_regist;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	Date st_date;
	Date ed_date;
	Date this_date;
	int schedule_no;

	public Schedule(Main main) {
		this.main = main;
		setLayout(new BorderLayout());
		// 패널
		p_north = new JPanel();
		p_center = new JPanel();
		p_souht = new JPanel();
		// 선택박스
		c_phase = new Choice();

		c_phase.add("미진행");
		c_phase.add("진행중");
		c_phase.add("완료");

		// 텍스트필드 ,에리어
		t_title = new JTextField(68); // 제목
		t_name = new JTextField(name, 8); // 작성자

		t_main = new JTextArea(50, 65); // 일정 내용

		bt_regist = new JButton("등록");

		p_north.setPreferredSize(new Dimension(800, 60));
		p_center.setPreferredSize(new Dimension(800, 400));
		p_souht.setPreferredSize(new Dimension(800, 100));

		t_name.setBackground(new Color(189, 189, 189));
		t_name.setDisabledTextColor(Color.BLACK);
		t_name.setEnabled(false);
		Dimension d = new Dimension(350, 25);
		t_title.setPreferredSize(d);

		p_north.add(new JLabel("제목")); // 타이틀 라벨
		p_north.add(t_title);
		p_north.add(new JLabel("시작일")); // 시작일 라벨

		p_north.add(st_picker);
		p_north.add(new JLabel("종료일")); // 종료일 라벨
		p_north.add(end_picker);
		p_north.add(new JLabel("작성자"));

		p_north.add(t_name);
		p_north.add(new JLabel("진행정도"));
		p_north.add(c_phase);

		p_center.add(t_main);
		p_souht.add(bt_regist);

		st_picker.setDate(Calendar.getInstance().getTime());
		st_picker.setBounds(50, 130, 100, 25);

		end_picker.setDate(Calendar.getInstance().getTime());
		end_picker.setBounds(50, 130, 100, 25);
		setLayout(new BorderLayout());

		add(p_north, BorderLayout.NORTH);
		add(p_center);
		add(p_souht, BorderLayout.SOUTH);
		t_name.setText(main.userInfo.getName());

		// 등록 버튼 이벤트
		bt_regist.addActionListener((e) -> {
			if (bt_regist.getText() == "등록") {
				regist();
				main.mainFrame.showPage(MainFrame.CALENDAR);
			} else if (bt_regist.getText() == "수정") {
				delete(schedule_no);
				deleteCal(schedule_no);
				regist();
				main.mainFrame.showPage(MainFrame.CALENDAR);
			} else if (bt_regist.getText() == "돌아가기") {
				main.mainFrame.showPage(MainFrame.CALENDAR);
			}
		});
		setSize(800, 600);
		setVisible(false);

	} // 생성자 end

	public void regist() {
		// System.out.println("regist실행됨");
		Connection con = main.getCon();
		PreparedStatement pstmt = null;

		// 랜덤 색 생성
		Color backColor = createRandomColor();
		int r = backColor.getRed();
		int g = backColor.getGreen();
		int b = backColor.getBlue();
		String title = t_title.getText();

		int writer_no = main.userInfo.getMember_no(); // 나중에 바꿔야함 멤버의 프라이머리키가 들어갈 것 작성자
		int step_no = c_phase.getSelectedIndex() + 1;
		String content = t_main.getText();

		Date st_date = st_picker.getDate();
		Date ed_date = end_picker.getDate();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String start_date = formatter.format(st_date);
		String end_date = formatter.format(ed_date);
		// 로직~~~~
		String sql = "insert into schedule(title,start_date,end_date,writer_no,step_no,content,color_r,color_g,color_b)";
		sql += " values(?,?,?,?,?,?,?,?,?)";
		System.out.println(sql);
		if (st_date.getTime() <= ed_date.getTime()) {
			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, title);
				pstmt.setString(2, start_date);
				pstmt.setString(3, end_date);
				pstmt.setInt(4, writer_no);
				pstmt.setInt(5, step_no);
				pstmt.setString(6, content);
				pstmt.setInt(7, r);
				pstmt.setInt(8, g);
				pstmt.setInt(9, b);
				int result = pstmt.executeUpdate();
				if (result == 0) {
					JOptionPane.showMessageDialog(this, "등록실패");
				} else {
					insertCal(st_date, ed_date);
					JOptionPane.showMessageDialog(this, "등록성공");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				main.closeDB(pstmt);
			}
		} else {
			JOptionPane.showMessageDialog(this, "알맞은 날짜를 설정하세요");
		}

	}

	public void delete(int schedule_no) {
		Connection con = main.getCon();
		PreparedStatement pstmt = null;

		String sql = "delete from schedule where schedule_no=?";

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, schedule_no);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt);
		}

	}

	public void deleteCal(int schedule_no) {
		Connection con = main.getCon();
		PreparedStatement pstmt = null;

		String sql = "delete from calendar where schedule_no=?";

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, schedule_no);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt);
		}
	}

	public void modi(int schedule_no) {
		Connection con = main.getCon();
		PreparedStatement pstmt = null;

		String title = t_title.getText();
		Date st_date = st_picker.getDate();
		Date ed_date = end_picker.getDate();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String start_date = formatter.format(st_date);
		String end_date = formatter.format(ed_date);
		int writer_no = main.userInfo.getMember_no();
		int step_no = c_phase.getSelectedIndex() + 1;
		String content = t_main.getText();

		String sql = "update schedule set title=?,start_date=?,end_date=?"
				+ ",writer_no=?,step_no=?,content=? where schedule_no =?";
		System.out.println(sql);

		if (st_date.getTime() <= ed_date.getTime()) {
			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, title);
				pstmt.setString(2, start_date);
				pstmt.setString(3, end_date);
				pstmt.setInt(4, writer_no);
				pstmt.setInt(5, step_no);
				pstmt.setString(6, content);
				pstmt.setInt(7, schedule_no);
				int result = pstmt.executeUpdate();
				System.out.println(schedule_no);
				if (result > 0) {
					JOptionPane.showMessageDialog(this, "성공");
				} else {
					JOptionPane.showMessageDialog(this, "실패");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				main.closeDB(pstmt);
			}
		} else {
			JOptionPane.showMessageDialog(this, "알맞은 날짜를 설정하세요");
		}
	}

	public void insertCal(Date st_date, Date ed_date) {// 달력에 보여줄 날자를 등록 calendar테이블에함
		/*
		 * 반복문 시작일 ~ 끝나는 날 ex) 1/1 ~ 1/5 .... getLast 5 (5, 1/1), (5, 1/2), ... (5, 1/5)
		 */
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		String insert_schedule = getLast();// 마지막에 인서트 스케쥴 스케쥴에 일정을 등록하고나서 셀에 보여줄것을 등록!
		long diff = ed_date.getTime() - st_date.getTime();
		long diffdays = diff / (24 * 60 * 60 * 1000);
		System.out.println(diffdays);
		// date시작일
		for (int i = 0; i <= diffdays; i++) {// 무슨스케쥴인지 알아야함 스케쥴에 등록한 일정의 키값을 얻기위해 getlast는 마지막날 키값임
			// now_date = 시작일 +i일
			// db에 인서트
			// 지금날짜랑 스케쥴에 등록한 키값 db에 인서트
			// insert into (scid,date) value (insert_스케쥴,now데이트)

			Date n_date = new Date(st_date.getYear(), st_date.getMonth(), st_date.getDate() + i);
			java.sql.Date now_date = new java.sql.Date(n_date.getTime());

			String sql = "insert into calendar(schedule_no,calendar_date)";
			sql += " values(?,?)";

			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, insert_schedule);
				pstmt.setDate(2, now_date);
				pstmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				main.closeDB(pstmt);
			}
		}

	}

	public String getLast() { // 마지막에 등록된 스케쥴 id값을 찾아줌 캘린더에 붙일 스케쥴을 알아내기 위해 똑같은 일정을 누를때 같은스케쥴이 뜨게하기위해
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select last_insert_id()";
		String key = null;
		System.out.println(con);
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			key = rs.getString(1);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt, rs);
		}
		return key;
	}

	// 초기화 할때 사용
	public void setSchedule() {
		bt_regist.setText("등록");
		t_name.setText(main.userInfo.getName());
		t_title.setEnabled(true);
		t_title.setText("");
		t_main.setEnabled(true);
		t_main.setText("");
		c_phase.setEnabled(true);
		c_phase.select("미진행");
		t_title.setBackground(Color.WHITE);
		t_main.setBackground(Color.white);
		c_phase.setBackground(Color.white);

		st_picker.setDate(Calendar.getInstance().getTime());
		end_picker.setDate(Calendar.getInstance().getTime());
		end_picker.setEnabled(true);
		st_picker.setEnabled(true);
	}

	public void setSchedule(int year, int month, int days) {
		String thisDay = year + "-" + (month + 1) + "-" + days;
		bt_regist.setText("등록");
		t_name.setText(main.userInfo.getName());
		t_title.setEnabled(true);
		t_title.setText("");
		t_main.setEnabled(true);
		t_main.setText("");
		c_phase.setEnabled(true);
		c_phase.select("미진행");
		t_title.setBackground(Color.WHITE);
		t_main.setBackground(Color.white);
		c_phase.setBackground(Color.white);
		System.out.println(thisDay);

		try {
			this_date = format.parse(thisDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(this_date);

		st_picker.setDate(this_date);
		end_picker.setDate(this_date);
		System.out.println(st_picker.getDate());
		end_picker.setEnabled(true);
		st_picker.setEnabled(true);
	}

	public void setSchedule(int schedule_no, String title, String start_date, String end_date, String name, String step,
			String content, int writer_no) {
		this.schedule_no = schedule_no;
		t_title.setText(title);
		t_name.setText(name);
		c_phase.select(step);
		t_main.setText(content);

		if (writer_no != main.userInfo.getMember_no()) {
			t_title.setBackground(new Color(189, 189, 189));
			t_title.setDisabledTextColor(Color.BLACK);
			t_title.setEnabled(false);
			c_phase.setEnabled(false);
			t_main.setBackground(new Color(189, 189, 189));
			t_main.setDisabledTextColor(Color.BLACK);
			t_main.setEnabled(false);

			bt_regist.setText("돌아가기");
			try {
				st_date = format.parse(start_date);
				// System.out.println(st_date);
				st_picker.setDate(st_date);
				ed_date = format.parse(end_date);
				// System.out.println(ed_date);
				end_picker.setDate(ed_date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			st_picker.setEnabled(false);
			end_picker.setEnabled(false);
		} else {

			bt_regist.setText("수정");
			t_title.setEnabled(true);
			t_main.setEnabled(true);
			c_phase.setEnabled(true);

			t_title.setBackground(Color.WHITE);
			t_main.setBackground(Color.white);
			c_phase.setBackground(Color.white);
			st_picker.setEnabled(true);
			end_picker.setEnabled(true);
			try {
				st_date = format.parse(start_date);
				st_picker.setDate(st_date);
				ed_date = format.parse(end_date);
				end_picker.setDate(ed_date);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
	}

	private Color createRandomColor() {
		Random random = new Random();

		float h = random.nextFloat();
		float s = random.nextFloat();
		float b = 1.0f;
		Color c = Color.getHSBColor(h, s, b);
		return c;
	}
}
