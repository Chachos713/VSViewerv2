package Util;

import java.io.File;
import java.util.Arrays;

import javax.swing.filechooser.FileFilter;

public class Filter extends FileFilter {
	private String[] okFileExt;

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
