package Util;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

public class KFileChooser {
	private static KFileChooser obj;

	public static KFileChooser create() {
		if (obj == null) {
			obj = new KFileChooser();
		}

		return obj;
	}

	private JFileChooser jfc;
	private Filter pdb, vsv, png, sdf;

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

	public File getFile() {
		return jfc.getSelectedFile();
	}
}
