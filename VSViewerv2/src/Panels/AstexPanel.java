package Panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import Util.Database;
import Util.MolPanel;
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

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}
