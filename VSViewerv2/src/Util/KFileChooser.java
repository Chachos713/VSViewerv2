package Util;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

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
	private Filter pdb, vsv, png, sdf;

	/**
	 * Creates the file chooser. The constructor is private to keep in line with
	 * the singleton design pattern.
	 */
	private KFileChooser() {
		jfc = new JFileChooser();

		String[] ext = { ".vsv" };
		String[] ext2 = { ".pdb" };
		String[] ext3 = { ".png" };
		String[] ext4 = { ".sdf" };
		vsv = new Filter(ext);
		pdb = new Filter(ext2);
		png = new Filter(ext3);
		sdf = new Filter(ext4);
		jfc.setAcceptAllFileFilterUsed(false);
	}

	/**
	 * Displays an open dialog for the user to select a file.
	 * 
	 * @param c
	 *            the component to center the file chooser around
	 * @param type
	 *            whether the user is selecting a pdb file or a vsv file
	 * @return whether the user selected ok or cancel
	 */
	public int open(Component c, int type) {
		if (type == 1) {
			jfc.addChoosableFileFilter(pdb);
			jfc.removeChoosableFileFilter(vsv);
		} else if (type == 0) {
			jfc.removeChoosableFileFilter(pdb);
			jfc.addChoosableFileFilter(vsv);
		}
		jfc.removeChoosableFileFilter(png);
		jfc.removeChoosableFileFilter(sdf);

		return jfc.showOpenDialog(c);
	}

	/**
	 * Displays a save dialog for the user
	 * 
	 * @param c
	 *            the component to center the file chooser around
	 * @param type
	 *            whether the file being saved is a vsv or a png file
	 * @return whether the user selected ok or cancel on the file chooser
	 */
	public int save(Component c, int type) {
		jfc.removeChoosableFileFilter(pdb);
		jfc.removeChoosableFileFilter(vsv);
		jfc.removeChoosableFileFilter(png);

		if (type == 0 || type == 1) {
			jfc.addChoosableFileFilter(vsv);
		} else if (type == 2) {
			jfc.addChoosableFileFilter(png);
		}

		if (type == 1) {
			jfc.addChoosableFileFilter(sdf);
		}

		return jfc.showSaveDialog(c);
	}

	/**
	 * @return the selected file
	 */
	public File getFile() {
		return jfc.getSelectedFile();
	}
}
