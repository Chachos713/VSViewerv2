package Util;

/**
 * A simple exception that is used to say that the file did not include and
 * limits. Helps differentiate between the different errors in file reading.
 * 
 * @author Kyle Diller
 *
 */
@SuppressWarnings("serial")
public class LimitsNotDefinedException extends Exception {

	/**
	 * Creates an exception with a message.
	 * 
	 * @param s
	 *            the message for the exception
	 */
	public LimitsNotDefinedException(String s) {
		super(s);
	}
}
