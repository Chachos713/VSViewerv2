package Views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import Panels.ScatterPlotPanel;
import Util.Database;

public class ScatterPlotView implements MouseListener, MouseMotionListener,
		MouseWheelListener, ActionListener, WindowListener {
	private ScatterPlotPanel spp;
	private CommentView cv;
	private MolGridView mgv;
	private ThreeDimView tdv;
	private JFrame frame;

	public ScatterPlotView(Database d, CommentView c, MolGridView m,
			ThreeDimView t) {

		cv = c;
		mgv = m;
		tdv = t;

		JMenuBar menu = createMenuBar();

		spp = new ScatterPlotPanel(d, this);

		frame = new JFrame("Scatter Plot View");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(this);
		frame.add(spp);
		frame.setJMenuBar(menu);
		frame.pack();
	}

	private JMenuBar createMenuBar() {
		JMenuBar menu = new JMenuBar();

		JMenu file = new JMenu("File");
		file.setMnemonic('f');

		JMenuItem open = new JMenuItem("Open");
		open.setActionCommand("FO");
		open.setMnemonic('o');
		open.addActionListener(this);

		JMenuItem save = new JMenuItem("Save");
		save.setActionCommand("FS");
		save.setMnemonic('s');
		save.addActionListener(this);

		JMenuItem saveAs = new JMenuItem("Save As");
		saveAs.setActionCommand("FA");
		saveAs.setMnemonic('a');
		saveAs.addActionListener(this);

		JMenuItem close = new JMenuItem("Close");
		close.setActionCommand("FC");
		close.setMnemonic('c');
		close.addActionListener(this);

		file.add(open);
		file.add(save);
		file.add(saveAs);
		file.add(close);

		JMenu tools = new JMenu("Tools");
		tools.setMnemonic('t');

		JMenuItem display = new JMenuItem("Display Options");
		display.setActionCommand("TD");
		display.setMnemonic('d');
		display.addActionListener(this);

		JCheckBoxMenuItem invert = new JCheckBoxMenuItem("Invert Selection");
		invert.setActionCommand("TI");
		invert.setMnemonic('i');
		invert.addActionListener(this);

		JMenuItem search = new JMenuItem("Comment Search");
		search.setActionCommand("TC");
		search.setMnemonic('c');
		search.addActionListener(this);
		
		JMenuItem lsrl = new JMenuItem("Line of Best Fit");
		lsrl.setActionCommand("TL");
		lsrl.setMnemonic('l');
		lsrl.addActionListener(this);

		JMenuItem split = new JMenuItem("Split by Data");
		split.setActionCommand("TS");
		split.addActionListener(this);
		split.setMnemonic('s');

		JMenu screens = new JMenu("Turn Screens On/Off");
		screens.setMnemonic('t');

		JCheckBoxMenuItem popup = new JCheckBoxMenuItem("Popup Molecule");
		popup.addActionListener(this);
		popup.setActionCommand("TOP");
		popup.setSelected(true);
		popup.setMnemonic('p');

		JCheckBoxMenuItem molGrid = new JCheckBoxMenuItem("Grid View");
		molGrid.addActionListener(this);
		molGrid.setActionCommand("TOM");
		molGrid.setSelected(true);
		molGrid.setMnemonic('m');

		JCheckBoxMenuItem comment = new JCheckBoxMenuItem("Comment View");
		comment.addActionListener(this);
		comment.setActionCommand("TOC");
		comment.setSelected(true);
		comment.setMnemonic('c');

		JCheckBoxMenuItem three = new JCheckBoxMenuItem("3D View");
		three.addActionListener(this);
		three.setActionCommand("TO3");
		three.setSelected(true);
		three.setMnemonic('3');

		screens.add(popup);
		screens.add(molGrid);
		screens.add(comment);
		screens.add(three);

		tools.add(display);
		tools.add(search);
		tools.add(split);
		tools.add(lsrl);
		tools.add(invert);
		tools.add(screens);

		menu.add(file);
		menu.add(tools);

		return menu;
	}

	public void run() {
		frame.setVisible(true);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		spp.mouseDragged(arg0);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		spp.mouseMoved(arg0);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (spp.mouseClicked(e)) {
			int m = spp.getSelected();
			tdv.addMol(m);
			cv.setMolecule(m, false);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		spp.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (spp.mouseReleased(e))
			mgv.show();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		spp.mouseWheelMoved(arg0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		char start = e.getActionCommand().charAt(0);

		switch (start) {
		case 'F':
			if (spp.fileChoices(e.getActionCommand().charAt(1))) {
				cv.close();
				mgv.close();
				tdv.close();
				frame.dispose();
			}
			break;
		case 'T':
			spp.toolChoices(e.getActionCommand().substring(1));
		}
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		if (spp.fileChoices('C')) {
			cv.close();
			mgv.close();
			tdv.close();
			frame.dispose();
			System.exit(0);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}
}
