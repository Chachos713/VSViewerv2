package Molecule;

/**
 * Stores the information for a given data point.
 * 
 * @author Kyle Diller
 *
 */
public class DataPt {
	/**
	 * Creates a data point based on a given string
	 * 
	 * @param s
	 *            the string to parse
	 * @return the data point from that string
	 */
	public static DataPt read(String s) {
		String[] split = s.split(" ");
		double v = Double.parseDouble(split[0]);

		return new DataPt(v, split[1]);
	}

	private String label;
	private double value;

	/**
	 * Creates a data point with a given value and label
	 * 
	 * @param v
	 *            the value of the data point
	 * @param l
	 *            the label of the data point
	 */
	public DataPt(double v, String l) {
		label = new String(l);
		value = v;
	}

	/**
	 * Creates a copy of a data point
	 * 
	 * @param other
	 *            the data point to copy
	 */
	public DataPt(DataPt other) {
		this(other.value, other.label);
	}

	/**
	 * @return the value of the data point
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @return the label for the dats point
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the values of the data point in the format it will be read
	 */
	public String toString() {
		return value + " " + label;
	}
}