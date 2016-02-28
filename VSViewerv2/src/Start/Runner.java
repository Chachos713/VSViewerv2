package Start;

import java.io.File;

import FormTest.CommentTest;
import FormTest.GridViewTest;
import FormTest.ScatterPlotTest;
import FormTest.ThreeDimTest;

public class Runner {
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			new DefaultStart("");
		} else if (args.length == 1) {
			if (!checkFile(args[0], true)) {
				errMessage();
			}

			new DefaultStart(args[0]);
		} else if (args.length == 2) {
			if (!checkFile(args[0], false)) {
				errMessage();
			}

			int selected = -1;

			try {
				selected = Integer.parseInt(args[1]);
			} catch (Exception e) {
				errMessage();
			}

			switch (selected) {
			case 0:// Default View
				break;
			case 1:// Scatter Plot
				if (args[0].endsWith(".vsv"))
					new ScatterPlotTest(args[0]);
				else
					errMessage();
				break;
			case 2:// Grid View
				new GridViewTest(args[0]);
				break;
			case 3:// Comments View
				if (args[0].endsWith(".vsv"))
					new CommentTest(args[0]);
				else
					errMessage();
				break;
			case 4:// 3D View
				new ThreeDimTest(args[0]);
				break;
			case 5:// Data Table
			default:
				errMessage();
			}
		} else {
			errMessage();
		}
	}

	private static boolean checkFile(String file, boolean def) {
		File f = new File(file);
		return f.exists() && (file.endsWith(".vsv") || !def);
	}

	private static void errMessage() {
		System.err
				.println("usage: java -jar VSViewer3D.jar [filename = \"\"] [form_to_open = 0]");
		System.err.println("filename is a file that exists");
		System.err
				.println("The grid view(2) and three dimension view(4) are the only forms to display sd files");
		System.err.println("form_to_open is an integer from 0 to 5");

		System.exit(0);
	}
}
