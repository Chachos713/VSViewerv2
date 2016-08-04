package Filter;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.EventListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Molecule.Molecule;
import Util.Calculator;
import Util.DataLabel;

/**
 * Creates a panel to control the range for a given descriptor.
 * 
 * @author Kyle Diller
 *
 */
@SuppressWarnings("serial")
public class FilterPanel extends JPanel implements ChangeListener {
	private String desc;
	private JSpinner minSpin, maxSpin;
	private JCheckBox include;

	/**
	 * Creates the panel.
	 * 
	 * @param name
	 *            the name of the descriptor.
	 * @param min
	 *            the lower bound.
	 * @param max
	 *            the upper bound.
	 * @param a
	 *            the event listener to tell it has changed.
	 */
	public FilterPanel(String name, double min, double max, EventListener a) {
		super(new GridLayout(1, 4));

		desc = name;
		this.add(new JLabel(desc));

		minSpin = new JSpinner(new KNumberModel(min, min, max,
				(max - min) / 100));
		maxSpin = new JSpinner(new KNumberModel(max, min, max,
				(max - min) / 100));
		include = new JCheckBox("Include", true);

		this.add(minSpin);
		this.add(maxSpin);
		this.add(include);

		minSpin.addChangeListener(this);
		maxSpin.addChangeListener(this);

		minSpin.addChangeListener((ChangeListener) a);
		maxSpin.addChangeListener((ChangeListener) a);
		include.addActionListener((ActionListener) a);
	}

	/**
	 * Creates a filter panel.
	 * 
	 * @param d
	 *            the data label to create the panel from.
	 * @param a
	 *            the event listener to use.
	 */
	public FilterPanel(DataLabel d, EventListener a) {
		this(d.getLabel(), d.getMin(), d.getMax(), a);
	}

	/**
	 * Checks if a peptide is within the data range.
	 * 
	 * @param pep
	 *            the peptide.
	 * @return true if the peptide's data is within the range.
	 */
	public boolean checkMolecule(Molecule pep) {
		double val = pep.getData(desc);

		return (include.isSelected() && val == Double.NEGATIVE_INFINITY)
				|| (Calculator.inRange((Double) minSpin.getValue(),
						(Double) maxSpin.getValue(), val));
	}

	/**
	 * @return the name of the descriptor.
	 */
	public String getDesc() {
		return desc;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		double min = Math.min((double) minSpin.getValue(),
				(double) maxSpin.getValue());
		double max = Math.max((double) minSpin.getValue(),
				(double) maxSpin.getValue());

		maxSpin.setValue(max);
		minSpin.setValue(min);
	}
}
