package Util;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * Creates a file chooser for the user to select files with. Uses the singleton
 * design pattern so that the user opens up the file choose in the same folder
 * the left off int.
 * 
 * @author Kyle Diller
 *
 */
public class KFileChooser {
	private static KFileChooser obj;

	/**
	 * Creates the file chooser for the user. Here is the main method for the
	 * singleton design pattern.
	 * 
	 * @return the file chooser object
	 */
	public static KFileChooser create() {
		if (obj == null) {
			obj = new KFileChooser();
		}

		return obj;
	}

	private JFileChooser jfc;

	/**
	 * Creates the file chooser. The constructor is private to keep in line with
	 * the singleton design pattern.
	 */
	private KFileChooser() {
		try {
			jfc = new JFileChooser(new File(".").getCanonicalFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		jfc.setAcceptAllFileFilterUsed(false);
	}

	/**
	 * Opens an open dialog
	 * 
	 * @param c
	 *            the compoenent to center around
	 * @param filters
	 *            the filters to filter the files by
	 * @return the return value of the file chooser
	 */
	public int open(Component c, FileFilter[] filters) {
		removeFilters();
		addFilters(filters);
		return jfc.showOpenDialog(c);
	}

	/**
	 * Opens the save dialog
	 * 
	 * @param c
	 *            the component to center around
	 * @param filters
	 *            the filters to open with
	 * @return the return value of the file chooser
	 */
	public int save(Component c, FileFilter[] filters) {
		removeFilters();
		addFilters(filters);
		return jfc.showSaveDialog(c);
	}

	/**
	 * Adds file filters to the file chooser
	 * 
	 * @param filters
	 *            the filters to add to the file chooser
	 */
	private void addFilters(FileFilter[] filters) {
		for (FileFilter f : filters) {
			jfc.addChoosableFileFilter(f);
		}
	}

	/**
	 * Removes all the file filters from the file chooser.
	 */
	private void removeFilters() {
		FileFilter[] filters = jfc.getChoosableFileFilters();

		for (FileFilter f : filters) {
			jfc.removeChoosableFileFilter(f);
		}
	}

	/**
	 * @return the selected file
	 */
	public File getFile() {
		return jfc.getSelectedFile();
	}
}
