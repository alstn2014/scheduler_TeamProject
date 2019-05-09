package scheduler.main.traymessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import scheduler.main.Main;

public class MessageThread extends Thread {
	Main main;
	int initCnt = 0; // ó�� �α��ν� ��� �� ������ ����
	SchedulerTray trayMessage;

	public MessageThread(Main main) {
		this.main = main;
		trayMessage = new SchedulerTray(main);
		initCnt = nowScheduleCnt();

	}

	public void run() {
		while (true) {
			int nowCnt = nowScheduleCnt();
			if (nowCnt > initCnt) {	// ���ο� ���� ���������� Ŭ�� ����
				initCnt = nowCnt; // �� ����
				if (main.userInfo.isShowTrayMessage()) {// �޽��� ���ٰ� ���� �Ǿ��������� ���
					showMessage(); // �޽��� ���
				}
			}
			try {
				sleep(10000);// 10�� �������� ����
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// ���� ��ϵ� �������� ������ �������� �޼���
	private int nowScheduleCnt() {
		int cnt = 0;

		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select count(*) from schedule";

		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			cnt = rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt, rs);
		}

		return cnt;
	}

	// ���ο� ���� ��� �� �޽��� ������ִ� �޼���
	private void showMessage() {
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select m.member_no as no, m.name as name from schedule s, member m where s.writer_no = m.member_no order by s.schedule_no desc LIMIT 1";

		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			rs.next();
			String name = rs.getString("name");
			int no = rs.getInt("no");
			if (main.userInfo.getMember_no() != no) { // ����� ����ڰ� ���� ������ ����ڿ� ���� �ʴٸ� ����
				trayMessage.display("���ο� ���� ���", name + "���� ���ο� ������ ����ϼ̽��ϴ�!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt, rs);
		}

	}
}
