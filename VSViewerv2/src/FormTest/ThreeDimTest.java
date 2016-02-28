package FormTest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import Panels.ThreeDimPanel;
import Util.Database;

public class ThreeDimTest implements ActionListener {
	public static void main(String[] args) {
		new ThreeDimTest(args[0]);
	}

	private JFrame frame;
	private ThreeDimPanel tdp;

	public ThreeDimTest(String file) {
		Database data;
		try {
			data = Database.read(file);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		frame = new JFrame("3D Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		tdp = new ThreeDimPanel(data, this, true, false);

		frame.add(tdp);
		frame.setJMenuBar(createMenu());
		frame.pack();
		frame.setVisible(true);
	}

	public JMenuBar createMenu() {
		JMenuBar menu = new JMenuBar();

		JMenu file = new JMenu("File");
		file.setMnemonic('f');

		JMenuItem save = new JMenuItem("Save");
		save.setActionCommand("S");
		save.setMnemonic('s');
		save.addActionListener(this);

		JMenuItem open = new JMenuItem("Open");
		open.setActionCommand("O");
		open.setMnemonic('o');
		open.addActionListener(this);

		file.add(open);
		file.add(save);

		menu.add(file);

		return menu;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		tdp.actionPerformed(e);
	}
}
