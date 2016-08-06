package Util;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Util.Calculator;
import astex.MoleculeViewer;
import astex.Renderer;

/**
 * Changes the lighting on the 3D view.
 * 
 * @author Kyle Diller
 *
 */
public class Lighting implements WindowListener, ChangeListener {
	private Renderer ren;
	private MoleculeViewer view;
	private JSlider front, back;
	private JLabel fValue, bValue;
	private Dialog frame;

	/**
	 * Creates the form.
	 * 
	 * @param m
	 *            the molecule viewer to change.
	 * @param owner
	 *            the frame that owns this.
	 */
	public Lighting(MoleculeViewer m, Frame owner) {
		view = m;
		ren = m.moleculeRenderer.renderer;
		frame = new Dialog(owner);

		front = new JSlider(5, 1000, (int) (ren.getFrontClip() * 10));
		front.setMajorTickSpacing(100);
		front.setPaintTicks(true);
		front.addChangeListener(this);

		back = new JSlider(5, 1000, (int) (ren.getBackClip() * -10));
		back.setMajorTickSpacing(100);
		back.setPaintTicks(true);
		back.addChangeListener(this);

		fValue = new JLabel("" + ren.getFrontClip());
		bValue = new JLabel("" + ren.getBackClip());

		JPanel frontPanel = new JPanel(new BorderLayout());
		JPanel backPanel = new JPanel(new BorderLayout());

		frontPanel.add(BorderLayout.WEST, new JLabel("Front:"));
		frontPanel.add(BorderLayout.CENTER, front);
		frontPanel.add(BorderLayout.EAST, fValue);

		backPanel.add(BorderLayout.WEST, new JLabel("Back:"));
		backPanel.add(BorderLayout.CENTER, back);
		backPanel.add(BorderLayout.EAST, bValue);

		Box vert = Box.createVerticalBox();
		vert.add(frontPanel);
		vert.add(backPanel);

		frame.add(vert);
		frame.addWindowListener(this);
		frame.pack();
	}

	/**
	 * Show the dialog.
	 */
	public void show() {
		frame.setVisible(true);
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		double f = front.getValue() / 10;
		Calculator.round(f, 1);
		double b = back.getValue() / -10;
		Calculator.round(b, 1);

		fValue.setText("" + f);
		bValue.setText("" + b);

		ren.setFrontClip(f, false);
		ren.setBackClip(b, false);

		view.dirtyRepaint();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		frame.setVisible(false);
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
