/*이 클래스는 글쓰기 버튼은 눌렀을시에 사용되는 클래스 입니다*/
package scheduler.board.write;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import scheduler.main.Main;
import scheduler.main.MainFrame;

public class NoticeBoardWrite extends JPanel /* implements ActionListener */ {
	public static final int NEWREGI = 1;
	public static final int BOARDREAD = 2;

	Main main;

	JLabel label;
	JTextArea area;
	JScrollPane scroll;
	JButton bt_register;
	JButton bt_back;// 뒤로가기 버튼
	JTextField field;
	JPanel p_north, p_south;

	String font_name;
	int size;

	int modi_board_no;
	int write_mode;

	public NoticeBoardWrite(Main main) {
		this.main = main;

		label = new JLabel("제목");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		bt_register = new JButton("등록");
		bt_back = new JButton("뒤로가기");
		field = new JTextField(40);
		p_north = new JPanel();
		p_south = new JPanel();

		Font font = new Font("돋움", Font.PLAIN, 20);// 기본값 PLAIN
		area.setFont(font);

		// p_north에 부착시키자
		p_north.add(label, BorderLayout.NORTH);
		label.setFont(new Font("굴림", Font.BOLD, 17));
		p_north.add(field);

		this.setLayout(new BorderLayout());

		// p_south에 버튼 부착하자!!
		p_south.add(bt_register);
		p_south.add(bt_back);

		// bt_register에 액션이벤트 부여!!
		bt_register.addActionListener((e) -> { // 등록 버튼
			switch (write_mode) {
			case NEWREGI:
				registerNoticeBoard();
				break;
			case BOARDREAD:
				modiNoticeBoard();
				break;
			}
			main.mainFrame.showPage(MainFrame.NOTICEBOARD);
		});

		// bt_back에 액션이벤트 부여!!
		bt_back.addActionListener((e) -> { // 게시판으로 돌아가기
			main.mainFrame.showPage(MainFrame.NOTICEBOARD);
		});

		area.setRequestFocusEnabled(true);
		// this 즉, JPanel에 부착시키자!!
		this.add(p_north, BorderLayout.NORTH);
		this.add(p_south, BorderLayout.SOUTH);
		this.add(scroll);
		this.setPreferredSize(new Dimension(780, 540));
		this.setVisible(false);

	}

	// 게시글 등록하는 메서드
	public void registerNoticeBoard() {
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		String title = field.getText();// 제목란에 입력받은 Text를 받아오겠다
		String content = area.getText();// 내용적는란에 입력받은 Text를 받아오겠다
		int writer = main.userInfo.getMember_no();

		String sql = "insert into board(title,content,writer,create_date)";
		sql += " values(?,?,?,now())";

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setInt(3, writer);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt);
			main.mainFrame.showPage(MainFrame.NOTICEBOARD);
		}
	}
	
	// 게시글 수정하는 메서드
	public void modiNoticeBoard() {
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		String title = field.getText();
		String content = area.getText();

		String sql = "update board set title = ?, content = ? where board_no = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setInt(3, modi_board_no);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt);
		}

	}

	public void setValue() {
		field.setText("");
		area.setText("");
		this.write_mode = NEWREGI;

		// 전부 활성화
		bt_register.setEnabled(true);
		area.setEditable(true);
		field.setEditable(true);
		bt_register.setText("신규등록");
	}

	public void setValue(int board_no) {
		this.write_mode = BOARDREAD;
		this.modi_board_no = board_no;
		bt_register.setText("수정");

		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select title, content, writer from board where board_no = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, board_no);
			rs = pstmt.executeQuery();

			rs.next();
			field.setText(rs.getString("title"));
			area.setText(rs.getString("content"));

			if (rs.getInt("writer") != main.userInfo.getMember_no()) { // 등록한 사람이랑 같이 않으면 비활성화
				bt_register.setEnabled(false);
				area.setEditable(false);
				field.setEditable(false);
			} else { // 같으면 활성화
				bt_register.setEnabled(true);
				area.setEditable(true);
				field.setEditable(true);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			main.closeDB(pstmt, rs);
		}

	}

}
