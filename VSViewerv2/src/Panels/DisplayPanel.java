package Panels;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import Util.DataLabel;

/**
 * A simple panel that allows the user to change what value to put on the x and
 * y axis, as well as to color by and size by, and what value to take the log
 * of.
 * 
 * @author Kyle Diller
 *
 */
@SuppressWarnings("serial")
public class DisplayPanel extends JPanel {
	/**
	 * Creates the display panel to allow the user to interact with.
	 * 
	 * @param labels
	 *            the list of data labels
	 * @param xaxis
	 *            the current value for the x axis
	 * @param yaxis
	 *            the current value for the y axis
	 * @param caxis
	 *            the current value for the color by
	 * @param saxis
	 *            the current value for the size by
	 * @param sMin
	 *            the current minimum for the size by
	 * @param sMax
	 *            the current maximum for the size by
	 * @param event
	 *            the action listener for whenever a new value is changed
	 */
	public DisplayPanel(ArrayList<DataLabel> labels, int xaxis, int yaxis,
			int caxis, int saxis, int sMin, int sMax, ActionListener event) {

		this.setLayout(new GridLayout(1, 4));

		Box xPanel = Box.createVerticalBox();
		xPanel.add(new JLabel("X-Axis Options"));
		JRadioButton temp;
		ButtonGroup xButts = new ButtonGroup();

		for (int i = 0; i < labels.size(); i++) {
			temp = new JRadioButton(labels.get(i).getLabel());
			temp.setSelected(i == xaxis);
			temp.addActionListener(event);
			temp.setActionCommand("x" + i);
			xPanel.add(temp);
			xButts.add(temp);
		}

		JScrollPane buttons = new JScrollPane(xPanel);
		buttons.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		buttons.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		buttons.setPreferredSize(new Dimension(100, 300));

		this.add(buttons);

		Box yPanel = Box.createVerticalBox();
		yPanel.add(new JLabel("Y-Axis Options"));
		ButtonGroup yButts = new ButtonGroup();

		for (int i = 0; i < labels.size(); i++) {
			temp = new JRadioButton(labels.get(i).getLabel());
			temp.setSelected(i == yaxis);
			temp.addActionListener(event);
			temp.setActionCommand("y" + i);
			yPanel.add(temp);
			yButts.add(temp);
		}

		buttons = new JScrollPane(yPanel);
		buttons.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		buttons.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		buttons.setPreferredSize(new Dimension(150, 300));

		this.add(buttons);

		Box cPanel = Box.createVerticalBox();
		cPanel.add(new JLabel("Coloring Options"));
		ButtonGroup cButts = new ButtonGroup();

		temp = new JRadioButton("None");
		temp.setSelected(-1 == caxis);
		temp.addActionListener(event);
		cPanel.add(temp);
		cButts.add(temp);
		temp.setActionCommand("c-1");

		for (int i = 0; i < labels.size(); i++) {
			temp = new JRadioButton(labels.get(i).getLabel());
			temp.setSelected(i == caxis);
			temp.addActionListener(event);
			temp.setActionCommand("c" + i);
			cPanel.add(temp);
			cButts.add(temp);
		}

		buttons = new JScrollPane(cPanel);
		buttons.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		buttons.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		buttons.setPreferredSize(new Dimension(100, 300));

		this.add(buttons);

		Box sPanel = Box.createVerticalBox();
		sPanel.add(new JLabel("Size Options"));
		ButtonGroup sButts = new ButtonGroup();

		temp = new JRadioButton("None");
		temp.setSelected(-1 == saxis);
		temp.addActionListener(event);
		temp.setActionCommand("s-1");
		sPanel.add(temp);
		sButts.add(temp);

		for (int i = 0; i < labels.size(); i++) {
			temp = new JRadioButton(labels.get(i).getLabel());
			temp.setSelected(i == saxis);
			temp.addActionListener(event);
			temp.setActionCommand("s" + i);
			sPanel.add(temp);
			sButts.add(temp);
		}

		buttons = new JScrollPane(sPanel);
		buttons.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		buttons.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		buttons.setPreferredSize(new Dimension(100, 300));

		this.add(buttons);

		Box lPanel = Box.createVerticalBox();
		lPanel.add(new JLabel("Log Options"));

		JCheckBox ltemp;

		for (int i = 0; i < labels.size(); i++) {
			ltemp = new JCheckBox(labels.get(i).getLabel());
			ltemp.setSelected(labels.get(i).doLog());
			ltemp.setEnabled(labels.get(i).canLog());
			ltemp.addActionListener(event);
			ltemp.setActionCommand("l" + i);
			lPanel.add(ltemp);
		}

		buttons = new JScrollPane(lPanel);
		buttons.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		buttons.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		buttons.setPreferredSize(new Dimension(100, 300));

		this.add(buttons);
	}

}
