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

public class MolGridView implements MouseListener, ActionListener {
	private MolGridPanel mgp;
	private ThreeDimView tdv;
	private CommentView cv;
	private JFrame frame;
	private int dim;

	public MolGridView(Database d, ThreeDimView t, CommentView c) {
		dim = d.getMolecule(0).getDimension();

		if (dim < 2)
			return;

		cv = c;
		tdv = t;
		mgp = new MolGridPanel(this, d, new ArrayList<Integer>());

		frame = new JFrame("Molecule Grid View");
		frame.add(mgp);
		frame.setJMenuBar(creatMenuBar());
		frame.pack();
	}

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
		tdv.addMol(m);
		cv.setMolecule(m, false);
	}

	public void show() {
		frame.setVisible(dim >= 2 && mgp.getNumMols() > 0 && mgp.display());
	}

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
