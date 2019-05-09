package scheduler.main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenu;

class FrameMouseAdapter extends MouseAdapter {
	MainFrame mainFrame;
	JMenu[] menu;

	public FrameMouseAdapter(MainFrame mainFrame, JMenu[] menu) {
		this.mainFrame = mainFrame;
		this.menu = menu;
	}

	public void mouseClicked(MouseEvent e) {
		Object obj = e.getSource(); // ���õ� �̺�Ʈ �޾ƿ�

		if (obj == menu[0]) {// �޷�
			mainFrame.showPage(MainFrame.CALENDAR);
		} else if (obj == menu[1]) { // �������
			mainFrame.showPage(MainFrame.SCHEDULE);
		} else if (obj == menu[2]) { // �Խ���
			mainFrame.showPage(MainFrame.NOTICEBOARD);
		} else if (obj == menu[3]) { // ��������
			mainFrame.showPage(MainFrame.MEMBERMODIFIED);
		} else if (obj == menu[4] && mainFrame.admin) { // ȸ������
			mainFrame.showPage(MainFrame.MEMBERMANAGER);
		}
	}

}
