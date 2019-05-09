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
		Object obj = e.getSource(); // 선택된 이벤트 받아옴

		if (obj == menu[0]) {// 달력
			mainFrame.showPage(MainFrame.CALENDAR);
		} else if (obj == menu[1]) { // 일정등록
			mainFrame.showPage(MainFrame.SCHEDULE);
		} else if (obj == menu[2]) { // 게시판
			mainFrame.showPage(MainFrame.NOTICEBOARD);
		} else if (obj == menu[3]) { // 정보수정
			mainFrame.showPage(MainFrame.MEMBERMODIFIED);
		} else if (obj == menu[4] && mainFrame.admin) { // 회원관리
			mainFrame.showPage(MainFrame.MEMBERMANAGER);
		}
	}

}
