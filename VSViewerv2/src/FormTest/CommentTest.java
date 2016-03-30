package FormTest;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Panels.CommentPanel;
import Util.Database;

/**
 * A simple test form for the comment view.
 * 
 * @author Kyle Diller
 *
 */
public class CommentTest implements ListSelectionListener, WindowListener,
		ActionListener {
	/**
	 * Main entry point for the test.
	 * 
	 * @param args
	 *            the file to read.
	 */
	public static void main(String[] args) {
		new CommentTest(args[0]);
	}

	private Database d;
	private JFrame frame;
	private JList<String> molNames;
	private CommentPanel cp;

	/**
	 * Creates and displays the comment viewer.
	 * 
	 * @param file
	 *            the file to read and display.
	 */
	public CommentTest(String file) {
		try {
			d = Database.read(file);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		cp = new CommentPanel(d, this);

		frame = new JFrame("Comment Test");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(this);

		DefaultListModel<String> model = new DefaultListModel<String>();
		molNames = new JList<String>(model);
		molNames.addListSelectionListener(this);

		for (int i = 0; i < d.getNumMols(); i++) {
			model.add(i, d.getMolecule(i).getName());
		}

		JScrollPane namePane = new JScrollPane(molNames);
		namePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		namePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		namePane.setPreferredSize(new Dimension(160, 160));

		frame.getContentPane().add(BorderLayout.WEST, namePane);
		frame.getContentPane().add(BorderLayout.CENTER, cp);
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		cp.changeMol(molNames.getSelectedIndex());
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		if (d.close())
			frame.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		cp.actionPerformed(e);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}

}
