package Filter;

import javax.swing.SpinnerNumberModel;

/**
 * A simple model to allow for number to be shown in the JSpinner.
 * 
 * @author Kyle Diller
 *
 */
@SuppressWarnings("serial")
public class KNumberModel extends SpinnerNumberModel {
	private double min, max, inc, value;

	/**
	 * Creates the model.
	 * 
	 * @param def
	 *            the default value.
	 * @param low
	 *            the minimum.
	 * @param high
	 *            the maximum.
	 * @param delta
	 *            the amount to change by.
	 */
	public KNumberModel(double def, double low, double high, double delta) {
		min = low;
		max = high;
		inc = delta;
		value = Math.min(max, Math.max(min, def));
	}

	@Override
	public Object getNextValue() {
		return change(1);
	}

	@Override
	public Object getPreviousValue() {
		return change(-1);
	}

	/**
	 * Changes the value based on a direction and amount.
	 * 
	 * @param dir
	 *            the way to change the number, and the amount.
	 * @return the new value to display.
	 */
	public Object change(int dir) {
		double newValue = (dir * inc) + value;

		newValue = Math.min(max, newValue);
		newValue = Math.max(min, newValue);

		return newValue;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object arg0) {
		if (arg0 == null || !(arg0 instanceof Number))
			return;

		if (((Double) arg0).doubleValue() != value) {
			value = ((Double) arg0).doubleValue();
			fireStateChanged();
		}
	}

}
