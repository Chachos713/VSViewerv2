package Panels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import Molecule.Molecule;
import Util.DataLabel;
import Util.Database;
import Views.CommentView;

/**
 * A simple panel to dipslay the comments, 2D structure, and data associated
 * with a given molecule.
 * 
 * @author Kyle Diller
 *
 */
@SuppressWarnings("serial")
public class CommentPanel extends JPanel implements Observer {
	private Database data;
	private int mol;

	private JTextArea dataDisplay;
	private JList<String> superComments, molComments;
	private DefaultListModel<String> superList, molList;
	private JButton add, delete;

	private ArrayList<DataLabel> label;

	/**
	 * Creates the view for the user to interact with.
	 * 
	 * @param d
	 *            the database of molecules
	 * @param c
	 *            the action listener for which the comments are to be added to
	 *            a molecule
	 */
	public CommentPanel(Database d, ActionListener c) {
		data = d;
		d.addObserver(this);
		mol = -1;

		dataDisplay = new JTextArea(8, 20);
		dataDisplay.setEditable(false);

		JScrollPane dataPane = new JScrollPane(dataDisplay);
		dataPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		dataPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		dataPane.setPreferredSize(new Dimension(210, 110));

		DrawMol dm = new DrawMol();

		JPanel un = new JPanel();
		un.setLayout(new GridLayout(1, 2));
		un.add(dm);
		un.add(dataPane);

		superList = new DefaultListModel<String>();
		molList = new DefaultListModel<String>();
		superComments = new JList<String>(superList);
		molComments = new JList<String>(molList);

		JPanel comm = new JPanel();
		comm.setLayout(new GridLayout(1, 2));

		JScrollPane molPane = new JScrollPane(molComments);
		molPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		molPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		molPane.setPreferredSize(new Dimension(200, 100));

		Box buttons = Box.createVerticalBox();

		add = new JButton("Add Comment");
		add.addActionListener(c);
		buttons.add(add);

		delete = new JButton("Delete Comment");
		delete.addActionListener(c);
		buttons.add(delete);

		comm.add(molPane);
		comm.add(buttons);

		this.setLayout(new GridLayout(2, 1));
		this.add(comm);
		this.add(un);

		ArrayList<String> comments = d.getMasterComments();

		for (int i = 0; i < comments.size(); i++) {
			superList.addElement(comments.get(i));
		}

		label = d.getLabel();
	}

	/**
	 * @return whether to display the form or not
	 */
	public boolean display() {
		return data.display(2);
	}

	/**
	 * Changes which molecule is displayed
	 * 
	 * @param m
	 *            the index of the new molecule
	 */
	public void changeMol(int m) {
		mol = m;
		updateSelected();
	}

	/**
	 * Updates the form for the user, when a molecule is changed
	 */
	public void updateSelected() {
		molList.clear();
		dataDisplay.setText("");

		if (mol < 0)
			return;

		Molecule m = data.getMolecule(mol);

		for (String c : m.getComments()) {
			molList.addElement(c);
		}

		for (DataLabel l : label) {
			dataDisplay.append(l.getLabel() + ": " + m.getData(l.getLabel())
					+ "\n");
		}

		this.repaint();
	}

	/**
	 * Compare the molecules against the current molecule
	 * 
	 * @param d3
	 *            3D comparison or 2D
	 */
	public void compare(boolean d3) {
		data.compare(mol, d3);
	}

	/**
	 * When a button is clicked this method controls what is to be done
	 * 
	 * @param e
	 *            the action event
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		String comment = null;
		boolean addNew = true;

		if (mol < 0) {
			JOptionPane.showMessageDialog(this, "No Molecule Selected",
					"ERROR", JOptionPane.ERROR_MESSAGE);

			return;
		}

		if (source == delete) {
			addNew = false;

			if (molComments.getSelectedIndex() < 0) {
				JOptionPane.showMessageDialog(this, "No Comment Selected",
						"ERROR", JOptionPane.ERROR_MESSAGE);

				return;
			}

			comment = molComments.getSelectedValue();
		} else if (source == add) {
			comment = addNewComment();
			if (comment.isEmpty())
				return;
		}

		data.modifyComment(mol, comment, addNew);
		updateSelected();

		superList.clear();
		ArrayList<String> comments = data.getMasterComments();

		for (int i = 0; i < comments.size(); i++) {
			superList.addElement(comments.get(i));
		}

		this.repaint();
	}

	private String addNewComment() {
		Box box = Box.createVerticalBox();
		final JTextField text = new JTextField(20);
		text.setEnabled(false);

		ButtonGroup bg = new ButtonGroup();

		JRadioButton ax = new JRadioButton("Master List");
		ax.setSelected(true);
		ax.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				text.setEnabled(false);
				superComments.setEnabled(true);
			}
		});
		bg.add(ax);

		JRadioButton an = new JRadioButton("Add New");
		an.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				superComments.setEnabled(false);
				text.setEnabled(true);
			}
		});
		bg.add(an);

		box.add(ax);
		box.add(new JScrollPane(superComments));
		box.add(an);
		box.add(text);

		int choice = JOptionPane.showConfirmDialog(this, box,
				"Add New Comment", JOptionPane.OK_CANCEL_OPTION);

		if (choice != JOptionPane.OK_OPTION)
			return "";

		if (ax.isSelected() && superComments.getSelectedIndex() >= 0)
			return superComments.getSelectedValue();
		else if (an.isSelected())
			return text.getText().trim();

		return "";
	}

	/**
	 * Used to draw the 2D structure
	 * 
	 * @author Kyle Diller
	 *
	 */
	private class DrawMol extends JPanel {
		public DrawMol() {
			this.setPreferredSize(new Dimension(210, 110));
		}

		public void paintComponent(Graphics g) {
			int width = this.getWidth() - 10;
			int height = this.getHeight() - 10;
			int x = 5;
			int y = 5;

			if (mol < 0)
				return;

			if (data.getMolecule(0).getDimension() < 2) {
				g.drawString("No 2D View Available", 0, 20);
				return;
			}

			g.clearRect(0, 0, this.getWidth(), this.getHeight());
			data.getMolecule(mol).draw(g, x, y, width, height, -1);
		}
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		updateSelected();

		superList.clear();
		ArrayList<String> comments = data.getMasterComments();

		for (int i = 0; i < comments.size(); i++) {
			superList.addElement(comments.get(i));
		}

		this.repaint();
		((CommentView) add.getActionListeners()[0]).setMolecule(mol, true);
	}
}
