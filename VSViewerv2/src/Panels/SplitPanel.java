package Panels;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Util.DataLabel;
import Util.Database;

@SuppressWarnings("serial")
public class SplitPanel extends JPanel implements ListSelectionListener,
		ChangeListener {
	private Database data;

	private JList<String> labels;
	private JTextField name;
	private JSlider slide;
	private JLabel value;

	public SplitPanel(Database d) {
		this.setLayout(new BorderLayout());
		data = d;

		DefaultListModel<String> header = new DefaultListModel<String>();
		labels = new JList<String>(header);
		labels.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		labels.setVisibleRowCount(5);
		labels.addListSelectionListener(this);

		for (DataLabel l : data.getLabel()) {
			header.addElement(l.getLabel());
		}

		JScrollPane scroller = new JScrollPane(labels);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		JPanel panel1 = new JPanel();
		panel1.add(scroller);

		slide = new JSlider();
		slide.addChangeListener(this);

		name = new JTextField(10);
		value = new JLabel();

		JPanel names = new JPanel();
		names.add(name);

		Box east = Box.createVerticalBox();
		east.add(new JLabel("Set Value:"));
		east.add(slide);
		east.add(value);
		east.add(new JLabel("Name:"));
		east.add(names);

		Box west = Box.createVerticalBox();
		west.add(new JLabel("Label:"));
		west.add(scroller);

		this.add(BorderLayout.WEST, west);
		this.add(BorderLayout.EAST, east);

		labels.setSelectedIndex(0);
	}

	public double getValue() {
		return slide.getValue();
	}

	public String getName() {
		return name.getText();
	}

	public String getLabel() {
		return labels.getSelectedValue();
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		String s = labels.getSelectedValue();
		double min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;

		for (int i = 0; i < data.getNumMols(); i++) {
			double v = data.getMolecule(i).getData(s);

			if (v != Double.NEGATIVE_INFINITY) {
				min = Math.min(min, v);
				max = Math.max(max, v);
			}
		}

		slide.setMaximum((int) max);
		slide.setMinimum((int) min);
		slide.setValue((int) ((min + max) / 2));
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		value.setText("Value: " + slide.getValue());
	}
}
