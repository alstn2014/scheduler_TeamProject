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
	JScrollPane scroll;// 테이블을 붙이기 위한 스크롤
	JScrollPane scroll2;// 에어리어를 붙일 스크롤
	BoardTableModel boardTableModel;

	JPanel p_north, p_south;
	JPanel p_s_north, p_s_south, p_s_south2;// 이 패널들은 버튼과 검색란!!
	JTextField t_input;// 검색할 수 있는 공간
	String[] ch2_list = { "제목", "작성자", "아이디" };
	Choice ch2 = new Choice();

	// 기본 검색 쿼리문
	private final String defaultSql = "SELECT board_no, title, name, id, create_date, hits from member m, board b WHERE m.member_no = b.writer ";

	public NoticeBoard(Main main) {
		this.main = main;

		bt_search = new JButton("검색");
		bt_write = new JButton("글쓰기");
		// bt_edit=new JButton("게시글관리");

		boardTableModel = new BoardTableModel();
		table = new JTable();
		scroll = new JScrollPane(table);
		p_north = new JPanel();
		p_south = new JPanel();
		p_south.setLayout(new BorderLayout());
		// south와 center로 나눈 패널안에서 south패널을 한번더 둘로 나누었다(p_s_north,p_s_south)
		t_input = new JTextField(35);
		t_input.setPreferredSize(new Dimension(120, 30));
		p_s_north = new JPanel();
		p_s_south = new JPanel();
		p_s_south2 = new JPanel();

		this.add(p_north);// 전체 JPanel에 p_center패널 부착
		p_north.setBackground(new Color(255, 255, 255));
		p_south.add(p_s_north, BorderLayout.NORTH);// p_south패널에 p_s_north패널을 p_south기준 밑에 부착

		for (int i = 0; i < ch2_list.length; i++) {// 얘도 마찬가지
			ch2.add(ch2_list[i]);
		}
		p_north.add(scroll);

		// p_south패널기준 아래쪽에 부착된p_s_south패널에 초이스,Field,searchButton부착
		p_s_south.add(ch2);
		p_s_south.add(t_input);
		p_s_south.add(bt_search);
		p_s_south.add(bt_write);
		p_south.add(p_s_south, BorderLayout.NORTH);

		// p_s_south 꺼!!
		bt_search.setBackground(new Color(000, 204, 000));
		bt_search.setPreferredSize(new Dimension(100, 30));
		bt_write.setBackground(new Color(255, 255, 255));
		bt_write.setFont(new Font("굴림", Font.PLAIN, 15));
		bt_write.setPreferredSize(new Dimension(100, 30));
		ch2.setPreferredSize(new Dimension(125, 40));
		// 스크롤 사이즈결정!!
		scroll.setPreferredSize(new Dimension(750, 440));
		// table 사이즈결정!!
		table.setPreferredSize(new Dimension(700, 447));
		// 부모Panel의 사이즈결정!!
		this.setPreferredSize(new Dimension(800, 600));
		// 자식Panel의 사이즈결정!!
		p_south.setPreferredSize(new Dimension(800, 150));
		p_north.setPreferredSize(new Dimension(800, 450));

		// 최종적으로 this 즉, JPanel에 전부 add하자!!
		this.add(p_north, BorderLayout.NORTH);
		this.add(p_south, BorderLayout.SOUTH);

		// 테이블과 마우스리스너 연결!!
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (table.getSelectedRow() != -1) {
					int board_no = Integer.parseInt(boardTableModel.data[table.getSelectedRow()][0]);
					addHitsCount(board_no);// 조회수 증가
					String[] tmpString = { NoticeBoardWrite.BOARDREAD + "", board_no + "" };
					main.mainFrame.showPage(MainFrame.BOARDWRITE, tmpString);
				}
			}
		});

		// 버튼과 리스너 연결(글쓰기 버튼)
		bt_write.addActionListener((e) -> {
			String[] tmpString = { NoticeBoardWrite.NEWREGI + "" };
			main.mainFrame.showPage(MainFrame.BOARDWRITE, tmpString);
		});

		// "검색"버튼에 이벤트 주자!!
		bt_search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				search();

			}
		});

		showTable();
		table.setModel(boardTableModel);

		// 셀의 너비를 조정하고, 글자 정렬!!
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

		// 가운데로 조정
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

	// 시작과 동시에 테이블을 띄우자!!(게시판목록)
	public void showTable() {
		setTableData(defaultSql, null);
	}

	// 검색기능!!
	public void search() {
		String sql = null;
		String[] findWord = { "%" + t_input.getText() + "%" };
		String findCond = ch2.getSelectedItem();
		if (findCond.equals("제목")) { // 제목검색
			sql = defaultSql + "and title like ?";
		} else if (findCond.equals("작성자")) { // 작성자 검색
			sql = defaultSql + "and name like ?";
		} else if (findCond.equals("아이디")) { // 작성자 검색
			sql = defaultSql + "and id like ?";
		}

		int result = setTableData(sql, findWord);
		if (result == 0) {// 검색 결과 없으면 전체 표시
			JOptionPane.showMessageDialog(main.mainFrame, "검색 결과가 없습니다.");
			showTable();
		}
	}

	// 조회수 증가 메서드
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
