package scheduler.member.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

import scheduler.main.Main;

public class MemberModel extends AbstractTableModel {
	Main main;
	String[] columnName = { "����", "���̵�", "�̸�", "�̸���", "��ȭ��ȣ", "����" };
	Object[][] data = new Object[1][columnName.length];

	public MemberModel(Main main) {
		this.main = main;
	}

	public int getRowCount() {
		return data.length;
	}

	public int getColumnCount() {
		return columnName.length;
	}

	public String getColumnName(int col) {
		return columnName[col];
	}

	public boolean isCellEditable(int row, int col) {
		switch (col) {
		case 1:
		case 2:
			return false;
		default:
			return true;
		}
	}

	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;

		// �̸���,��ȭ��ȣ �ٲ𶧸� ����
		if (col == 3) { // �̸��� ����
			String email = (String) data[row][3];
			String sql = "update member set email=? where id=?";
			dbUpdate(sql, email, row);
		} else if (col == 4) { // ��ȭ��ȣ ����
			String phone = (String) data[row][4];
			String sql = "update member set phone=? where id = ?";
			dbUpdate(sql, phone, row);
		} else if (col == 5) { // admin �ٲ�븸 ����
			String check = "N";
			if ((Boolean) data[row][5]) {// �����ϰ�� Y�� ���ۤ�
				check = "Y";
			}

			String sql = "update member set admin=? where id=?";
			dbUpdate(sql, check, row);
		}
	}

	public void dbUpdate(String sql, String value, int row) {
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		String id = (String) data[row][1];

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, value);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt);
		}

	}

	public Class getColumnClass(int col) {
		switch (col) {
		case 0:
		case 5:
			return Boolean.class;
		default:
			return String.class;
		}
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}
}
