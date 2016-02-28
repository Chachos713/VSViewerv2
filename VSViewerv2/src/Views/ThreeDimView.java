package Views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import Panels.ThreeDimPanel;
import Util.Database;

public class ThreeDimView implements ActionListener {
	private ThreeDimPanel tdp;
	private JFrame frame;
	private int dim;

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

	public void show(boolean last) {
		frame.setVisible(tdp.display() && (!last || frame.isVisible()));
	}

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

	public void addMol(int m) {
		if (m < 0)
			return;

		tdp.addMolecule(m);

		if (dim >= 3)
			show(false);
	}

	public void close() {
		frame.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		tdp.actionPerformed(e);
	}
}
