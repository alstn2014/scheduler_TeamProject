package scheduler.board.noticeboard;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;

import scheduler.board.write.NoticeBoardWrite;
import scheduler.main.Main;
import scheduler.main.MainFrame;

public class NoticeBoard extends JPanel {
	Main main;
	JButton bt_search, bt_write;
	JTable table;
	JScrollPane scroll;// ���̺��� ���̱� ���� ��ũ��
	JScrollPane scroll2;// ���� ���� ��ũ��
	BoardTableModel boardTableModel;

	JPanel p_north, p_south;
	JPanel p_s_north, p_s_south, p_s_south2;// �� �гε��� ��ư�� �˻���!!
	JTextField t_input;// �˻��� �� �ִ� ����
	String[] ch2_list = { "����", "�ۼ���", "���̵�" };
	Choice ch2 = new Choice();

	// �⺻ �˻� ������
	private final String defaultSql = "SELECT board_no, title, name, id, create_date, hits from member m, board b WHERE m.member_no = b.writer ";

	public NoticeBoard(Main main) {
		this.main = main;

		bt_search = new JButton("�˻�");
		bt_write = new JButton("�۾���");
		// bt_edit=new JButton("�Խñ۰���");

		boardTableModel = new BoardTableModel();
		table = new JTable();
		scroll = new JScrollPane(table);
		p_north = new JPanel();
		p_south = new JPanel();
		p_south.setLayout(new BorderLayout());
		// south�� center�� ���� �гξȿ��� south�г��� �ѹ��� �ѷ� ��������(p_s_north,p_s_south)
		t_input = new JTextField(35);
		t_input.setPreferredSize(new Dimension(120, 30));
		p_s_north = new JPanel();
		p_s_south = new JPanel();
		p_s_south2 = new JPanel();

		this.add(p_north);// ��ü JPanel�� p_center�г� ����
		p_north.setBackground(new Color(255, 255, 255));
		p_south.add(p_s_north, BorderLayout.NORTH);// p_south�гο� p_s_north�г��� p_south���� �ؿ� ����

		for (int i = 0; i < ch2_list.length; i++) {// �굵 ��������
			ch2.add(ch2_list[i]);
		}
		p_north.add(scroll);

		// p_south�гα��� �Ʒ��ʿ� ������p_s_south�гο� ���̽�,Field,searchButton����
		p_s_south.add(ch2);
		p_s_south.add(t_input);
		p_s_south.add(bt_search);
		p_s_south.add(bt_write);
		p_south.add(p_s_south, BorderLayout.NORTH);

		// p_s_south ��!!
		bt_search.setBackground(new Color(000, 204, 000));
		bt_search.setPreferredSize(new Dimension(100, 30));
		bt_write.setBackground(new Color(255, 255, 255));
		bt_write.setFont(new Font("����", Font.PLAIN, 15));
		bt_write.setPreferredSize(new Dimension(100, 30));
		ch2.setPreferredSize(new Dimension(125, 40));
		// ��ũ�� ���������!!
		scroll.setPreferredSize(new Dimension(750, 440));
		// table ���������!!
		table.setPreferredSize(new Dimension(700, 447));
		// �θ�Panel�� ���������!!
		this.setPreferredSize(new Dimension(800, 600));
		// �ڽ�Panel�� ���������!!
		p_south.setPreferredSize(new Dimension(800, 150));
		p_north.setPreferredSize(new Dimension(800, 450));

		// ���������� this ��, JPanel�� ���� add����!!
		this.add(p_north, BorderLayout.NORTH);
		this.add(p_south, BorderLayout.SOUTH);

		// ���̺�� ���콺������ ����!!
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (table.getSelectedRow() != -1) {
					int board_no = Integer.parseInt(boardTableModel.data[table.getSelectedRow()][0]);
					addHitsCount(board_no);// ��ȸ�� ����
					String[] tmpString = { NoticeBoardWrite.BOARDREAD + "", board_no + "" };
					main.mainFrame.showPage(MainFrame.BOARDWRITE, tmpString);
				}
			}
		});

		// ��ư�� ������ ����(�۾��� ��ư)
		bt_write.addActionListener((e) -> {
			String[] tmpString = { NoticeBoardWrite.NEWREGI + "" };
			main.mainFrame.showPage(MainFrame.BOARDWRITE, tmpString);
		});

		// "�˻�"��ư�� �̺�Ʈ ����!!
		bt_search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				search();

			}
		});

		showTable();
		table.setModel(boardTableModel);

		// ���� �ʺ� �����ϰ�, ���� ����!!
		DefaultTableCellRenderer cellCenter = new DefaultTableCellRenderer();
		cellCenter.setHorizontalAlignment(JLabel.CENTER);
		DefaultTableCellRenderer cellRight = new DefaultTableCellRenderer();
		cellRight.setHorizontalAlignment(JLabel.RIGHT);

		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(110);
		table.getColumnModel().getColumn(2).setPreferredWidth(110);
		table.getColumnModel().getColumn(3).setPreferredWidth(130);
		table.getColumnModel().getColumn(4).setPreferredWidth(40);
		table.setRowHeight(35);

		// ����� ����
		table.getColumnModel().getColumn(0).setCellRenderer(cellCenter);
		table.getColumnModel().getColumn(1).setCellRenderer(cellCenter);
		table.getColumnModel().getColumn(2).setCellRenderer(cellCenter);
		table.getColumnModel().getColumn(3).setCellRenderer(cellCenter);
		table.getColumnModel().getColumn(4).setCellRenderer(cellCenter);

		table.updateUI();

	}

	public int setTableData(String sql, String[] value) {
		int total = 0;
		int col = boardTableModel.columnName.length;
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String[][] tmpData = null;
		String addOrder = sql + " order by board_no desc";

		try {
			pstmt = con.prepareStatement(addOrder, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if (value != null) {
				for (int i = 0; i < value.length; i++) {
					pstmt.setString(i + 1, value[i]);
				}
			}
			rs = pstmt.executeQuery();

			rs.last();
			total = rs.getRow();
			rs.beforeFirst();
			tmpData = new String[total][col];
			for (int i = 0; i < total; i++) {
				rs.next();
				tmpData[i][0] = rs.getString("board_no");
				tmpData[i][1] = rs.getString("title");
				tmpData[i][2] = rs.getString("name") + "(" + rs.getString("id") + ")";
				tmpData[i][3] = rs.getString("create_date");
				tmpData[i][4] = rs.getString("hits");
			}

			boardTableModel.data = tmpData;
			table.updateUI();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt, rs);
		}

		return total;
	}

	// ���۰� ���ÿ� ���̺��� �����!!(�Խ��Ǹ��)
	public void showTable() {
		setTableData(defaultSql, null);
	}

	// �˻����!!
	public void search() {
		String sql = null;
		String[] findWord = { "%" + t_input.getText() + "%" };
		String findCond = ch2.getSelectedItem();
		if (findCond.equals("����")) { // ����˻�
			sql = defaultSql + "and title like ?";
		} else if (findCond.equals("�ۼ���")) { // �ۼ��� �˻�
			sql = defaultSql + "and name like ?";
		} else if (findCond.equals("���̵�")) { // �ۼ��� �˻�
			sql = defaultSql + "and id like ?";
		}

		int result = setTableData(sql, findWord);
		if (result == 0) {// �˻� ��� ������ ��ü ǥ��
			JOptionPane.showMessageDialog(main.mainFrame, "�˻� ����� �����ϴ�.");
			showTable();
		}
	}

	// ��ȸ�� ���� �޼���
	public void addHitsCount(int board_no) {
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		String sql = "UPDATE board set hits = hits+1 where board_no= ?";

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, board_no);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt);
		}

	}

}
