package scheduler.main.traymessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import scheduler.main.Main;

public class MessageThread extends Thread {
	Main main;
	int initCnt = 0; // 처음 로그인시 등록 된 스케줄 갯수
	SchedulerTray trayMessage;

	public MessageThread(Main main) {
		this.main = main;
		trayMessage = new SchedulerTray(main);
		initCnt = nowScheduleCnt();

	}

	public void run() {
		while (true) {
			int nowCnt = nowScheduleCnt();
			if (nowCnt > initCnt) {	// 새로운 값이 기존값보다 클때 동작
				initCnt = nowCnt; // 값 변경
				if (main.userInfo.isShowTrayMessage()) {// 메시지 본다고 설정 되어있을때만 출력
					showMessage(); // 메시지 출력
				}
			}
			try {
				sleep(10000);// 10초 간격으로 실행
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// 현재 등록된 스케줄의 갯수를 가져오는 메서드
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

	// 새로운 일정 등록 시 메시지 출력해주는 메서드
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
			if (main.userInfo.getMember_no() != no) { // 등록한 사용자가 현재 접속한 사용자와 같지 않다면 실행
				trayMessage.display("새로운 일정 등록", name + "님이 새로운 일정을 등록하셨습니다!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt, rs);
		}

	}
}
