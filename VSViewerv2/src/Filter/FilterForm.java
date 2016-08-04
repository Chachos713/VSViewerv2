package Filter;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Molecule.Molecule;
import Util.DataLabel;
import Util.Node;

/**
 * The form to display all the filter panels.
 * 
 * @author Kyle Diller
 *
 */
public class FilterForm implements WindowListener, ActionListener,
		ChangeListener {
	private Node<FilterPanel> panels;
	private Box view;
	private Dialog frame;
	private Frame owner;

	/**
	 * Creates the form to display.
	 * 
	 * @param owner
	 *            the owner of the form.
	 * @param desc
	 *            the list of descriptors.
	 */
	public FilterForm(Frame owner, ArrayList<DataLabel> desc) {
		this.owner = owner;
		view = Box.createVerticalBox();

		JScrollPane scroll = new JScrollPane(view);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(450, 300));

		frame = new Dialog(owner, "Filter");
		frame.addWindowListener(this);
		frame.add(scroll);
		frame.pack();

		JPanel id = new JPanel(new GridLayout(1, 4));
		id.add(new JLabel("Name"));
		id.add(new JLabel("Min"));
		id.add(new JLabel("Max"));
		id.add(new JLabel("Missing Data"));
		view.add(id);

		update(desc);
	}

	/**
	 * Check that all the descriptors are present.
	 * 
	 * @param desc
	 *            the list of desriptors.
	 */
	public void update(ArrayList<DataLabel> desc) {
		for (DataLabel d : desc) {
			if (!hasDesc(d.getLabel())) {
				FilterPanel temp = new FilterPanel(d, this);
				view.add(temp);
				Node<FilterPanel> fTemp = new Node<FilterPanel>(temp, panels);
				panels = fTemp;
				view.revalidate();
			}
		}
	}

	/**
	 * Looks for a descriptor through the panels.
	 * 
	 * @param label
	 *            the descriptor to look for.
	 * @return true if the descriptor was found.
	 */
	private boolean hasDesc(String label) {
		Node<FilterPanel> temp = panels;

		while (temp != null) {
			if (temp.getData().getDesc().equals(label))
				return true;

			temp = temp.getNext();
		}

		return false;
	}

	/**
	 * Shows the form.
	 */
	public void show() {
		frame.setVisible(true);
	}

	/**
	 * Hides the form.
	 */
	public void close() {
		frame.setVisible(false);
	}

	/**
	 * Checks if a peptide is deservings of bing drawn.
	 * 
	 * @param pep
	 *            the peptide to check.
	 * @return true if the peptide's data is valid.
	 */
	public boolean addMolecule(Molecule pep) {
		Node<FilterPanel> temp = panels;

		while (temp != null) {
			if (!temp.getData().checkMolecule(pep))
				return false;

			temp = temp.getNext();
		}

		return true;
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		owner.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		owner.repaint();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		close();
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
