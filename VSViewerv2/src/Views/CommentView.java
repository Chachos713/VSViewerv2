package Views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Panels.CommentPanel;
import Util.Database;

public class CommentView implements ActionListener {
	private CommentPanel cp;
	private JFrame frame;

	private JButton comp3d, comp2d;

	public CommentView(Database d) {
		cp = new CommentPanel(d, this);

		comp3d = new JButton("3D");
		comp3d.addActionListener(this);

		comp2d = new JButton("2D");
		comp2d.addActionListener(this);

		JPanel butts = new JPanel();
		butts.add(new JLabel("Compare:"));
		butts.add(comp3d);
		butts.add(comp2d);

		frame = new JFrame("Comment View");
		frame.getContentPane().add(BorderLayout.CENTER, cp);
		frame.getContentPane().add(BorderLayout.SOUTH, butts);
		frame.pack();
	}

	public void setMolecule(int m, boolean last) {
		if (m < 0)
			return;

		cp.changeMol(m);
		frame.setVisible(cp.display() && (!last || frame.isVisible()));
	}

	public void close() {
		frame.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == comp3d)
			cp.compare(true);
		else if (arg0.getSource() == comp2d)
			cp.compare(false);
		else
			cp.actionPerformed(arg0);
	}
}
