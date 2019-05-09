//스케쥴이 너무많아져서 다못담은것을 담아줄곳
package scheduler.calendar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class PlanCount extends JFrame {
	JScrollPane scroll;
	JPanel panel;
	ArrayList<PlanInfo> inData;

	public PlanCount() {
		panel = new JPanel();
		scroll = new JScrollPane(panel);
		inData = new ArrayList<PlanInfo>();
		add(scroll);

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setVisible(false);
			}
		});

		setLocation(800, 400);
		setSize(200, 200);
		setVisible(false);

	}

	public void addData(PlanInfo addPlanInfo) {
		panel.add(addPlanInfo);
		inData.add(addPlanInfo);
	}

	public void removeData() {
		while (inData.size() > 0) {
			panel.remove(inData.get(0));
			inData.remove(0);
		}
	}
}
