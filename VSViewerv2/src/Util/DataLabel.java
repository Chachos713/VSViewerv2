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
	private boolean log, doLog;

	/**
	 * Creates a data label with a given name and starting value for log.
	 * 
	 * @param l
	 *            the name of the label
	 * @param p
	 *            the starting value for log
	 */
	public DataLabel(String l, boolean p) {
		log = p;
		label = l;
		doLog = false;
	}

	/**
	 * Checks if the new point helps to give this label a log scale
	 * 
	 * @param pos
	 *            whether the value is positive or not
	 */
	public void addData(boolean pos) {
		log |= pos;
	}

	/**
	 * Tells the label whether to use a log scale on this data
	 * 
	 * @param p
	 *            whether to use a log scale or not
	 */
	public void setDoLog(boolean p) {
		doLog = p;
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
	 * @return whether to use a log scale or not
	 */
	public boolean doLog() {
		return doLog;
	}
}
