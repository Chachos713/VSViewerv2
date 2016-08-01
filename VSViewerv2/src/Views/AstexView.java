package Views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import Panels.AstexPanel;
import Util.Database;

public class AstexView implements ActionListener {
	private AstexPanel ap;
	private JFrame frame;
	private int dim;

	public AstexView(Database d) {
		dim = d.getMolecule(0).getDimension();

		if (dim < 3)
			return;

		ap = new AstexPanel(d, this, false, true);

		frame = new JFrame("3D View (Astex)");
		frame.setJMenuBar(createMenu());
		frame.add(ap);
		frame.pack();
	}

	private JMenuBar createMenu() {
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

		JMenu tools = new JMenu("Tools");

		JMenuItem advOptions = new JMenuItem("Advanced Options");
		advOptions.setActionCommand("A");
		advOptions.setMnemonic('a');
		advOptions.addActionListener(this);

		tools.add(advOptions);
		tools.add(ap.getProteinOptions());

		menu.add(file);
		menu.add(tools);

		return menu;
	}

	public void show(boolean last) {
		frame.setVisible(ap.display() && (!last || frame.isVisible()));
	}

	public void addMol(int m) {
		if (m < 0)
			return;

		ap.addMolecule(m);

		if (dim >= 3)
			show(false);
	}

	public void close() {
		frame.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ap.actionPerformed(e);
	}
}
