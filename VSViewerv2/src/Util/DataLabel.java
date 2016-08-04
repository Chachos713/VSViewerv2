package Util;

/**
 * Helps to maintain which data label can use a log scale, and maintain which
 * data labels to use a log scale for
 * 
 * @author Kyle Diller
 *
 */
public class DataLabel {
	private String label;
	private boolean log;
	private double min, max;
	private int disType;
	private Node<Double> values;

	/**
	 * Creates a data label with a name and an initial value.
	 * 
	 * @param l
	 *            the name of the label.
	 * @param p
	 *            the first value associated with that label.
	 */
	public DataLabel(String l, double p) {
		log = p > 0;
		label = l;
		disType = 0;
		min = p;
		max = p;
		values = new Node<Double>(p, null);
		values = new Node<Double>(0.0, values);
	}

	/**
	 * Adds another value to the label.
	 * 
	 * @param value
	 *            another value associated with the label.
	 */
	public void addData(double value) {
		log |= (value > 0);
		max = Math.max(max, value);

		if (value != Double.NEGATIVE_INFINITY)
			min = Math.min(min, value);

		Node<Double> temp = new Node<Double>(value);
		Node<Double> next = values;

		while (next.hasNext() && next.getNext().getData() < temp.getData()) {
			next = next.getNext();
		}

		temp.setNext(next.getNext());
		next.setNext(temp);
	}

	/**
	 * @return the minimum value associated with the label.
	 */
	public double getMin() {
		return min;
	}

	/**
	 * @return the maximum value associated with the label.
	 */
	public double getMax() {
		return max;
	}

	/**
	 * @return the label associated with this data label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return true if there is at least one molecule with a positive value
	 *         associated with the label
	 */
	public boolean canLog() {
		return log;
	}

	/**
	 * @return whether to use a log scale or not.
	 */
	public boolean doLog() {
		return disType == 1;
	}

	/**
	 * @return whether to use the percentile or not.
	 */
	public boolean doPer() {
		return disType == 2;
	}

	/**
	 * Change how to display the data label.
	 * 
	 * @param i
	 *            the new way to display the data label.
	 */
	public void setDisType(int i) {
		disType = i % 3;
	}

	public String toString() {
		return "[ " + label + " <> " + min + " <> " + max + " ]";
	}

	/**
	 * @return the display type of the label.
	 */
	public int getDisType() {
		return disType;
	}

	/**
	 * Searches for a value in the list.
	 * 
	 * @param d
	 *            the value to search for.
	 * @return the index of the value.
	 */
	public int find(double d) {
		int i = 0;
		Node<Double> next = values;

		while (next.hasNext()) {
			next = next.getNext();

			if (d == next.getData())
				return i;

			i++;
		}

		return -1;
	}
}
