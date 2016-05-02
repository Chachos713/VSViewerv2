package Panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import Util.Database;
import Util.MolSorter;
import Views.MolGridView;

/**
 * A simple form to display 2D molecular strutures.
 * 
 * @author Kyle Diller
 *
 */
@SuppressWarnings("serial")
public class MolGridPanel extends JPanel implements ActionListener,
		ComponentListener, Observer {
	private int currentPos;
	private int numCols, numRows;
	private int width, height;

	private JButton next, prev, setting;
	private Drawer draw;

	private Database data;

	private ArrayList<Integer> display;

	/**
	 * Creates the form for the molecules to be drawn on.
	 * 
	 * @param owner
	 *            the class that will handle all the mouse clicks, and the class
	 *            that contains the JFrame
	 * @param d
	 *            the database of molecules
	 * @param mols
	 *            the list of molecules to display
	 */
	public MolGridPanel(MouseListener owner, Database d, ArrayList<Integer> mols) {
		draw = new Drawer(owner);
		display = mols;
		data = d;
		data.addObserver(this);

		currentPos = 0;
		numRows = 5;
		numCols = 5;
		width = 200;
		height = 100;

		draw.setPreferredSize(new Dimension(width * numCols + 20, height
				* numRows + 20));
		draw.setMinimumSize(new Dimension(width + 20, height + 20));
		draw.addComponentListener(this);

		JPanel buttons = new JPanel();

		next = new JButton("Next");
		next.addActionListener(this);

		prev = new JButton("Prev");
		prev.addActionListener(this);

		setting = new JButton("Set Size");
		setting.addActionListener(this);

		buttons.add(prev);
		buttons.add(setting);
		buttons.add(next);

		this.setLayout(new BorderLayout());
		this.add(BorderLayout.SOUTH, buttons);
		this.add(BorderLayout.CENTER, draw);
	}

	/**
	 * Allows the user to change the dimensions of the molecules drawn.
	 */
	public void changeSize() {
		SpinnerNumberModel snmW = new SpinnerNumberModel(width, 10, 1000, 10);
		SpinnerNumberModel snmH = new SpinnerNumberModel(height, 10, 1000, 10);

		JSpinner w = new JSpinner(snmW);
		JSpinner h = new JSpinner(snmH);

		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2, 2));
		p.add(new JLabel("Width: "));
		p.add(w);
		p.add(new JLabel("Height: "));
		p.add(h);

		int choice = JOptionPane.showConfirmDialog(this, p, "Change Size",
				JOptionPane.OK_CANCEL_OPTION);

		if (choice != JOptionPane.OK_OPTION)
			return;

		width = (int) snmW.getValue();
		height = (int) snmH.getValue();

		numCols = (this.getWidth() - 20) / width;
		numRows = (this.getHeight() - 20) / height;
	}

	/**
	 * Determines which molecule was clicked.
	 * 
	 * @param x
	 *            the x coordinate of the mouse click
	 * @param y
	 *            the y coordinate of the mouse click
	 * @return the molecule that was clicked
	 */
	public int molSelected(int x, int y) {
		int r = (y - 10) / height;
		int c = (x - 10) / width;

		if (r < 0 || r >= numRows)
			return -1;
		if (c < 0 || c >= numCols)
			return -1;

		int delta = r * numCols + c;

		if (delta >= display.size())
			return -1;

		int i = currentPos + delta;
		i %= display.size();

		return display.get(i);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == next && numRows * numCols < display.size()) {
			currentPos += (numCols * numRows);
			currentPos %= display.size();
		} else if (arg0.getSource() == prev
				&& numRows * numCols < display.size()) {
			currentPos -= (numCols * numRows);
			currentPos = (currentPos + display.size()) % display.size();
		} else if (arg0.getSource() == setting) {
			changeSize();
		}

		this.repaint();
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		int w = draw.getWidth();
		int h = draw.getHeight();

		numCols = (w - 20) / width;
		numRows = (h - 20) / height;

		this.repaint();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		display = new ArrayList<Integer>();

		for (int i = 0; i < data.getNumMols(); i++) {
			if (data.isSelected(i))
				display.add(i);
		}

		((MolGridView) draw.getMouseListeners()[0]).show();
	}

	/**
	 * @return the number or molecules that are drawn
	 */
	public int getNumMols() {
		return display.size();
	}

	/**
	 * @return whether to display the form or not
	 */
	public boolean display() {
		return data.display(1);
	}

	/**
	 * @param c
	 *            the second part of the action command when file is clicked
	 */
	public void file(char c) {
		switch (c) {
		case 'S':
			try {
				data.save(display);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * @param c
	 *            the second part of the action command when tools is clicked
	 */
	public void tools(char c) {
		switch (c) {
		case 'S':
			sort();
			break;
		}
	}

	/**
	 * Sorts the molecules based on a data label
	 */
	private void sort() {
		SortPanel sort = new SortPanel(data.getLabel());

		int choice = JOptionPane.showConfirmDialog(this, sort,
				"Sort Molecules", JOptionPane.OK_CANCEL_OPTION);

		if (choice != JOptionPane.OK_OPTION)
			return;

		String label = sort.getLabel();
		boolean descending = sort.isDescending();

		ArrayList<Double> values = new ArrayList<Double>();

		for (int i : display) {
			values.add(data.getMolecule(i).getData(label));
		}

		display = MolSorter.sort(display, values, descending);

		this.repaint();
	}

	/**
	 * Draws the molecules on the form
	 * 
	 * @author Kyle Diller
	 *
	 */
	private class Drawer extends JPanel {
		public Drawer(MouseListener ml) {
			this.addMouseListener(ml);
		}

		public void paintComponent(Graphics g) {
			g.clearRect(0, 0, this.getWidth(), this.getHeight());

			int numMols = display.size();
			int x, y;
			int i;
			int r, c;

			if (numMols < 1)
				return;

			for (int k = 0; k < numCols * numRows; k++) {
				i = (currentPos + k) % numMols;

				if (k >= numMols)
					break;

				r = k / numCols;
				c = k % numCols;

				x = width * c + 10;
				y = height * r + 10;

				data.getMolecule(display.get(i)).draw(g, x, y, width, height,
						1 + i);
			}
		}
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
	}
}
