package Start;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import Util.Database;
import Util.Filter;
import Util.KFileChooser;
import Views.AstexView;
import Views.CommentView;
import Views.MolGridView;
import Views.ScatterPlotView;
import Views.ThreeDimView;

/**
 * Starts the program with an initial file
 * 
 * @author Kyle Diller
 *
 */
public class DefaultStart {
	/**
	 * Starts the program with a default file.
	 * 
	 * @param file
	 *            the file toe read. If file = "", then open up a file chooser
	 *            for the user to select a file.
	 * @throws Exception
	 *             if there is a problem reading the file.
	 */
	public DefaultStart(String file) throws Exception {
		Database d = null;

		// Checks if there is a file provided
		if (file.isEmpty()) {
			file = openToStart();
		}

		// Creates the database ad all the forms
		d = Database.read(file);

		ThreeDimView tdv = new ThreeDimView(d);
		CommentView cv = new CommentView(d);
		AstexView av = new AstexView(d);
		MolGridView mgv = new MolGridView(d, tdv, av, cv);

		ScatterPlotView spv = new ScatterPlotView(d, cv, mgv, tdv, av);

		// Starts the program with the scatter plot view.
		spv.run();
	}

	/**
	 * Opens the file chooser for the user to select the default file.
	 * 
	 * Exits the program if the selected file is not approved.
	 * 
	 * @return the selected file.
	 */
	private String openToStart() {
		KFileChooser kfc = KFileChooser.create();

		String[] v = { ".vsv" };
		FileFilter[] vsv = { new Filter(v) };

		int choice = kfc.open(null, vsv);

		if (choice != JFileChooser.APPROVE_OPTION)
			System.exit(0);

		return kfc.getFile().toString();
	}
}
