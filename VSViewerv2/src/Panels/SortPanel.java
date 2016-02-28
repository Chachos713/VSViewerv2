package Panels;

import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import Util.DataLabel;

@SuppressWarnings("serial")
public class SortPanel extends Box {
	private JList<String> labels;
	private JCheckBox desc;

	public SortPanel(ArrayList<DataLabel> label) {
		super(BoxLayout.Y_AXIS);

		DefaultListModel<String> header = new DefaultListModel<String>();
		labels = new JList<String>(header);
		labels.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		labels.setVisibleRowCount(5);

		for (DataLabel l : label) {
			header.addElement(l.getLabel());
		}

		labels.setSelectedIndex(0);

		desc = new JCheckBox("Descending");

		JScrollPane scroller = new JScrollPane(labels);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		this.add(scroller);
		this.add(desc);
	}

	public boolean isDescending() {
		return desc.isSelected();
	}

	public String getLabel() {
		return labels.getSelectedValue();
	}
}
