package scheduler.member.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

import scheduler.main.Main;

public class MemberModel extends AbstractTableModel {
	Main main;
	String[] columnName = { "선택", "아이디", "이름", "이메일", "전화번호", "어드민" };
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

		// 이메일,전화번호 바뀔때만 동작
		if (col == 3) { // 이메일 변경
			String email = (String) data[row][3];
			String sql = "update member set email=? where id=?";
			dbUpdate(sql, email, row);
		} else if (col == 4) { // 전화번호 변경
			String phone = (String) data[row][4];
			String sql = "update member set phone=? where id = ?";
			dbUpdate(sql, phone, row);
		} else if (col == 5) { // admin 바뀔대만 동작
			String check = "N";
			if ((Boolean) data[row][5]) {// 어드민일경우 Y로 동작ㄹ
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
