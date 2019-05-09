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

public class MemberManager extends JPanel { // �����ڿ� ������
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

	// String item; // �˻� ����Ʈ

	MemberModel memberModel;
	String[] columnName;
	private final String defauleSelectSql = "select member_no,id,name,email,phone,admin from member";

	int row = 0;
	int col = 0;

	public MemberManager(Main main) {
		this.main = main;
		chooser = new JFileChooser();

		checkbox = new JCheckBox();

		// �г�
		p_north = new JPanel();
		p_center = new JPanel();
		p_south = new JPanel();

		p_north.setPreferredSize(new Dimension(750, 50));
		p_center.setPreferredSize(new Dimension(750, 450));
		p_south.setPreferredSize(new Dimension(800, 100));

		// ����
		table = new JTable();
		memberModel = new MemberModel(main);

		scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(750, 400));
		// ���� ��ư
		bt_del = new JButton("ȸ������");
		bt_reset = new JButton("��й�ȣ �ʱ�ȭ");
		bt_excel = new JButton("���� �ٿ�");

		// ����
		la_search = new JLabel("�� ��");
		la_search.setFont(new Font("����", Font.BOLD, 18));

		choice = new Choice(); // ����Ʈ
		choice.setFont(new Font("����", 0, 15));
		choice.add("���̵�");
		choice.add("�̸�");
		choice.add("�̸���");
		choice.add("��ȭ��ȣ");

		t_search = new JTextField(25);// �˻� �ؽ�Ʈ
		bt_search = new JButton("�� ��");

		// ���� ����
		p_north.add(bt_del);
		p_north.add(bt_reset);
		p_north.add(bt_excel);

		// ���� ����
		p_center.add(scroll); // ���̺�

		// ���� ����
		p_south.add(la_search);
		p_south.add(choice);
		p_south.add(t_search);
		p_south.add(bt_search);

		// ���� ��ư ����
		p_north.setLayout(new FlowLayout(FlowLayout.TRAILING));

		this.setLayout(new BorderLayout());
		add(p_north, BorderLayout.NORTH);
		add(p_center, BorderLayout.CENTER);
		add(p_south, BorderLayout.SOUTH);

		// ȣ��
		getList();
		table.setModel(memberModel);

		// ����
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

	// ���̺� ������ ����
	private int setModelData(String sql, String[] setValue) {
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Object[][] tmpData = null;
		int total = 0;

		try {
			pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if (setValue != null) {// ������ ���� ������ ���� ����
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

	// ���̺�
	public void getList() {
		setModelData(defauleSelectSql, null);
	}

	// ����
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

		JOptionPane.showMessageDialog(this, "ȸ������ �Ϸ�");
		getList();
	}

	// �ʱ�ȭ
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

		JOptionPane.showMessageDialog(this, "�ʱ�ȭ ��й�ȣ�� 0000�Դϴ�");
	}

	// ��������
	public void excel() {

		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		HSSFWorkbook workbook;// ���� poi.jar
		HSSFSheet sheet;

		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet("ȸ������Ʈ");
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
			System.out.println("���� ����Ϸ�");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("���� �������");
		}
	}

	// �˻�
	public void search() {
		String getItem = choice.getSelectedItem();
		String sql = null;
		String[] setValue = { "%" + t_search.getText() + "%" };
		if (getItem.equals("���̵�")) {
			sql = defauleSelectSql + " where id like ?";
		} else if (getItem.equals("�̸�")) {
			sql = defauleSelectSql + " where name like ?";
		} else if (getItem.equals("�̸���")) {
			sql = defauleSelectSql + " where email like ?";
		} else {
			sql = defauleSelectSql + " where phone like ?";
		}
		int result = setModelData(sql, setValue);
		if (result == 0) {
			JOptionPane.showMessageDialog(main.mainFrame, "�˻� ����� �����ϴ�.");
			getList();
		}

	}

	// �迭���� üũ�ڽ� �Ȱ� �������� �޼���
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
