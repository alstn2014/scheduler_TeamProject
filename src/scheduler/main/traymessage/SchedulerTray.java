/*
 * Ʈ����â�� �޽��� ����� �� ����ϴ� Ŭ����
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
	CheckboxMenuItem showMessageChecker; // �˸� �޽��� ���� ���� Ȯ��
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

		shortcut = new Menu("�ٷΰ���");
		calendar = new MenuItem("�޷�");
		schedule = new MenuItem("�������");
		board = new MenuItem("�Խ���");
		member = new MenuItem("��������");
		showMessageChecker = new CheckboxMenuItem("�˶� ���", true);
		close = new MenuItem("����");

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

		// Ʈ���� â���� ������ ȭ�� ���
		trayIcon.addActionListener(actionListener);

		// ���� ��ư ������ ����
		calendar.addActionListener(actionListener);
		schedule.addActionListener(actionListener);
		board.addActionListener(actionListener);
		member.addActionListener(actionListener);
		close.addActionListener(actionListener);
		showMessageChecker.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				main.userInfo.setShowTrayMessage(showMessageChecker.getState()); // �˸� �޽��� ���� ���� ����
			}
		});

		// Ʈ����â�� ������ �߰�
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}

	}

	// Ʈ���� �޽��� ������ִ� �޼���
	public void display(String title, String content) {
		trayIcon.displayMessage(title, content, MessageType.INFO);
	}

}