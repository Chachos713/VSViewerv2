package Start;

import javax.swing.JFileChooser;

import Util.Database;
import Util.KFileChooser;
import Views.CommentView;
import Views.MolGridView;
import Views.ScatterPlotView;
import Views.ThreeDimView;

public class DefaultStart {
	public DefaultStart(String file) throws Exception {
		Database d = null;

		if (file.isEmpty()) {
			file = openToStart();
		}

		d = Database.read(file);

		ThreeDimView tdv = new ThreeDimView(d);
		CommentView cv = new CommentView(d);
		MolGridView mgv = new MolGridView(d, tdv, cv);

		ScatterPlotView spv = new ScatterPlotView(d, cv, mgv, tdv);
		spv.run();
	}

	private String openToStart() {
		KFileChooser kfc = KFileChooser.create();

		int choice = kfc.open(null, 0);

		if (choice != JFileChooser.APPROVE_OPTION)
			System.exit(0);

		return kfc.getFile().toString();
	}
}
