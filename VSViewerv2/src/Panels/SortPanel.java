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

/**
 * A simple panel for sorting the molecules.
 * 
 * @author Kyle Diller
 *
 */
@SuppressWarnings("serial")
public class SortPanel extends Box {
	private JList<String> labels;
	private JCheckBox desc;

	/**
	 * Creates a sorting panel based on the list of data labels.
	 * 
	 * @param label
	 *            the list of data labels.
	 */
	public SortPanel(ArrayList<DataLabel> label) {
		super(BoxLayout.Y_AXIS);

		DefaultListModel<String> header = new DefaultListModel<String>();
		labels = new JList<String>(header);
		labels.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		labels.setVisibleRowCount(5);

		for (DataLabel l : label) {
			header.addElement(l.getLabel());
		}
		
		header.addElement("Name");

		labels.setSelectedIndex(0);

		desc = new JCheckBox("Descending");

		JScrollPane scroller = new JScrollPane(labels);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		this.add(scroller);
		this.add(desc);
	}

	/**
	 * Returns whether the descending check box is selected or not.
	 * 
	 * @return true is the descending check box is selected.
	 */
	public boolean isDescending() {
		return desc.isSelected();
	}

	/**
	 * Gets which data label is selected to sort by.
	 * 
	 * @return the selected data label.
	 */
	public String getLabel() {
		return labels.getSelectedValue();
	}
}
