package Util;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MolPanel extends JPanel {
	public static boolean deletable;

	private JCheckBox display, surface;
	private JButton remove;

	public MolPanel(String name, ActionListener a) {
		display = new JCheckBox(name);
		display.addActionListener(a);
		
		surface = new JCheckBox("Surface");
		surface.addActionListener(a);

		remove = new JButton("X");
		remove.addActionListener(a);
		remove.setVisible(deletable);

		this.add(display);
		this.add(remove);
		this.add(surface);
		
		//name = name.replace('-', '_');
		this.setName(name);
	}

	public boolean isRemove(Object source) {
		return remove == source;
	}

	public boolean isCheck(Object source) {
		return display == source || surface == source;
	}

	public boolean isSelected() {
		return display.isSelected();
	}
	
	public boolean doSurface(){
		return surface.isSelected();
	}

	public void setSelected(boolean b) {
		display.setSelected(b);
	}

	public JButton getButton() {
		return remove;
	}

	public boolean isSurface(Object source) {
		return surface == source;
	}
}
