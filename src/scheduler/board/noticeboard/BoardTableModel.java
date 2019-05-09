package scheduler.board.noticeboard;

import javax.swing.table.AbstractTableModel;

import scheduler.main.Main;

public class BoardTableModel extends AbstractTableModel {
	String[] columnName = { "�۹�ȣ", "����", "�ۼ���", "�ۼ���", "��ȸ��" };
	String data[][] = new String[1][1];

	public int getColumnCount() {

		return columnName.length;
	}

	public int getRowCount() {

		return data.length;
	}

	public String getColumnName(int col) {

		return columnName[col];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

}
