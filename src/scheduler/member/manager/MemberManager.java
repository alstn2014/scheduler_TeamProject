package scheduler.member.manager;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import scheduler.main.Main;
import scheduler.member.SHA;

public class MemberManager extends JPanel { // 관리자용 페이지
	Main main;

	JTable table;
	JButton bt_del;
	JButton bt_reset;
	JButton bt_excel;
	JButton bt_search;
	JLabel la_search;
	JTextField t_search;
	JPanel p_north;
	JPanel p_south;
	JPanel p_east;
	JPanel p_center;
	JScrollPane scroll;
	Choice choice;
	JFileChooser chooser;

	JCheckBox checkbox;

	String member_id;

	// String item; // 검색 셀렉트

	MemberModel memberModel;
	String[] columnName;
	private final String defauleSelectSql = "select member_no,id,name,email,phone,admin from member";

	int row = 0;
	int col = 0;

	public MemberManager(Main main) {
		this.main = main;
		chooser = new JFileChooser();

		checkbox = new JCheckBox();

		// 패널
		p_north = new JPanel();
		p_center = new JPanel();
		p_south = new JPanel();

		p_north.setPreferredSize(new Dimension(750, 50));
		p_center.setPreferredSize(new Dimension(750, 450));
		p_south.setPreferredSize(new Dimension(800, 100));

		// 센터
		table = new JTable();
		memberModel = new MemberModel(main);

		scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(750, 400));
		// 북쪽 버튼
		bt_del = new JButton("회원삭제");
		bt_reset = new JButton("비밀번호 초기화");
		bt_excel = new JButton("엑셀 다운");

		// 남쪽
		la_search = new JLabel("검 색");
		la_search.setFont(new Font("돋움", Font.BOLD, 18));

		choice = new Choice(); // 셀렉트
		choice.setFont(new Font("바탕", 0, 15));
		choice.add("아이디");
		choice.add("이름");
		choice.add("이메일");
		choice.add("전화번호");

		t_search = new JTextField(25);// 검색 텍스트
		bt_search = new JButton("검 색");

		// 북쪽 부착
		p_north.add(bt_del);
		p_north.add(bt_reset);
		p_north.add(bt_excel);

		// 센터 부착
		p_center.add(scroll); // 테이블

		// 남쪽 부착
		p_south.add(la_search);
		p_south.add(choice);
		p_south.add(t_search);
		p_south.add(bt_search);

		// 북쪽 버튼 정렬
		p_north.setLayout(new FlowLayout(FlowLayout.TRAILING));

		this.setLayout(new BorderLayout());
		add(p_north, BorderLayout.NORTH);
		add(p_center, BorderLayout.CENTER);
		add(p_south, BorderLayout.SOUTH);

		// 호출
		getList();
		table.setModel(memberModel);

		// 수정
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				row = table.getSelectedRow();
				col = table.getSelectedColumn();
				member_id = "";
				member_id = (String) table.getValueAt(row, 1);

			}
		});

		bt_del.addActionListener((e) -> {
			del();
		});

		bt_reset.addActionListener((e) -> {
			reset();
		});

		bt_excel.addActionListener((e) -> {
			excel();
		});

		bt_search.addActionListener((e) -> {
			search();
		});

		t_search.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					search();
				}
			}
		});

		setVisible(true);
		setSize(800, 600);
	}

	// 테이블 데이터 설정
	private int setModelData(String sql, String[] setValue) {
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Object[][] tmpData = null;
		int total = 0;

		try {
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if (setValue != null) {// 설정할 값이 없으면 동작 안함
				for (int i = 0; i < setValue.length; i++) {
					pstmt.setString(i + 1, setValue[i]);
				}
			}
			rs = pstmt.executeQuery();

			rs.last();
			total = rs.getRow();
			int colLength = memberModel.columnName.length;
			tmpData = new Object[total][colLength];

			rs.beforeFirst();
			for (int i = 0; i < total; i++) {
				rs.next();
				for (int a = 0; a < colLength; a++) {
					if (a == 0) {
						tmpData[i][a] = false;
					} else if (a == 5) {
						tmpData[i][a] = rs.getBoolean("admin");
					} else {
						tmpData[i][a] = rs.getString(a + 1);
					}
				}
			}
			memberModel.data = tmpData;
			table.updateUI();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt, rs);
		}

		return total;

	}

	// 테이블
	public void getList() {
		setModelData(defauleSelectSql, null);
	}

	// 삭제
	public void del() {
		Connection con = main.getCon();
		ArrayList<String> list = getChecked(memberModel.data);

		for (int i = 0; i < list.size(); i++) {
			String sql = "delete from member where id=?";
			PreparedStatement pstmt = null;
			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, list.get(i));
				pstmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				main.closeDB(pstmt);
			}
		}

		JOptionPane.showMessageDialog(this, "회원삭제 완료");
		getList();
	}

	// 초기화
	public void reset() {
		Connection con = main.getCon();
		ArrayList<String> list = getChecked(memberModel.data);
		String pass = null;
		try {
			pass = SHA.encrypt("0000");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		for (int i = 0; i < list.size(); i++) {
			String sql = "update member set password=? where id=?";
			PreparedStatement pstmt = null;
			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, pass);
				pstmt.setString(2, list.get(i));
				pstmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				main.closeDB(pstmt);
			}
		}

		JOptionPane.showMessageDialog(this, "초기화 비밀번호는 0000입니다");
	}

	// 엑셀저장
	public void excel() {

		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		HSSFWorkbook workbook;// 엑셀 poi.jar
		HSSFSheet sheet;

		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet("회원리스트");
		String sql = "select * from member";

		try {
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();

			ResultSetMetaData meta = rs.getMetaData();

			int columnCount = meta.getColumnCount();
			HSSFRow row = sheet.createRow(0);

			for (int i = 1; i <= columnCount; i++) {
				HSSFCell cell = row.createCell(i - 1);
				cell.setCellValue(meta.getColumnName(i));
			}

			rs.last();
			int total = rs.getRow();
			rs.beforeFirst();
			for (int i = 1; i <= total; i++) {
				HSSFRow r = sheet.createRow(i);
				rs.next();
				for (int a = 0; a < columnCount; a++) {
					HSSFCell cell = r.createCell(a);
					cell.setCellValue(rs.getString(a + 1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt, rs);
		}

		try {
			if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();

				if (file == null) {
					return;
				}

				file = new File(file.getParentFile(), file.getName() + ".xls");

				FileOutputStream fileOut = new FileOutputStream(file);
				workbook.write(fileOut);
				fileOut.close();
			}
			System.out.println("엑셀 저장완료");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("엑셀 저장실패");
		}
	}

	// 검색
	public void search() {
		String getItem = choice.getSelectedItem();
		String sql = null;
		String[] setValue = { "%" + t_search.getText() + "%" };
		if (getItem.equals("아이디")) {
			sql = defauleSelectSql + " where id like ?";
		} else if (getItem.equals("이름")) {
			sql = defauleSelectSql + " where name like ?";
		} else if (getItem.equals("이메일")) {
			sql = defauleSelectSql + " where email like ?";
		} else {
			sql = defauleSelectSql + " where phone like ?";
		}
		int result = setModelData(sql, setValue);
		if (result == 0) {
			JOptionPane.showMessageDialog(main.mainFrame, "검색 결과가 없습니다.");
			getList();
		}

	}

	// 배열에서 체크박스 된거 가져오는 메서드
	private ArrayList<String> getChecked(Object data[][]) {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < data.length; i++) {
			if ((boolean) data[i][0]) {
				list.add((String) data[i][1]);
			}
		}
		return list;
	}
}
