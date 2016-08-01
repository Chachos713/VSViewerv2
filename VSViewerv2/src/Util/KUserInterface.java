package Util;

import java.awt.event.MouseEvent;

import astex.MoleculeViewer;
import astex.UserInterface;

/**
 * This class is used to ensure that the pop up display does not hide/show any
 * molecules.
 * 
 * @author Kyle Diller
 *
 */
public class KUserInterface extends UserInterface {
	/**
	 * Creates an instance of the pop up display.
	 * 
	 * @param arg0
	 *            the molecule viewer for the super class.
	 */
	public KUserInterface(MoleculeViewer arg0) {
		super(arg0);
	}

	/**
	 * This calls the super mouse released if the button is not the right
	 * button.
	 * 
	 * @param e
	 *            the mouse event that triggers this event.
	 */
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() != MouseEvent.BUTTON3)
			super.mouseReleased(e);
	}
}
