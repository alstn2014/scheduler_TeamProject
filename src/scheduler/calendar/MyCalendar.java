//�޷�������
package scheduler.calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import scheduler.main.Main;
import scheduler.schedule.Schedule;

public class MyCalendar extends JPanel {
	Main main;
	Schedule schedule;
	PlanInfo infoLabel;
	ArrayList<Cell> cell_list = new ArrayList<Cell>();
	ArrayList<PlanInfo> label_list = new ArrayList<PlanInfo>();
	JPanel p_north, p_center, p_south;
	JButton prev, next;
	JLabel la_month;
	JPanel[] p_dayOfWeek = new JPanel[7];
	JLabel[] la_dayOfWeek = new JLabel[7];

	String[] dayOfWeek = { "��", "��", "ȭ", "��", "��", "��", "��" };

	Calendar cal = Calendar.getInstance();

	int year = cal.get(Calendar.YEAR);
	int month = cal.get(Calendar.MONTH);
	int days = 0;

	public MyCalendar(Main main) {
		this.main = main;
		p_north = new JPanel();// ������ �����޹�ư�ϰ� ����� ����
		p_center = new JPanel();// ���� ����
		p_south = new JPanel();// ���ڰ� ����
		prev = new JButton("���� ��");
		next = new JButton("���� ��");
		la_month = new JLabel();// yyyy.mm��
		la_month.setHorizontalAlignment(JLabel.CENTER);

		p_north.setLayout(new BorderLayout());
		p_north.add(la_month, BorderLayout.CENTER);
		p_north.add(prev, BorderLayout.WEST);
		p_north.add(next, BorderLayout.EAST);
		setLayout(new BorderLayout());
		add(p_north, BorderLayout.NORTH);
		add(p_center);
		add(p_south, BorderLayout.SOUTH);

		p_center.setLayout(new GridLayout(1, 7));
		p_south.setLayout(new GridLayout(6, 7));

		p_north.setPreferredSize(new Dimension(800, 50));
		p_center.setPreferredSize(new Dimension(800, 50));
		p_south.setPreferredSize(new Dimension(800, 420));

		prev.addActionListener((e) -> {
			if (month > 0) {
				month--;
			} else {
				year--;
				month = 11;
			}
			setCal();

		});

		next.addActionListener((e) -> {
			if (month < 11) {
				month++;
			} else {
				year++;
				month = 0;
			}
			setCal();
		});

		initCal();
		setCal();
		// ���� �����ϴ� �޼���
		setWeek();
	}

	public void setWeek() {
		// ���ϼ���
		for (int i = 0; i < p_dayOfWeek.length; i++) {
			p_dayOfWeek[i] = new JPanel();
			la_dayOfWeek[i] = new JLabel("");

			la_dayOfWeek[i].setText(dayOfWeek[i]);
			if (i == 0) {// �Ͽ���
				la_dayOfWeek[i].setForeground(Color.RED);
			}
			if (i == 6) {// �����
				la_dayOfWeek[i].setForeground(Color.BLUE);
			}
			p_dayOfWeek[i].add(la_dayOfWeek[i]);
			p_dayOfWeek[i].setBorder(BorderFactory.createLineBorder(Color.black));
			p_center.add(p_dayOfWeek[i]);
		}
	}

	// �޷� ���� �ϴ� �޼���
	public void initCal() {
		for (int i = 0; i < 42; i++) {
			Cell p_day = new Cell(main);
			cell_list.add(p_day);
			p_south.add(cell_list.get(i));
		}
	}

	// �޷� �� �����ϴ� �޼���
	public void setCal() {
		for (int i = 0; i < cell_list.size(); i++) {
			Cell tmpCell = cell_list.get(i);
			tmpCell.labelDel();

		}
		days = 0; // ���ڸ� ǥ���� ��

		la_month.setText(year + "�� " + (month + 1) + "��"); // ��/�� ���
		la_month.setFont(new Font("Serif", Font.BOLD, 20));
		cal.set(year, month, 1);// �� ������ ���������� �ٲ�

		int startday = cal.get(Calendar.DAY_OF_WEEK); // ����
		int lastday = cal.getActualMaximum(Calendar.DATE); // �� ������ ��¥
		// ��¥�Է�
		for (int i = 0; i < 42; i++) {
			Cell tmp_cell = cell_list.get(i);
			// ��¥ �Է�����
			if (i + 1 >= startday && days < lastday) {
				days++;
				tmp_cell.setCellDate(year, month, days);
				tmp_cell.setMonthDay();

			} else {
				tmp_cell.setCellDate(0, 0, 0);
				tmp_cell.setOtherMonthDay();

			}

			// ��¥�� �� ����
			if ((i % 7) == 6) {
				cell_list.get(i).la_day.setForeground(Color.BLUE);
			} else if ((i % 7) == 0) {
				cell_list.get(i).la_day.setForeground(Color.RED);
			}

		}

	}

}