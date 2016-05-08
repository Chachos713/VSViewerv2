package Util;

/**
 * A simple exception that is used to denote that the file ended to early. Helps
 * to differentiate between other excpetions while reading files.
 * 
 * @author Kyle Diller
 *
 */
@SuppressWarnings("serial")
public class EndFileException extends Exception {

	/**
	 * Creates an exception with a defined message.
	 * 
	 * @param string
	 *            the message for the exception
	 */
	public EndFileException(String string) {
		super(string);
	}

}
