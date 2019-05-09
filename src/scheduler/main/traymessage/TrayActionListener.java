/*
 * Ʈ���� ������ ���콺 Ŭ���Ҷ� �����ϴ� ������
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
		if (obj == schedulerTray.trayIcon) { // Ʈ���� ������ ������ â ����
			openWindow();
		} else if (obj == schedulerTray.calendar) { // �޷� �޴� ����
			main.mainFrame.showPage(MainFrame.CALENDAR);
			openWindow();
		} else if (obj == schedulerTray.schedule) { // ������ �޴� ����
			main.mainFrame.showPage(MainFrame.SCHEDULE);
			openWindow();
		} else if (obj == schedulerTray.board) { // �Խ��� �޴� ����
			main.mainFrame.showPage(MainFrame.NOTICEBOARD);
			openWindow();
		} else if (obj == schedulerTray.member) { // ȸ�� ���� �޴� ����
			main.mainFrame.showPage(MainFrame.MEMBERMODIFIED);
			openWindow();
		} else if (obj == schedulerTray.close) { // â�ݱ�
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
