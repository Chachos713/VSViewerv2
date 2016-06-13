package Panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileFilter;

import Util.Database;
import Util.Filter;
import Util.KFileChooser;
import Util.MolPanel;
import Views.AstexView;
import astex.MoleculeViewer;

@SuppressWarnings("serial")
public class AstexPanel extends JPanel implements Observer {
	private MoleculeViewer mv;
	private Database data;

	private MolPanel[] mols;

	public AstexPanel(Database d, ActionListener a, boolean readAll,
			boolean delete) {
		MolPanel.deletable = delete;
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(900, 700));

		data = d;
		data.addObserver(this);

		mv = new MoleculeViewer();

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

	public boolean display() {
		return data.display(3);
	}

	public void addMolecule(int mol) {
		if (mol < 0)
			return;

		if (!mols[mol].isSelected()) {
			// TODO
			// mv.addMolecule(data.getMolecule(mol).getAstex());
		}

		mols[mol].setVisible(true);
		mols[mol].setSelected(true);
	}

	public void removeMolecule(int i) {
		if (mols[i].isSelected()) {
			// TODO Remove molecule
		}

		mols[i].setVisible(false);
		mols[i].setSelected(false);
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
			// TODO Add molecule
		} else {
			// TODO Remove Molecule
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
		KFileChooser k = KFileChooser.create();

		String[] p = { ".png" };
		FileFilter[] png = { new Filter(p) };

		String[] d = { ".pdb" };
		FileFilter[] pdb = { new Filter(d) };

		if (source instanceof JCheckBox)
			findChangedBox(source);
		else if (source instanceof JButton)
			findButton(source);
		else
			switch (arg0.getActionCommand().charAt(0)) {
			case 'S':// Save image
				break;
			case 'O':// Open odb
			}

		this.repaint();
		mv.dirtyRepaint();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		((AstexView)mols[0].getButton().getActionListeners()[0]).show(true);
	}
}
