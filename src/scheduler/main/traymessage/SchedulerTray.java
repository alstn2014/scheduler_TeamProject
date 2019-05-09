/*
 * 트레이창에 메시지 출력할 때 사용하는 클래스
 * */
package scheduler.main.traymessage;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import scheduler.main.Main;

public class SchedulerTray {
	Main main;
	Image image;
	TrayIcon trayIcon;
	SystemTray tray = SystemTray.getSystemTray();
	PopupMenu popup;
	Menu shortcut;
	MenuItem calendar;
	MenuItem schedule;
	MenuItem board;
	MenuItem member;
	CheckboxMenuItem showMessageChecker; // 알림 메시지 볼지 여부 확인
	MenuItem close;
	ActionListener actionListener;

	public SchedulerTray(Main main) {
		this.main = main;
		image = main.getIcon();
		popup = new PopupMenu();

		actionListener = new TrayActionListener(main, this);

		trayIcon = new TrayIcon(image, "Scheduler");
		trayIcon.setImageAutoSize(true);
		trayIcon.setToolTip("Scheduler");

		shortcut = new Menu("바로가기");
		calendar = new MenuItem("달력");
		schedule = new MenuItem("일정등록");
		board = new MenuItem("게시판");
		member = new MenuItem("정보수정");
		showMessageChecker = new CheckboxMenuItem("알람 사용", true);
		close = new MenuItem("종료");

		shortcut.add(calendar);
		shortcut.add(schedule);
		shortcut.add(board);
		shortcut.add(member);
		popup.add(shortcut);
		popup.addSeparator();
		popup.add(showMessageChecker);
		popup.addSeparator();
		popup.add(close);

		trayIcon.setPopupMenu(popup);

		// 트레이 창에서 누르면 화면 출력
		trayIcon.addActionListener(actionListener);

		// 열기 버튼 누를때 동작
		calendar.addActionListener(actionListener);
		schedule.addActionListener(actionListener);
		board.addActionListener(actionListener);
		member.addActionListener(actionListener);
		close.addActionListener(actionListener);
		showMessageChecker.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				main.userInfo.setShowTrayMessage(showMessageChecker.getState()); // 알림 메시지 볼지 말지 설정
			}
		});

		// 트레이창에 아이콘 추가
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}

	}

	// 트레이 메시지 출력해주는 메서드
	public void display(String title, String content) {
		trayIcon.displayMessage(title, content, MessageType.INFO);
	}

}