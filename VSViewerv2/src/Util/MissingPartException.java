package Util;

/**
 * A simple exception to say there is missing information from the file. I use
 * this so that I can easily differentiate between different errors when reading
 * the files.
 * 
 * @author Kyle Diller
 *
 */
@SuppressWarnings("serial")
public class MissingPartException extends Exception {

	/**
	 * Just passes the message on to exception and let's exception deal with it.
	 * 
	 * @param string
	 *            the message for the exception
	 */
	public MissingPartException(String string) {
		super(string);
	}

}
