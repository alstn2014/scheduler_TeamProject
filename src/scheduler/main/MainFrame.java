/*
 * �гε� �߰��Ǵ� ���� ������
 * */

// �޷� �ҷ��ö� ���ΰ�ħ �Ǵ°� �߰� �ؾߵ�

package scheduler.main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import scheduler.board.noticeboard.NoticeBoard;
import scheduler.board.write.NoticeBoardWrite;
import scheduler.calendar.MyCalendar;
import scheduler.main.traymessage.MessageThread;
import scheduler.member.manager.MemberManager;
import scheduler.member.manager.MemberModified;
import scheduler.schedule.Schedule;

public class MainFrame extends JFrame {
	public static final int CALENDAR = 0; // Ķ���� ������ ����� �ε��� ��ȣ
	public static final int SCHEDULE = 1; // ������ ������ ����� �ε��� ��ȣ
	public static final int NOTICEBOARD = 2; // �Խ��� ������ ����� �ε��� ��ȣ
	public static final int MEMBERMODIFIED = 3; // ���� ���� ������ ����� �ε��� ��ȣ
	public static final int MEMBERMANAGER = 4; // ȸ�� ���� ������ ����� �ε��� ��ȣ
	public static final int BOARDWRITE = 5; // �Խ��� �۾��� ������ ����� �ε��� ��ȣ

	private final int NONVISIBLEMENUCNT = 1; // �޴��� �߰����� ������ ����ϴ� ������ ����

	Main main;
	Boolean admin = false; // ���� ����
	int user_num;

	JPanel container; // ȭ�� ��ü �� �����̳� �� �г�
	JMenuBar bar;
	private final String[] menuTitle = { "�޷�", "�������", "�Խ���", "��������", "ȸ������" }; // ��µǴ� �޴� �̸�
	private JMenu[] menu = new JMenu[menuTitle.length]; // �ٿ� ������ �޴� ���
	private JPanel[] pages = new JPanel[menuTitle.length + NONVISIBLEMENUCNT]; // ȭ�鿡 ������ ������ ���

	FrameMouseAdapter frameMouseAdapter;

	public MainFrame(Main main) {
		// ���ο� ���� ����ϸ� �޽��� ����ִ� ������

		this.main = main;
		admin = main.userInfo.isAdmin(); // ���� �������� Ȯ��
		setIconImage(main.getIcon()); // ������ ����
		setTitle(main.getTitle() + " - �α��� : " + main.userInfo.getName() + "(" + main.userInfo.getId() + ")"); // Ÿ��Ʋ
																												// ����

		container = new JPanel();
		bar = new JMenuBar();

		for (int i = 0; i < menuTitle.length; i++) { // �ٿ� �޴� �߰�
			if (i == MEMBERMANAGER) {
				if (admin) { // ���� ������ ���쿡�� �� ������(ȸ������)�߰�
					menu[i] = new JMenu(menuTitle[i]);
					bar.add(menu[i]);
				}
			} else {
				menu[i] = new JMenu(menuTitle[i]);
				bar.add(menu[i]);
			}
		}

		// �� �߰�
		setJMenuBar(bar);

		// ������ ����
		createPages();

		// ���� �̺�Ʈ
		addWindowListener(new WindowAdapter() {
			// �ݱ��ư ������ ����
			public void windowClosing(WindowEvent e) {
				main.frameClose();
			}

			// �ּ�ȭ ��ư ������ Ʈ���� â���� ��
			public void windowIconified(WindowEvent e) {
				setVisible(false);
			}

		});

		// ���� ����ϴ� �г� ����
		add(container);

		// ���콺 �̺�Ʈ ���
		frameMouseAdapter = new FrameMouseAdapter(this, menu);
		for (int i = 0; i < menuTitle.length; i++) {
			if (i == MEMBERMANAGER) {
				if (admin) {
					menu[i].addMouseListener(frameMouseAdapter);
				}
			} else {
				menu[i].addMouseListener(frameMouseAdapter);
			}
		}

		// ȭ�� ����
		setSize(810, 600);
		centerWindow(this);
		setVisible(true);
	}

	// pages�迭�� panel �߰��ϴ� �޼���
	private void createPages() {
		pages[CALENDAR] = new MyCalendar(main); // �޷�
		pages[SCHEDULE] = new Schedule(main); // �������
		pages[NOTICEBOARD] = new NoticeBoard(main); // �Խ���
		pages[MEMBERMODIFIED] = new MemberModified(main); // ��������
		pages[MEMBERMANAGER] = new MemberManager(main); // ȸ������
		pages[BOARDWRITE] = new NoticeBoardWrite(main);

		for (int i = 0; i < menuTitle.length + NONVISIBLEMENUCNT; i++) {
			container.add(pages[i]);
		}

	}

	public void showPage(int page) {
		showPage(page, null);
	}

	public void showPage(int page, String[] value) {
		if (page == CALENDAR) { // Ķ���� ������ ���� �ҷ������� ����
			MyCalendar tmpCal = (MyCalendar) getPages(CALENDAR);
			tmpCal.setCal();
		} else if (page == SCHEDULE) {
			Schedule tmpSchedule = (Schedule) getPages(SCHEDULE);
			tmpSchedule.setSchedule();
		} else if (page == BOARDWRITE) {
			NoticeBoardWrite tmpNoticeBoardWrite = (NoticeBoardWrite) getPages(BOARDWRITE);
			if (value[0].equals(NoticeBoardWrite.NEWREGI + "")) {
				tmpNoticeBoardWrite.setValue();
			} else if (value[0].equals(NoticeBoardWrite.BOARDREAD + "")) {
				tmpNoticeBoardWrite.setValue(Integer.parseInt(value[1]));
			}
		} else if (page == NOTICEBOARD) {
			NoticeBoard tmpNoticeBoard = (NoticeBoard) getPages(NOTICEBOARD);
			tmpNoticeBoard.showTable();
		}

		for (int i = 0; i < pages.length; i++) {
			if (pages[i] != null) {
				if (i == page) {
					pages[i].setVisible(true);

				} else {
					pages[i].setVisible(false);
				}
			}
		}
	}

	// ����� �߰��� ������ ���
	public static void centerWindow(Window frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}

	// ������ ������ �޼���
	public JPanel getPages(int index) {
		return pages[index];
	}

}
