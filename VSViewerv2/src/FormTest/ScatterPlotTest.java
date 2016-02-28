package FormTest;

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

public class ScatterPlotTest implements MouseMotionListener, MouseListener,
		MouseWheelListener, ActionListener, WindowListener {
	public static void main(String[] args) {
		new ScatterPlotTest(args[0]);
	}

	private JFrame frame;
	private ScatterPlotPanel sp;

	public ScatterPlotTest(String file) {
		Database d;
		try {
			d = Database.read(file);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		frame = new JFrame("Scatter Plot");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(this);

		sp = new ScatterPlotPanel(d, this);

		frame.add(sp);
		frame.setJMenuBar(createMenu());
		frame.pack();
		frame.setVisible(true);
	}

	private JMenuBar createMenu() {
		JMenuBar menu = new JMenuBar();

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

		JCheckBoxMenuItem popup = new JCheckBoxMenuItem("Popup Molecule");
		popup.addActionListener(this);
		popup.setActionCommand("TOP");
		popup.setSelected(true);
		popup.setMnemonic('p');

		tools.add(display);
		tools.add(search);
		tools.add(split);
		tools.add(lsrl);
		tools.add(invert);
		tools.add(popup);

		menu.add(tools);

		return menu;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		sp.mouseClicked(arg0);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		sp.mousePressed(arg0);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		sp.mouseReleased(arg0);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		sp.mouseDragged(arg0);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		sp.mouseMoved(arg0);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		sp.mouseWheelMoved(arg0);
	}

	public void actionPerformed(ActionEvent e) {
		sp.toolChoices(e.getActionCommand().substring(1));
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		if (sp.fileChoices('C')) {
			frame.dispose();
			System.exit(0);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
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
