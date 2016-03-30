package FormTest;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import Panels.MolGridPanel;
import Util.Database;

/**
 * A test form for the grid view display of the molecules.
 * 
 * @author Kyle Diller
 *
 */
public class GridViewTest implements MouseListener, ActionListener {
	/**
	 * The main entry point into the code.
	 * 
	 * @param args
	 *            The file to read.
	 */
	public static void main(String[] args) {
		new GridViewTest(args[0]);
	}

	private JFrame frame;
	private MolGridPanel mgv;

	/**
	 * Creates and displays the form for the user.
	 * 
	 * @param file
	 *            the file to display.
	 */
	public GridViewTest(String file) {
		Database d;
		try {
			d = Database.read(file);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		ArrayList<Integer> all = new ArrayList<Integer>();

		for (int i = 0; i < d.getNumMols(); i++) {
			all.add(i);
		}

		JMenuBar b = new JMenuBar();

		JMenuItem sort = new JMenuItem("Sort");
		sort.setMnemonic('s');
		sort.addActionListener(this);
		sort.setActionCommand("TS");

		JMenu tools = new JMenu("Tools");
		tools.setMnemonic('t');
		tools.add(sort);

		b.add(tools);

		frame = new JFrame("Grid View Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setJMenuBar(b);

		mgv = new MolGridPanel(this, d, all);

		frame.add(mgv);
		frame.pack();
		frame.setMinimumSize(new Dimension((300 + 20) + 20, (150 + 20) + 80));
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		mgv.tools('S');
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		int selected = mgv.molSelected(arg0.getX(), arg0.getY());

		System.out.println(selected);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
