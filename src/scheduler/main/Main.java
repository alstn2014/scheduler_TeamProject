/*
 * ó�� ����Ǵ� main ȭ��.
 * ��� Ŭ�������� ���Ǵ� �޼���/���� ����
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
	private final DBManager dbManager = new DBManager(); // DB���� ����
	public final UserInfo userInfo = new UserInfo(); // ���� ���� ����
	public MainFrame mainFrame;
	private final Image iconImg = Toolkit.getDefaultToolkit().getImage("res/icon.png");
	private String appTitle = "TeamScheduler";

	// ȭ�� ������ �߻��ϴ� �޼���a
	public void frameClose() {
		dbManager.closeDB();
		System.exit(0);
	}

	// mainframe �����ϴ� �޼���
	public void setMainFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	// ������ ������ִ� �޼���
	public Image getIcon() {
		return iconImg;
	}

	// Ÿ��Ʋ ������ִ� �޼���
	public String getTitle() {
		return appTitle;
	}

	// DB���� �޼��� ����
	public Connection getCon() { // ���ؼ� ��ü ��ȭ�ϴ� �޼���
		return dbManager.getCon();
	}

	public void closeDB(PreparedStatement pstmt, ResultSet rs) {
		dbManager.closeDB(pstmt, rs);
	}

	public void closeDB(PreparedStatement pstmt) {
		dbManager.closeDB(pstmt);
	}
	// DB ���� �޼��� ��

	public static void main(String[] args) {
		Main main = new Main();
		MessageThread msgThread = new MessageThread(main);
		msgThread.start();
		new Login(main);

	}

}
