package Views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import Panels.ThreeDimPanel;
import Util.Database;

/**
 * The 3D form for the main program.
 * 
 * @author Kyle Diller
 *
 */
public class ThreeDimView implements ActionListener {
	private ThreeDimPanel tdp;
	private JFrame frame;
	private int dim;

	/**
	 * Creates a 3D view form based on the database.
	 * 
	 * @param d
	 *            The database of molecules.
	 */
	public ThreeDimView(Database d) {
		dim = d.getMolecule(0).getDimension();

		if (dim < 3)
			return;

		tdp = new ThreeDimPanel(d, this, false, true);

		frame = new JFrame("3D View");
		frame.setJMenuBar(createMenu());
		frame.add(tdp);
		frame.pack();
	}

	/**
	 * Displays the form.
	 * 
	 * @param last
	 *            if the form being already displayed matters. Uses 'implies' to
	 *            do the operation. Mainly used for when the form is set not to
	 *            display, and then set to display.
	 */
	public void show(boolean last) {
		frame.setVisible(tdp.display() && (!last || frame.isVisible()));
	}

	/**
	 * Creates the menu bar of the 3D form.
	 * 
	 * @return The menu bar for the 3D form.
	 */
	public JMenuBar createMenu() {
		JMenuBar menu = new JMenuBar();

		JMenu file = new JMenu("File");
		file.setMnemonic('f');

		JMenuItem save = new JMenuItem("Save");
		save.setActionCommand("S");
		save.setMnemonic('s');
		save.addActionListener(this);

		JMenuItem open = new JMenuItem("Open");
		open.setActionCommand("O");
		open.setMnemonic('o');
		open.addActionListener(this);

		file.add(open);
		file.add(save);

		menu.add(file);

		return menu;
	}

	/**
	 * Set a molecule to be displayed in the 3D view.
	 * 
	 * @param m
	 *            The index of the molecule to add.
	 */
	public void addMol(int m) {
		if (m < 0)
			return;

		tdp.addMolecule(m);

		if (dim >= 3)
			show(false);
	}

	/**
	 * Clean up the memory used with the form the application is closed.
	 */
	public void close() {
		frame.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		tdp.actionPerformed(e);
	}
}
