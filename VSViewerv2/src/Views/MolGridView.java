package Views;

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
 * The molecule gridview from for the main program.
 * 
 * @author Kyle Diller
 *
 */
public class MolGridView implements MouseListener, ActionListener {
	private MolGridPanel mgp;
	private AstexView av;
	private CommentView cv;
	private JFrame frame;
	private int dim;

	/**
	 * Creates a molecule gridview form based on a database, with links to a 3D
	 * view and a Comment view.
	 * 
	 * @param d
	 *            The database of molecules.
	 * @param t
	 *            The 3D view.
	 * @param av
	 * @param c
	 *            The Comment view.
	 */
	public MolGridView(Database d, AstexView a, CommentView c) {
		dim = d.getMolecule(0).getDimension();

		if (dim < 2)
			return;

		cv = c;
		av = a;
		mgp = new MolGridPanel(this, d, new ArrayList<Integer>());

		frame = new JFrame("Molecule Grid View");
		frame.add(mgp);
		frame.setJMenuBar(creatMenuBar());
		frame.pack();
	}

	/**
	 * Creates the menu bar for the molecule gridview.
	 * 
	 * @return The menu bar for the molecule gridview.
	 */
	private JMenuBar creatMenuBar() {
		JMenuBar bar = new JMenuBar();

		JMenuItem save = new JMenuItem("Save");
		save.setMnemonic('n');
		save.addActionListener(this);
		save.setActionCommand("FS");

		JMenuItem sort = new JMenuItem("Sort");
		sort.setMnemonic('s');
		sort.addActionListener(this);
		sort.setActionCommand("TS");

		JMenu file = new JMenu("File");
		file.setMnemonic('f');
		file.add(save);

		JMenu tools = new JMenu("Tools");
		tools.setMnemonic('t');
		tools.add(sort);

		bar.add(file);
		bar.add(tools);

		return bar;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int m = mgp.molSelected(e.getX(), e.getY());
		av.addMol(m);
		cv.setMolecule(m, false);
	}

	/**
	 * Shows the molecule grid view form.
	 */
	public void show() {
		frame.setVisible(dim >= 2 && mgp.getNumMols() > 0 && mgp.display());
	}

	/**
	 * Frees all the memory associated with molecule gridview.
	 */
	public void close() {
		frame.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		char c = arg0.getActionCommand().charAt(0);

		switch (c) {
		case 'T':
			mgp.tools(arg0.getActionCommand().charAt(1));
			break;
		case 'F':
			mgp.file(arg0.getActionCommand().charAt(1));
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
