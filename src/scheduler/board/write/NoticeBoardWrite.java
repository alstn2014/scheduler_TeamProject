/*�� Ŭ������ �۾��� ��ư�� �������ÿ� ���Ǵ� Ŭ���� �Դϴ�*/
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
	JButton bt_back;// �ڷΰ��� ��ư
	JTextField field;
	JPanel p_north, p_south;

	String font_name;
	int size;

	int modi_board_no;
	int write_mode;

	public NoticeBoardWrite(Main main) {
		this.main = main;

		label = new JLabel("����");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		bt_register = new JButton("���");
		bt_back = new JButton("�ڷΰ���");
		field = new JTextField(40);
		p_north = new JPanel();
		p_south = new JPanel();

		Font font = new Font("����", Font.PLAIN, 20);// �⺻�� PLAIN
		area.setFont(font);

		// p_north�� ������Ű��
		p_north.add(label, BorderLayout.NORTH);
		label.setFont(new Font("����", Font.BOLD, 17));
		p_north.add(field);

		this.setLayout(new BorderLayout());

		// p_south�� ��ư ��������!!
		p_south.add(bt_register);
		p_south.add(bt_back);

		// bt_register�� �׼��̺�Ʈ �ο�!!
		bt_register.addActionListener((e) -> { // ��� ��ư
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

		// bt_back�� �׼��̺�Ʈ �ο�!!
		bt_back.addActionListener((e) -> { // �Խ������� ���ư���
			main.mainFrame.showPage(MainFrame.NOTICEBOARD);
		});

		area.setRequestFocusEnabled(true);
		// this ��, JPanel�� ������Ű��!!
		this.add(p_north, BorderLayout.NORTH);
		this.add(p_south, BorderLayout.SOUTH);
		this.add(scroll);
		this.setPreferredSize(new Dimension(780, 540));
		this.setVisible(false);

	}

	// �Խñ� ����ϴ� �޼���
	public void registerNoticeBoard() {
		Connection con = main.getCon();
		PreparedStatement pstmt = null;
		String title = field.getText();// ������� �Է¹��� Text�� �޾ƿ��ڴ�
		String content = area.getText();// �������¶��� �Է¹��� Text�� �޾ƿ��ڴ�
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
	
	// �Խñ� �����ϴ� �޼���
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

		// ���� Ȱ��ȭ
		bt_register.setEnabled(true);
		area.setEditable(true);
		field.setEditable(true);
		bt_register.setText("�űԵ��");
	}

	public void setValue(int board_no) {
		this.write_mode = BOARDREAD;
		this.modi_board_no = board_no;
		bt_register.setText("����");

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

			if (rs.getInt("writer") != main.userInfo.getMember_no()) { // ����� ����̶� ���� ������ ��Ȱ��ȭ
				bt_register.setEnabled(false);
				area.setEditable(false);
				field.setEditable(false);
			} else { // ������ Ȱ��ȭ
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
