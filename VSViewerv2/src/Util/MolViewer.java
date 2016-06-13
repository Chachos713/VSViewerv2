package Util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import astex.MoleculeViewer;

/**
 * An extension of the astex molecule viewer that allows for more general
 * control of the 3D view.
 * 
 * @author Kyle Diller
 *
 */
@SuppressWarnings("serial")
public class MolViewer extends MoleculeViewer implements MouseWheelListener {
	private MouseEvent mousePressedEvent = null;
	private int button;

	/**
	 * Creates the 3D viewer.
	 */
	public MolViewer() {
		super();
		addMouseWheelListener(this);
	}

	/**
	 * Deals with the zooming in and out on the 3D view.
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		int dy = e.getWheelRotation();
		moleculeRenderer.renderer.applyZoom(dy * 0.25);

		dirtyRepaint();
	}

	/**
	 * Allows for the mouse to rotate or move the view.
	 */
	public void mouseDragged(MouseEvent e) {
		int dy = e.getY() - mousePressedEvent.getY();
		int dx = e.getX() - mousePressedEvent.getX();

		if (button == MouseEvent.BUTTON3)
			moleculeRenderer.translateCenter(dx, dy);
		else
			super.mouseDragged(e);

		mousePressedEvent = e;
		dirtyRepaint();
	}

	/**
	 * Gets where the mouse was clicked.
	 */
	public void mousePressed(MouseEvent e) {
		mousePressedEvent = e;
		button = e.getButton();
		super.mousePressed(e);
	}

	/**
	 * Gets where the mouse is released.
	 */
	public void mouseReleased(MouseEvent e) {
		mousePressedEvent = null;
		super.mouseReleased(e);
		button = 0;
	}
}
