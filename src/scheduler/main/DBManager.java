/*
 * DB���� ó���ϴ� Ŭ����
 * */
package scheduler.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

final class DBManager {
	private Connection con = null;

	final private String driver = "com.mysql.jdbc.Driver";
	final private String url = "jdbc:mysql://younggu1545.cafe24.com:3306/younggu1545?zeroDateTimeBehavior=convertToNull";
	final private String user = "younggu1545";
	final private String password = "Itbank510!@";

	public DBManager() {
		connect();
	}

	public Connection getCon() {
		if (con == null) {
			connect();
			if (con == null) {// db���� ���ϸ� ��õ�
				getCon();
			}
		}

		return con;
	}

	private void connect() {
		if (con == null) {
			try {
				Class.forName(driver);
				this.con = DriverManager.getConnection(url, user, password);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (this.con == null) {
				System.out.println("DB ���� ����");
			} else {
				System.out.println("DB ���� ����");
			}
		}

	}

	void closeDB() {
		if (this.con != null) {
			try {
				this.con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	void closeDB(PreparedStatement pstmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	void closeDB(PreparedStatement pstmt) {

		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
