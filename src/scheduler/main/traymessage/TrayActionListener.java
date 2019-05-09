/*
 * 트레이 아이콘 마우스 클릭할때 동작하는 리스너
 * */
package scheduler.main.traymessage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import scheduler.main.Main;
import scheduler.main.MainFrame;

public class TrayActionListener implements ActionListener {

	Main main;
	SchedulerTray schedulerTray;

	public TrayActionListener(Main main, SchedulerTray schedulerTray) {
		this.main = main;
		this.schedulerTray = schedulerTray;
	}

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == schedulerTray.trayIcon) { // 트레이 아이콘 누르면 창 열림
			openWindow();
		} else if (obj == schedulerTray.calendar) { // 달력 메뉴 보기
			main.mainFrame.showPage(MainFrame.CALENDAR);
			openWindow();
		} else if (obj == schedulerTray.schedule) { // 스케줄 메뉴 보기
			main.mainFrame.showPage(MainFrame.SCHEDULE);
			openWindow();
		} else if (obj == schedulerTray.board) { // 게시판 메뉴 보기
			main.mainFrame.showPage(MainFrame.NOTICEBOARD);
			openWindow();
		} else if (obj == schedulerTray.member) { // 회원 관리 메뉴 보기
			main.mainFrame.showPage(MainFrame.MEMBERMODIFIED);
			openWindow();
		} else if (obj == schedulerTray.close) { // 창닫기
			close();
		}

	}

	public void close() {
		main.frameClose();
	}

	public void openWindow() {
		main.mainFrame.setVisible(true);
		main.mainFrame.setExtendedState(main.mainFrame.NORMAL);
	}

}
