package Util;

import java.io.File;
import java.util.Arrays;

import javax.swing.filechooser.FileFilter;

/**
 * A generic filter for the file chooser
 * 
 * @author Kyle Diller
 *
 */
public class Filter extends FileFilter {
	private String[] okFileExt;

	/**
	 * Creates a filte filter with some preset file extensions
	 * 
	 * @param ext
	 *            the array of acceptable extensions
	 */
	public Filter(String[] ext) {
		super();
		okFileExt = ext;
	}

	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		}

		for (String ext : okFileExt) {
			if (file.getName().toLowerCase().endsWith(ext)) {
				return true;
			}
		}

		return false;
	}

	public String getDescription() {
		return Arrays.toString(okFileExt);
	}
}
