/*
 * 처음 실행되는 main 화면.
 * 모든 클래스에서 사용되는 메서드/변수 저장
 * */
package scheduler.main;

import java.awt.Image;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import scheduler.main.traymessage.MessageThread;
import scheduler.main.user.UserInfo;
import scheduler.member.login.Login;

public class Main {
	private final DBManager dbManager = new DBManager(); // DB연결 관련
	public final UserInfo userInfo = new UserInfo(); // 유저 정보 저장
	public MainFrame mainFrame;
	private final Image iconImg = Toolkit.getDefaultToolkit().getImage("res/icon.png");
	private String appTitle = "TeamScheduler";

	// 화면 닫을때 발생하는 메서드a
	public void frameClose() {
		dbManager.closeDB();
		System.exit(0);
	}

	// mainframe 설정하는 메서드
	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	// 아이콘 출력해주는 메서드
	public Image getIcon() {
		return iconImg;
	}

	// 타이틀 출력해주는 메서드
	public String getTitle() {
		return appTitle;
	}

	// DB관련 메서드 시작
	public Connection getCon() { // 컨넥션 객체 반화하는 메서드
		return dbManager.getCon();
	}

	public void closeDB(PreparedStatement pstmt, ResultSet rs) {
		dbManager.closeDB(pstmt, rs);
	}

	public void closeDB(PreparedStatement pstmt) {
		dbManager.closeDB(pstmt);
	}
	// DB 관련 메서드 끝

	public static void main(String[] args) {
		Main main = new Main();
		MessageThread msgThread = new MessageThread(main);
		msgThread.start();
		new Login(main);

	}

}
