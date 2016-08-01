package Panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileFilter;

import Molecule.Molecule;
import Util.Database;
import Util.Filter;
import Util.KFileChooser;
import Util.KUserInterface;
import Util.MolPanel;
import Util.MolViewer;
import Views.AstexView;
import astex.Color32;
import astex.DynamicArray;
import astex.Format;
import astex.MoleculeRenderer;

@SuppressWarnings("serial")
public class AstexPanel extends JPanel implements Observer, ActionListener {
	private MolViewer mv;
	private MoleculeRenderer mr;
	private Database data;

	private MolPanel[] mols;
	private static Format hex = new Format("0x%06x");

	private astex.Molecule protein;
	private KUserInterface ui;
	private JCheckBoxMenuItem[] proteinOpts;

	public AstexPanel(Database d, ActionListener a, boolean readAll,
			boolean delete) {
		MolPanel.deletable = delete;
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(900, 700));

		data = d;
		data.addObserver(this);

		mv = new MolViewer();
		mr = mv.moleculeRenderer;

		this.add(BorderLayout.CENTER, mv);

		mols = new MolPanel[data.getNumMols()];

		Box displayPanel = Box.createVerticalBox();

		for (int i = 0; i < mols.length; i++) {
			mols[i] = new MolPanel(data.getMolecule(i).getName(), a);
			mols[i].setVisible(readAll);

			displayPanel.add(mols[i]);
		}

		JScrollPane dataPane = new JScrollPane(displayPanel);
		dataPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		dataPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		dataPane.setPreferredSize(new Dimension(200, 700));

		this.add(BorderLayout.EAST, dataPane);
	}

	public JMenuItem getProteinOptions() {
		if (proteinOpts == null) {
			createOpts();
		}

		JMenu men = new JMenu("Protein Options");

		for (JCheckBoxMenuItem jcm : proteinOpts) {
			men.add(jcm);
		}

		return men;
	}

	private void createOpts() {
		final String[] opts = { "Show/Hide", "Mesh Surface",
				"Show/Hide Hydrogens", "Show Only Polar Hydrogens", "Surface",
				"Ribbon" };
		proteinOpts = new JCheckBoxMenuItem[opts.length];

		for (int i = 0; i < opts.length; i++) {
			proteinOpts[i] = new JCheckBoxMenuItem(opts[i]);
			proteinOpts[i].addActionListener(this);
			proteinOpts[i].setActionCommand(opts[i]);
		}

		resetOpts();
	}

	private void resetOpts() {
		final boolean[] def = { true, false, true, true, false, false };

		for (int i = 0; i < def.length; i++) {
			proteinOpts[i].setSelected(def[i]);
		}
	}

	public boolean display() {
		return data.display(3);
	}

	public void addMolecule(int mol) {
		if (mol < 0)
			return;

		if (!mols[mol].isSelected()) {
			addMolecule(data.getMolecule(mol));
		}

		mols[mol].setVisible(true);
		mols[mol].setSelected(true);
	}

	public void removeMolecule(int i) {
		if (mols[i].isSelected()) {
			removeMolecule(data.getMolecule(i));
		}

		mols[i].setVisible(false);
		mols[i].setSelected(false);
	}

	private void addMolecule(Molecule mol) {
		mv.addMolecule(mol.getAstex());

		mr.execute("select molexact '" + mol.getName() + "';");
		mr.execute("display cylinders on current;");
		mr.execute("display lines off current;");
		mr.execute("ball_radius 0 current;");
		mr.execute("exclude molexact '" + mol.getName() + "';");
	}

	private void removeMolecule(Molecule mol) {
		DynamicArray da = mv.moleculeRenderer.getMolecules();
		da.remove(mol.getAstex());
	}

	private void findChangedBox(Object source) {
		int i = 0;

		for (i = 0; i < mols.length; i++) {
			if (mols[i].isCheck(source))
				break;
		}

		if (i == mols.length)
			return;

		if (mols[i].isSelected()) {
			addMolecule(data.getMolecule(i));
		} else {
			removeMolecule(data.getMolecule(i));
		}
	}

	private void findButton(Object source) {
		int i = 0;

		for (i = 0; i < mols.length; i++) {
			if (mols[i].isRemove(source))
				break;
		}

		if (i == mols.length)
			return;

		removeMolecule(i);
	}

	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();

		String[] p = { ".png" };
		FileFilter[] png = { new Filter(p) };

		String[] d = { ".pdb" };
		FileFilter[] pdb = { new Filter(d) };

		File f;
		KFileChooser kfc = KFileChooser.create();
		int choice;

		if (source instanceof JCheckBox)
			findChangedBox(source);
		else if (source instanceof JButton)
			findButton(source);
		else if (source instanceof JCheckBoxMenuItem) {
			for (int i = 0; i < proteinOpts.length; i++) {
				if (source == proteinOpts[i]) {
					runCommand(i, proteinOpts[i]);
				}
			}
		} else
			switch (arg0.getActionCommand().charAt(0)) {
			case 'S':// Save image
				choice = kfc.save(this, png);
				break;
			case 'O':// Open pdb
				choice = kfc.open(this, pdb);

				if (choice != JFileChooser.APPROVE_OPTION)
					return;

				f = kfc.getFile();
				openProtein(f);
				break;
			case 'A':
				if (ui == null)
					ui = new KUserInterface(mv);
				else
					ui.userInterfaceFrame.setVisible(true);
			}

		this.repaint();
		mv.dirtyRepaint();
	}

	private void openProtein(File f) {
		MoleculeRenderer mr = mv.moleculeRenderer;
		DynamicArray da = mr.getMolecules();

		if (protein != null) {
			da.remove(protein);
		}

		mv.loadMolecule(f.toString());

		for (int i = 0; i < da.size(); i++) {
			if (((astex.Molecule) da.get(i)).getName().equals(f.toString())) {
				protein = (astex.Molecule) da.get(i);
			}
		}

		mr.execute("select molexact '" + protein.getName() + "';");
		mr.execute("display cylinders on current;");
		mr.execute("display lines off current;");
		mr.execute("ball_radius 0 current;");
		mr.execute("select none;");

		resetOpts();

		for (int i = 0; i < proteinOpts.length; i++) {
			runCommand(i, proteinOpts[i]);
		}
	}

	/**
	 * Alters the view based on the state change of one of the check box
	 * changes.
	 * 
	 * @param i
	 *            the index of the check box that is changed.
	 * @param source
	 *            the check box that is changed.
	 */
	private void runCommand(int i, Object source) {
		astex.Molecule mol = protein;
		int type = 0;

		if (mol == null)
			return;

		switch (i) {
		case 0: // Show/Hide the molecule
			String dis = ((JCheckBoxMenuItem) source).isSelected() ? "on"
					: "off";
			mr.execute("select molexact '" + mol.getName() + "';");
			mr.execute("display cylinders " + dis + " current;");
			mr.execute("display lines " + dis + " current;");
			mr.execute("exclude molexact '" + mol.getName() + "';");

			if (((JCheckBoxMenuItem) source).isSelected()) {
				for (int j = 1; j < proteinOpts.length; j++) {
					runCommand(j, proteinOpts[j]);
				}
			} else {
				mr.execute("object remove surf_" + type + ";");
				mr.execute("object remove surf_mesh_" + type + ";");
				mr.execute("object remove ribb_" + type + ";");
				mr.execute("select molexact '" + mol.getName() + "';");
				mr.execute("label clear current;");
				mr.execute("select molexact '" + mol.getName() + "';");
			}
			break;
		case 1: // Shows/Hide the mesh surface
			if (((JCheckBoxMenuItem) source).isSelected()) {
				int col = (type == 0) ? Color32.green : Color32.magenta;
				String color = hex.format(col & 0xFFFFFF);

				mr.execute("select molexact '" + mol.getName() + "';");
				mr.execute("surface -probe 1 -solid false surf_mesh_" + type
						+ " " + color + " current;");
				mr.execute("exclude molexact '" + mol.getName() + "';");
			} else {
				mr.execute("object remove surf_mesh_" + type + ";");
			}
			break;
		case 2: // Show/Hide all hydrogens
			dis = ((JCheckBoxMenuItem) source).isSelected() ? "on" : "off";
			mr.execute("select molexact '" + mol.getName() + "';");
			mr.execute("exclude not (element 1);");
			mr.execute("display cylinders " + dis + " current;");
			mr.execute("display lines " + dis + " current;");
			mr.execute("exclude molexact '" + mol.getName() + "';");
			runCommand(3, proteinOpts[3]);
			break;
		case 3: // Only show polar hydrogens
			if (type == 1)
				break;

			dis = ((JCheckBoxMenuItem) source).isSelected() ? "off" : "on";

			mr.execute("select molexact '" + mol.getName() + "';");
			mr.execute("exclude not (element 1);");

			for (int j = 0; j < mol.getAtomCount(); j++) {
				if (mol.getAtom(j).getElement() == 1
						&& mol.getAtom(j).getBondedAtom(0).getElement() != 6)
					mr.execute("exclude id " + mol.getAtom(j).getId() + ";");
			}

			mr.execute("display cylinders " + dis + " current;");
			mr.execute("display lines " + dis + " current;");
			mr.execute("exclude molexact '" + mol.getName() + "';");
			break;
		case 4: // Hide/Show the solid surface
			if (((JCheckBoxMenuItem) source).isSelected()) {
				int col = (type == 0) ? Color32.green : Color32.magenta;
				String color = hex.format(col & 0xFFFFFF);

				mr.execute("select molexact '" + mol.getName() + "';");
				mr.execute("surface -probe 1 -solid true surf_" + type + " "
						+ color + " current;");
				mr.execute("exclude molexact '" + mol.getName() + "';");
			} else {
				mr.execute("object remove surf_" + type + ";");
			}
			break;
		case 5: // Show/Hide the ribbon
			if (((JCheckBoxMenuItem) source).isSelected()) {
				int col = (type == 0) ? Color32.blue : Color32.red;
				String color = hex.format(col & 0xFFFFFF);
				System.out.println(color);

				mr.execute("select molexact '" + mol.getName() + "';");
				mr.execute("schematic -name ribb_" + type
						+ " -colorbyss true -tubecolor " + color + " current;");
				mr.execute("exclude molexact '" + mol.getName() + "';");
			} else {
				mr.execute("object remove ribb_" + type + ";");
			}
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		((AstexView) mols[0].getButton().getActionListeners()[0]).show(true);
	}
}
