package Panels;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Filter.KNumberModel;
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
public class DisplayPanel extends JPanel implements ChangeListener {
	private Box cPanel, sPanel, lPanel;
	private ActionListener action;
	private ButtonGroup cButts, sButts;
	private ArrayList<String> added;
	private JSpinner max, min;

	/**
	 * Creates the display panel to allow the user to interact with.
	 * 
	 * @param labels
	 *            the list of data labels
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
	public DisplayPanel(ArrayList<DataLabel> labels, int caxis, int saxis,
			int sMin, int sMax, ActionListener event) {

		added = new ArrayList<String>();
		action = event;
		this.setLayout(new GridLayout(2, 2));
		JRadioButton temp;
		JScrollPane buttons;

		cPanel = Box.createVerticalBox();
		cPanel.add(new JLabel("Coloring Options"));
		cButts = new ButtonGroup();

		temp = new JRadioButton("None");
		temp.setSelected(-1 == caxis);
		temp.addActionListener(event);
		cPanel.add(temp);
		cButts.add(temp);
		temp.setActionCommand("c-1");

		// Creates the color by
		for (int i = 0; i < labels.size(); i++) {
			temp = new JRadioButton(labels.get(i).getLabel());
			temp.setSelected(i == caxis);
			temp.addActionListener(event);
			temp.setActionCommand("c" + i);
			cPanel.add(temp);
			cButts.add(temp);
			added.add(labels.get(i).getLabel());
		}

		buttons = new JScrollPane(cPanel);
		buttons.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		buttons.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		buttons.setPreferredSize(new Dimension(100, 200));

		this.add(buttons);

		sPanel = Box.createVerticalBox();
		sPanel.add(new JLabel("Size Options"));
		sButts = new ButtonGroup();

		temp = new JRadioButton("None");
		temp.setSelected(-1 == saxis);
		temp.addActionListener(event);
		temp.setActionCommand("s-1");
		sPanel.add(temp);
		sButts.add(temp);

		// Creates the size by
		for (int i = 0; i < labels.size(); i++) {
			temp = new JRadioButton(labels.get(i).getLabel());
			temp.setSelected(i == saxis);
			temp.addActionListener(event);
			temp.setActionCommand("s" + i);
			sPanel.add(temp);
			sButts.add(temp);
		}

		buttons = new JScrollPane(sPanel);
		buttons.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		buttons.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		buttons.setPreferredSize(new Dimension(100, 200));

		this.add(buttons);

		Box set = Box.createVerticalBox();

		JPanel te = new JPanel(new GridLayout(1, 2));
		te.add(new JLabel("Marker Min:"));
		min = new JSpinner(new KNumberModel(sMin, 1, 1000, 1));
		te.add(min);

		JPanel t = new JPanel();
		t.add(te);

		set.add(t);

		te = new JPanel(new GridLayout(1, 2));
		te.add(new JLabel("CMD", SwingConstants.CENTER));
		te.add(new JLabel("Bioscience"));
		set.add(te);

		te = new JPanel(new GridLayout(1, 2));
		te.add(new JLabel("Marker Max:"));
		max = new JSpinner(new KNumberModel(sMax, 1, 1000, 1));
		te.add(max);
		t = new JPanel();
		t.add(te);
		set.add(t);

		min.addChangeListener(this);
		max.addChangeListener(this);

		this.add(set);

		lPanel = Box.createVerticalBox();
		lPanel.add(new JLabel("Display Types"));

		JComboBox<String> ltemp;
		JPanel pTemp;

		for (int i = 0; i < labels.size(); i++) {
			ltemp = new JComboBox<String>();
			ltemp.addItem("None");
			ltemp.addItem("Log");
			ltemp.addItem("Percentile");
			ltemp.addActionListener(event);
			ltemp.setActionCommand("l" + i);
			ltemp.setSelectedIndex(labels.get(i).getDisType());

			pTemp = new JPanel(new GridLayout(1, 2));
			pTemp.add(new JLabel(labels.get(i).getLabel()));
			pTemp.add(ltemp);
			lPanel.add(pTemp);
		}

		buttons = new JScrollPane(lPanel);
		buttons.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		buttons.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		buttons.setPreferredSize(new Dimension((int) lPanel.getPreferredSize()
				.getWidth() + 20, 200));

		this.add(buttons);

		this.setPreferredSize(new Dimension(
				buttons.getPreferredSize().width * 2 + 5, 405));
	}

	/**
	 * Add new data labels.
	 * 
	 * @param labels
	 *            the list of data labels.
	 */
	public void update(ArrayList<DataLabel> labels) {
		JRadioButton temp;
		JComboBox<String> ltemp;
		JPanel pTemp;

		for (int i = 0; i < labels.size(); i++) {
			if (added.contains(labels.get(i).getLabel()))
				continue;

			temp = new JRadioButton(labels.get(i).getLabel());
			temp.setSelected(false);
			temp.addActionListener(action);
			temp.setActionCommand("c" + i);
			cPanel.add(temp);
			cButts.add(temp);
			added.add(labels.get(i).getLabel());

			temp = new JRadioButton(labels.get(i).getLabel());
			temp.setSelected(false);
			temp.addActionListener(action);
			temp.setActionCommand("s" + i);
			sPanel.add(temp);
			sButts.add(temp);

			ltemp = new JComboBox<String>();
			ltemp.addItem("None");
			ltemp.addItem("Log");
			ltemp.addItem("Percentile");
			ltemp.addActionListener(action);
			ltemp.setActionCommand("l" + i);
			ltemp.setSelectedIndex(labels.get(i).getDisType());

			pTemp = new JPanel(new GridLayout(1, 2));
			pTemp.add(new JLabel(labels.get(i).getLabel()));
			pTemp.add(ltemp);
			lPanel.add(pTemp);
		}
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		double v1 = (double) min.getValue();
		double v2 = (double) max.getValue();

		double min = Math.min(v1, v2);
		double max = Math.max(v1, v2);

		this.min.setValue(min);
		this.max.setValue(max);

		((ScatterPlotPanel) action).setMinMax(min, max);
	}
}
