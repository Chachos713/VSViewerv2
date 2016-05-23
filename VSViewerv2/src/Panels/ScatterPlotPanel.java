package Panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.vecmath.Point2d;

import Start.DefaultStart;
import Util.Calculator;
import Util.DataLabel;
import Util.Database;
import Util.KFileChooser;

/**
 * A simple form for displaying the scatter plot
 * 
 * @author Kyle Diller
 *
 */
@SuppressWarnings("serial")
public class ScatterPlotPanel extends JPanel implements ActionListener,
		Observer {
	private int xaxis, yaxis, saxis, caxis;
	private ArrayList<DataLabel> labels;
	private boolean invertSelection;

	private int sMin, sMax;

	private Database data;

	private ArrayList<Point> circ;
	private Point hover, start;
	private int button;

	private Point2d[] molLoc;

	private double xZoom, yZoom, xLoc, yLoc;

	private PopupDisplay pd;
	private JFrame molSide;
	private int hoveredMol;

	private boolean drawLSRL;
	private double rBound;

	private JMenu xMenu, yMenu;

	/**
	 * Creates the panel for the scatter plot to be drawn on.
	 * 
	 * @param d
	 *            the database of molecules
	 * @param owner
	 *            the event listener and class that holds the reference to this
	 *            object
	 */
	public ScatterPlotPanel(Database d, EventListener owner) {
		data = d;
		d.addObserver(this);
		labels = d.getLabel();

		molLoc = new Point2d[d.getNumMols()];

		if (!(owner instanceof MouseListener && owner instanceof MouseMotionListener))
			return;

		this.addMouseListener((MouseListener) owner);
		this.addMouseMotionListener((MouseMotionListener) owner);
		this.addMouseWheelListener((MouseWheelListener) owner);

		xaxis = 0;
		yaxis = 1;
		saxis = -1;
		caxis = -1;

		sMin = 3;
		sMax = 9;

		yaxis %= data.getNumMols();

		hover = new Point();

		xZoom = 1;
		yZoom = 1;
		xLoc = 0;
		yLoc = 0;

		drawLSRL = false;
		rBound = 0.9;

		this.setPreferredSize(new Dimension(800, 800));

		JButton comp3 = new JButton("3D");
		comp3.setActionCommand("3");
		comp3.addActionListener(new Compare());
		comp3.setVisible(data.getMolecule(0).getDimension() > 2);

		JButton comp2 = new JButton("2D");
		comp2.setActionCommand("2");
		comp2.addActionListener(new Compare());
		comp2.setVisible(data.getMolecule(0).getDimension() > 1);

		JPanel butt = new JPanel();
		butt.add(new JLabel("Compare:"));
		butt.add(comp2);
		butt.add(comp3);

		pd = new PopupDisplay();

		molSide = new JFrame();
		molSide.getContentPane().add(BorderLayout.CENTER, pd);
		molSide.getContentPane().add(BorderLayout.SOUTH, butt);
		molSide.setLocation(820, 0);
		molSide.pack();
		molSide.setVisible(data.getMolecule(0).getDimension() >= 2);

		xMenu = new JMenu("X Axis");
		yMenu = new JMenu("Y Axis");
		createMenus((ActionListener) owner);
	}

	/**
	 * Creates the menus for easy to change x and y axis
	 * 
	 * @param act
	 *            the action listener for when a menu item is clicked
	 */
	private void createMenus(ActionListener act) {
		xMenu.removeAll();
		yMenu.removeAll();

		JMenuItem xTemp, yTemp;

		for (int i = 0; i < labels.size(); i++) {
			xTemp = new JMenuItem(labels.get(i).getLabel());
			xTemp.addActionListener(act);
			xTemp.setActionCommand("mx" + i);
			xMenu.add(xTemp);

			yTemp = new JMenuItem(labels.get(i).getLabel());
			yTemp.addActionListener(act);
			yTemp.setActionCommand("my" + i);
			yMenu.add(yTemp);
		}
	}

	/**
	 * @return the x axis menu
	 */
	public JMenu getXMenu() {
		return xMenu;
	}

	/**
	 * @return the y axis menu
	 */
	public JMenu getYMenu() {
		return yMenu;
	}

	/**
	 * Changes the axis label for the specific axis based on which menu item was
	 * clicked
	 * 
	 * @param command
	 *            the action command from the menu item that was clicked
	 */
	public void menuChange(String command) {
		int i = 0;
		try {
			i = Integer.parseInt(command.substring(2));
		} catch (Exception e) {
			return;
		}

		switch (command.charAt(1)) {
		case 'x':
			xaxis = i;
			break;
		case 'y':
			yaxis = i;
			break;
		}

		this.repaint();
	}

	/**
	 * Draws the scatter plot on the panelS
	 */
	public void paintComponent(Graphics g) {
		g.setColor(Color.black);
		Font f = new Font("SansSerif", Font.BOLD, 12);
		g.setFont(f);

		Dimension d = this.getSize();
		g.clearRect(0, 0, (int) d.getWidth(), (int) d.getHeight());

		// Gets the labels for each axis
		String x = labels.get(xaxis).getLabel();
		String y = labels.get(yaxis).getLabel();
		String c = caxis == -1 ? null : labels.get(caxis).getLabel();
		String s = saxis == -1 ? null : labels.get(saxis).getLabel();

		// Draws the borders
		drawVerticalCenteredString(y, 10, (int) (d.getHeight() / 2), g);
		drawCenteredString(x, (int) (d.getWidth() / 2),
				(int) (d.getHeight() - 10), g);

		drawCenteredString("Scatter Plot Display", (int) d.getWidth() / 2, 10,
				g);

		g.drawLine(20, 20, 20, (int) d.getHeight() - 20);
		g.drawLine(20, (int) d.getHeight() - 20, (int) d.getWidth() - 20,
				(int) d.getHeight() - 20);

		double[] xNums = new double[data.getNumMols()];
		double[] yNums = new double[data.getNumMols()];
		double[] sNums = new double[data.getNumMols()];
		double[] cNums = new double[data.getNumMols()];

		// Collects all the values for each axis
		for (int i = 0; i < data.getNumMols(); i++) {
			xNums[i] = data.getMolecule(i).getData(x);
			yNums[i] = data.getMolecule(i).getData(y);

			if (labels.get(xaxis).doLog())
				xNums[i] = (xNums[i] == Double.NEGATIVE_INFINITY) ? xNums[i]
						: Math.log10(xNums[i]);

			if (labels.get(yaxis).doLog())
				yNums[i] = (yNums[i] == Double.NEGATIVE_INFINITY) ? yNums[i]
						: Math.log10(yNums[i]);

			if (saxis != -1) {
				sNums[i] = data.getMolecule(i).getData(s);
				if (labels.get(saxis).doLog())
					sNums[i] = (sNums[i] == Double.NEGATIVE_INFINITY) ? sNums[i]
							: Math.log10(sNums[i]);
			}

			if (caxis != -1) {
				cNums[i] = data.getMolecule(i).getData(c);
				if (labels.get(caxis).doLog())
					cNums[i] = (cNums[i] == Double.NEGATIVE_INFINITY) ? cNums[i]
							: Math.log10(cNums[i]);
			}
		}

		double xlo, xhi, ylo, yhi, clo, chi, slo, shi;

		double xV, yV, cV, sV;

		xlo = ylo = slo = clo = Double.POSITIVE_INFINITY;
		xhi = yhi = shi = chi = Double.NEGATIVE_INFINITY;

		// Finds the minimum and maximum of the data
		for (int i = 0; i < data.getNumMols(); i++) {
			xV = xNums[i];
			yV = yNums[i];

			if (xV == Double.NEGATIVE_INFINITY
					|| yV == Double.NEGATIVE_INFINITY)
				continue;

			if (yV != Double.NEGATIVE_INFINITY)
				ylo = Math.min(ylo, yV);

			if (xV != Double.NEGATIVE_INFINITY)
				xlo = Math.min(xlo, xV);

			xhi = Math.max(xhi, xV);
			yhi = Math.max(yhi, yV);

			if (saxis != -1) {
				sV = sNums[i];

				if (sV != Double.NEGATIVE_INFINITY)
					slo = Math.min(slo, sV);

				shi = Math.max(shi, sV);
			}

			if (caxis != -1) {
				cV = cNums[i];

				if (cV != Double.NEGATIVE_INFINITY)
					clo = Math.min(clo, cV);

				chi = Math.max(chi, cV);
			}
		}

		// Calculates the range for drawing molecules
		double yDelta = (yhi - ylo) * .05;
		double xDelta = (xhi - xlo) * .05;

		xlo -= xDelta;
		xhi += xDelta;

		ylo -= yDelta;
		yhi += yDelta;

		double xPer, yPer, sPer, cPer;

		double xValue, yValue, sValue;
		Color cValue = null;

		double minDelta = Double.POSITIVE_INFINITY;
		int name = -1;
		double dis;

		// Draws the points
		for (int i = 0; i < data.getNumMols(); i++) {
			molLoc[i] = null;
			xV = xNums[i];
			yV = yNums[i];

			if (c != null) {
				cV = cNums[i];

				if (cV == Double.NEGATIVE_INFINITY)
					cValue = Color.blue;
				else {
					cPer = (cV - clo) / (chi - clo);
					cValue = new Color((int) (255 * cPer), 0,
							(int) (255 * (1 - cPer)));
				}
			} else {
				cValue = Color.red;
			}

			if (data.isSelected(i))
				cValue = Color.green;

			g.setColor(cValue);

			if (s != null) {
				sV = sNums[i];

				if (sV == Double.NEGATIVE_INFINITY)
					sValue = sMin;
				else {
					sPer = (sV - slo) / (shi - slo);
					sValue = (int) (sPer * (sMax - sMin) + sMin);
				}
			} else {
				sValue = (sMin + sMax) / 2;
			}

			if (xV == Double.NEGATIVE_INFINITY
					|| yV == Double.NEGATIVE_INFINITY)
				continue;

			xPer = (xV - xlo) / (xhi - xlo) * xZoom;
			yPer = (yV - ylo) / (yhi - ylo) * yZoom;

			xValue = (int) (xPer * (d.getWidth() - 40) + xLoc);
			yValue = (int) (d.getHeight() - yPer * (d.getHeight() - 40) + yLoc);

			if (Calculator.inRange(20, this.getWidth() - 20, xValue)
					&& Calculator.inRange(20, this.getHeight() - 20, yValue)) {

				g.fillOval((int) (xValue - sValue), (int) (yValue - sValue),
						(int) (2 * sValue), (int) (2 * sValue));
				molLoc[i] = new Point2d(xValue, yValue);
			}

			dis = Calculator.dis(xValue, yValue, hover.x, hover.y);
			if (dis < sValue) {
				if (dis < minDelta) {
					minDelta = dis;
					name = i;
				}
			}
		}

		// Draws the minimum and maximum values on the axes
		xlo = Calculator.round(xlo + (xhi - xlo)
				* (((-xLoc) / (this.getWidth() - 40)) * (1 / xZoom)), 3);
		xhi = Calculator.round(xlo + (xhi - xlo)
				* ((this.getWidth() - 40 - xLoc) / (this.getWidth() - 40))
				* (1 / xZoom), 3);
		ylo = Calculator.round(
				ylo + (yhi - ylo)
						* (((-yLoc) / (this.getHeight() - 40)) * (1 / yZoom)),
				3);
		yhi = Calculator.round(ylo + (yhi - ylo)
				* ((this.getHeight() - 40 - yLoc) / (this.getHeight() / 40))
				* (1 / yZoom), 3);

		g.setColor(Color.black);

		drawCenteredString("" + xlo, 45, (int) d.getHeight() - 10, g);
		drawCenteredString("" + xhi, (int) d.getWidth() - 45,
				(int) d.getHeight() - 10, g);

		drawVerticalCenteredString("" + yhi, 10, 45, g);
		drawVerticalCenteredString("" + ylo, 10, (int) d.getHeight() - 45, g);

		// Draws the circle
		if (circ != null) {
			for (int i = 0; i < circ.size() - 1; i++) {
				g.drawLine(circ.get(i).x, circ.get(i).y, circ.get(i + 1).x,
						circ.get(i + 1).y);
			}
		}

		// Draws molecule name hovered over
		if (name >= 0) {
			g.drawString(data.getMolecule(name).getName(), hover.x, hover.y);
			hoveredMol = name;
			if (!molSide.isVisible())
				molSide.setVisible(data.getMolecule(0).getDimension() >= 2
						&& data.display(0));
		}

		// Draws LSRL if need be
		double[] lsrl = Calculator.LSRL(molLoc);

		double mYlo = lsrl[0] * 20 + lsrl[1];
		double mYhi = lsrl[0] * (this.getWidth() - 20) + lsrl[1];
		double mXlo = 20;
		double mXhi = this.getWidth() - 20;

		if (drawLSRL && Math.abs(lsrl[2]) >= rBound)
			g.drawLine((int) mXlo, (int) mYlo, (int) mXhi, (int) mYhi);

		pd.repaint();
	}

	/**
	 * Draws a centered screen around a point
	 * 
	 * @param s
	 *            the string to draw
	 * @param u
	 *            the x center
	 * @param v
	 *            the y center
	 * @param g
	 *            the graphics object to draw it on
	 */
	public void drawCenteredString(String s, int u, int v, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		int x = u - fm.stringWidth(s) / 2;
		int y = v + fm.getAscent() / 2;
		g.drawString(s, x, y);
	}

	/**
	 * Draws a vertical string centered around a point
	 * 
	 * @param s
	 *            the string
	 * @param u
	 *            the x center
	 * @param d
	 *            the y center
	 * @param g
	 *            the graphics object to draw it on
	 */
	public void drawVerticalCenteredString(String s, int u, int d, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(-Math.PI / 2.0);
		int x = u + fm.getAscent() / 2;
		int y = d + fm.stringWidth(s) / 2;
		g2.drawString(s, -y, x);
		g2.rotate(Math.PI / 2.0);
	}

	/**
	 * Shows the display options for the scatter plot
	 */
	public void displayOptions() {
		DisplayPanel d = new DisplayPanel(labels, xaxis, yaxis, caxis, saxis,
				sMin, sMax, this);

		JOptionPane.showConfirmDialog(this, d, "Display Options",
				JOptionPane.OK_CANCEL_OPTION);
	}

	/**
	 * Used to zomm in and out of the form
	 * 
	 * @param arg0
	 *            the mouse wheel event for how much to zoom
	 */
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		if (arg0.getWheelRotation() < 0) {
			xZoom *= 1.10;
			yZoom *= 1.10;
		} else if (arg0.getWheelRotation() > 0) {
			xZoom /= 1.10;
			yZoom /= 1.10;
		}

		this.repaint();
	}

	/**
	 * Determines which molecule was clicked on the scatter plot
	 * 
	 * @param arg0
	 *            the mouse click event
	 * @return if a molecule was clicked or not
	 */
	public boolean mouseClicked(MouseEvent arg0) {
		int delta = arg0.getButton() == 1 ? 1 : -1;

		int y = arg0.getY();
		int x = arg0.getX();

		if (y < 20 && x > 20) {
			displayOptions();
		} else if (x < 20) {
			yaxis += delta;
			yaxis = (yaxis + labels.size()) % labels.size();

			yZoom = 1;
			yLoc = 0;
		} else if (y > this.getHeight() - 20) {
			xaxis += delta;
			xaxis = (xaxis + labels.size()) % labels.size();

			xZoom = 1;
			xLoc = 0;
		} else {
			data.findClicked(arg0.getX(), arg0.getY(), molLoc);
			return true;
		}

		this.repaint();
		return false;
	}

	/**
	 * Starts the circling tool
	 * 
	 * @param arg0
	 *            the mouse event to get the first point
	 */
	public void mousePressed(MouseEvent arg0) {
		circ = new ArrayList<Point>();
		start = arg0.getPoint();
		button = arg0.getButton();
		circ.add(start);
		this.repaint();
	}

	/**
	 * Ends the circling tool to show which molecules are selected
	 * 
	 * @param arg0
	 *            the mouse venet of the last point
	 * @return whether something was selected or not
	 */
	public boolean mouseReleased(MouseEvent arg0) {
		circ.add(new Point(start));

		data.setSelected(circ, molLoc, invertSelection);

		boolean good = circ.size() > 3;

		circ = null;
		this.repaint();

		return good;
	}

	/**
	 * Adds more points to the circling tool
	 * 
	 * @param arg0
	 *            the mouse event of where the mouse is
	 */
	public void mouseDragged(MouseEvent arg0) {
		if (button == 1) {
			circ.add(arg0.getPoint());
		} else if (button == 3) {
			xLoc += (arg0.getX() - start.x);
			yLoc += (arg0.getY() - start.y);

			start = arg0.getPoint();
		}

		hover = new Point();

		this.repaint();
	}

	/**
	 * Changes when the mose is moved over points
	 * 
	 * @param arg0
	 *            the mouse event for where the mouse is
	 */
	public void mouseMoved(MouseEvent arg0) {
		hover = arg0.getPoint();
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand();

		switch (command.charAt(0)) {
		case 'x':
			xaxis = Integer.parseInt(command.substring(1));

			xZoom = 1;
			xLoc = 0;
			break;
		case 'y':
			yaxis = Integer.parseInt(command.substring(1));

			yZoom = 1;
			yLoc = 0;
			break;
		case 'c':
			caxis = Integer.parseInt(command.substring(1));
			break;
		case 's':
			saxis = Integer.parseInt(command.substring(1));
			break;
		case 'l':
			int label = Integer.parseInt(command.substring(1));
			labels.get(label).setDoLog(
					((JCheckBox) (arg0.getSource())).isSelected());
			break;
		}

		this.repaint();
	}

	@Override
	public void update(Observable o, Object arg) {
		labels = data.getLabel();

		this.createMenus(xMenu.getItem(0).getActionListeners()[0]);

		this.repaint();
	}

	/**
	 * @return which molecule was clicked last
	 */
	public int getSelected() {
		return data.getLastClicked();
	}

	/**
	 * Do something with a file menu item selected
	 * 
	 * @param c
	 *            the second char on the action command string
	 * @return whether to close the form or not
	 */
	public boolean fileChoices(char c) {
		KFileChooser kfc = KFileChooser.create();
		int choice;
		switch (c) {
		case 'C':
			if (!data.close())
				break;
			molSide.dispose();
			return true;
		case 'O':
			if (!data.close())
				break;

			choice = kfc.open(this, 0);

			if (choice != JFileChooser.APPROVE_OPTION)
				break;

			try {
				new DefaultStart(kfc.getFile().toString());
			} catch (Exception e) {
			}

			molSide.dispose();
			return true;
		case 'S':
			try {
				data.save();
			} catch (Exception e) {
			}
			break;
		case 'A':
			try {
				choice = kfc.save(this, 0);

				if (choice != JFileChooser.APPROVE_OPTION)
					break;

				data.saveAs(kfc.getFile().toString());
			} catch (Exception e) {
			}
			break;
		}
		return false;
	}

	/**
	 * Do something from the tools menu
	 * 
	 * @param s
	 *            the resulting string from the action command
	 */
	public void toolChoices(String s) {
		char c = s.charAt(0);
		switch (c) {
		case 'D':
			this.displayOptions();
			break;
		case 'I':
			invertSelection = !invertSelection;
			break;
		case 'C':
			data.searchComments();
			break;
		case 'L':
			lsrlSettings();
			break;
		case 'S':
			data.createSplit();
			break;
		case 'O':
			data.flipScreen(s.charAt(1));
			break;
		}
	}

	/**
	 * Changes the values for the lsrl to display by
	 */
	public void lsrlSettings() {
		Box b = Box.createVerticalBox();

		JCheckBox c = new JCheckBox("Draw Line of Best Fit", drawLSRL);
		c.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				drawLSRL = c.isSelected();
			}
		});

		b.add(c);

		JSlider s = new JSlider(0, 100, (int) (100 * rBound));
		final JLabel l = new JLabel("Value: " + rBound);

		s.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				rBound = s.getValue() / 100.0;
				l.setText("Value: " + rBound);
			}
		});

		b.add(s);
		b.add(l);

		JOptionPane.showMessageDialog(this, b, "Line of Best Fit Settings",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Deals with the popup window for which molecule is hovered over
	 * 
	 * @author Kyle Diller
	 *
	 */
	private class PopupDisplay extends JPanel {

		public PopupDisplay() {
			this.setPreferredSize(new Dimension(320, 170));
		}

		public void paintComponent(Graphics g) {
			if (data.getMolecule(0).getDimension() < 2 || !data.display(0)) {
				molSide.setVisible(false);
				return;
			}

			if (hoveredMol < 0)
				return;

			if (!molSide.isVisible())
				molSide.setVisible(true);

			g.clearRect(0, 0, this.getWidth(), this.getHeight());
			data.getMolecule(hoveredMol).draw(g, 10, 10, this.getWidth() - 20,
					this.getHeight() - 20, -1);
		}
	}

	/**
	 * Compare the structures of molecules against the one that is hovered over
	 * 
	 * @author Kyle Diller
	 *
	 */
	private class Compare implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (hoveredMol < 0)
				return;

			data.compare(hoveredMol, arg0.getActionCommand().contains("3"));
		}
	}
}
