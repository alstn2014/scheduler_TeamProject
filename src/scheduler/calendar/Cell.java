//날자 정보가 들어있는 셀
package scheduler.calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import scheduler.main.Main;
import scheduler.main.MainFrame;
import scheduler.schedule.Schedule;

//박스레이아웃
public class Cell extends JPanel {
	MyCalendar myCalendar;
	PlanInfo infoLabel;
	Schedule schedule;
	Main main;
	PlanCount planCount;
	ArrayList<JLabel> plan_list = new ArrayList<JLabel>();
	JLabel la_day, plan_count;
	String printDay;
	JPanel p_center;
	int year, month, days;

	public Cell(Main main) {

		this.main = main;

		la_day = new JLabel();
		plan_count = new JLabel();
		plan_count.setPreferredSize(new Dimension(110, 13));
		p_center = new JPanel();
		planCount = new PlanCount();
		setLayout(new BorderLayout());
		add(la_day, BorderLayout.NORTH);
		add(p_center);
		p_center.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 1));
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				main.mainFrame.showPage(MainFrame.SCHEDULE);// 패널누르면 일정등록페이지로 이동
				((Schedule) main.mainFrame.getPages(MainFrame.SCHEDULE)).setSchedule(year, month, days);

			}
		});

		setPreferredSize(new Dimension(60, 60));
		setBorder(BorderFactory.createLineBorder(Color.black));

	}

	public void setCellDate(int year, int month, int days) {
		this.year = year;
		this.month = month;
		this.days = days;

		if (year > 0) {// 1 캘린더 테이블에서 날자조회 - 데이터가 있으면 라벨을 추가함 + 이벤트추가 라벨에는 작성자 + 스케줄제목 - 이벤트에는 일정보기화면으로
			setSchedule();
		}

	}

	public void setCellColor(Color color) {
		p_center.setBackground(color);
	}

	public void setOtherMonthDay() {
		setCellColor(Color.GRAY);
		la_day.setText("");
	}

	public void setMonthDay() {
		setCellColor(null);
		la_day.setText("" + days);
	}

	public void showPlan() {
		main.mainFrame.showPage(MainFrame.SCHEDULE);

	}

	public void labelDel() {
		for (int i = 0; i < plan_list.size(); i++) {
			p_center.remove(plan_list.get(i));
		}

		while (plan_list.size() > 0) {
			plan_list.remove(0);
		}
		this.updateUI();
	}

	public void setSchedule() {
		Connection con = main.getCon();

		String sql = "SELECT title, writer_no, name, calendar_date,s.schedule_no, s.start_date as start_date"
				+ " FROM schedule s, calendar c, member m"
				+ " WHERE s.schedule_no=c.schedule_no and m.member_no=s.writer_no and calendar_date=? order by s.start_date";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String now_date = year + "-" + (month + 1) + "-" + days;

		try {
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			pstmt.setString(1, now_date);

			rs = pstmt.executeQuery();
			rs.last();
			int total = rs.getRow();
			rs.beforeFirst();

			planCount.removeData();
			if (total > 3) {
				for (int i = 0; i < total; i++) {
					rs.next();
					int schedule_no = rs.getInt("schedule_no");
					String title = rs.getString("title");
					String name = rs.getString("name");
					String start_date = rs.getString("start_date");
					PlanInfo tmpLabel = new PlanInfo(name, title, schedule_no, main, start_date, year, month, days,
							planCount);

					plan_list.add(tmpLabel);
					if (plan_list.size() < 3) {
						p_center.add(tmpLabel);

					} else {
						planCount.addData(tmpLabel);
						plan_count.setText("+" + (plan_list.size() - 2) + "...");
						plan_count.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent e) {
								planCount.setVisible(true);
							}
						});
					}
				}
				p_center.add(plan_count);
				plan_list.add(plan_count);
			} else if (total < 4) {
				for (int i = 0; i < total; i++) {
					rs.next();
					int schedule_no = rs.getInt("schedule_no");
					String title = rs.getString("title");
					String name = rs.getString("name");
					String start_date = rs.getString("start_date");
					PlanInfo tmpLabel = new PlanInfo(name, title, schedule_no, main, start_date, year, month, days,
							planCount);
					plan_list.add(tmpLabel);
					p_center.add(tmpLabel);
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			main.closeDB(pstmt, rs);
		}

	}

}
