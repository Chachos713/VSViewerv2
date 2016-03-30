package Start;

import java.io.File;

import FormTest.CommentTest;
import FormTest.GridViewTest;
import FormTest.ScatterPlotTest;
import FormTest.ThreeDimTest;

/**
 * The main entry point to the program. Determines what to do based on the
 * command line arguements are.
 * 
 * Expects the arguements to be default file to open, then te=he form to start
 * with. Does provide default values for both.
 * 
 * @author Kyle Diller
 *
 */
public class Runner {
	/**
	 * The main entry point into the program
	 * 
	 * @param args
	 *            the list of command line arguements.
	 * @throws Exception
	 *             if there is a problem with reading the default file.
	 */
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			// No Arguements are provided
			// Open with no file
			new DefaultStart("");
		} else if (args.length == 1) {
			// A default file is provided
			// Checks that the file exists and is a vsv
			if (!checkFile(args[0], true)) {
				errMessage();
			}

			new DefaultStart(args[0]);
		} else if (args.length == 2) {
			// A default file is proided as well as a for to look at
			// Checks that the file exists
			if (!checkFile(args[0], false)) {
				errMessage();
			}

			int selected = -1;

			try {
				selected = Integer.parseInt(args[1]);
			} catch (Exception e) {
				errMessage();
			}

			// Uses the test forms to show specific forms.
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
			// Any other number of arguements are provided
			errMessage();
		}
	}

	/**
	 * Checks for if the file is okay to read
	 * 
	 * @param file
	 *            The file to check.
	 * @param def
	 *            Check if the file is .vsv.
	 * @return True if the file exists and the file is a .vsv if needed.
	 */
	private static boolean checkFile(String file, boolean def) {
		File f = new File(file);
		// Part after && is logically equivalent to def -> vsv
		return f.exists() && (!def || file.endsWith(".vsv"));
	}

	/**
	 * Outputs the error message, then ends the program. Occurs when the
	 * arguements for the program are wrong.
	 */
	private static void errMessage() {
		System.err
				.println("usage: java -jar VSViewer3D.jar [filename = \"\"] [form_to_open = 0]");
		System.err.println("filename is a file that exists");
		System.err
				.println("The grid view(2) and three dimension view(4) are the only forms to display sd files");
		System.err.println("form_to_open is an integer from 0 to 4");

		System.exit(0);
	}
}
