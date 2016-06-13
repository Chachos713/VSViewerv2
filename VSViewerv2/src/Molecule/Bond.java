package Molecule;

import astex.Color32;

/**
 * Stores the atoms that are involved in the bond. Extends TransformGroup so
 * that it deals with the two end points of the atoms.
 * 
 * @author Kyle Diller
 *
 */
public class Bond {

	private Atom a1, a2;
	private int type; // Needs to be stored but might not be used

	/**
	 * Creates a bond
	 * 
	 * @param a
	 *            the first atom
	 * @param b
	 *            the second atom
	 * @param t
	 *            the bond type
	 * @param is3D
	 *            determine to draw the cylinder
	 * @param color
	 *            the color of the bond if it is part of a protein
	 * @param pro
	 *            is parte of a protein
	 */
	public Bond(Atom a, Atom b, int t, boolean is3D, float[] color, boolean pro) {
		a1 = a;
		a2 = b;
		type = t;

		a1.addPartner(a2);
		a2.addPartner(a1);
	}

	/**
	 * Copies a bond
	 * 
	 * @param other
	 *            the bond to copy
	 * @param is3D
	 *            drar the 3D structure or not
	 */
	public Bond(Bond other, boolean is3D) {
		this(other.a1, other.a2, other.type, is3D, null, false);
	}

	/**
	 * @return the first atom
	 */
	public Atom getA1() {
		return a1;
	}

	/**
	 * @return the second atom
	 */
	public Atom getA2() {
		return a2;
	}

	/**
	 * @return the bond type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @return the string representation of the bond in a vsv file
	 */
	public String toString() {
		return (a1.getID() + 1) + ":" + (a2.getID() + 1) + ":" + type;
	}

	/**
	 * @return whether to draw the bond or not
	 */
	public boolean doDraw() {
		return a1.isPolarH() || a2.isPolarH();
	}

	/**
	 * Adds this bond to the open astex molecule.
	 * 
	 * @param asMol
	 *            the astex molecule to add this bond to.
	 */
	public void addToAsMol(astex.Molecule asMol) {
		astex.Atom temp1 = asMol.addAtom();
		astex.Atom temp2 = asMol.addAtom();

		temp1.setElement(a1.getType());
		temp1.x = a1.x3d;
		temp1.y = a1.y3d;
		temp1.z = a1.z3d;

		if (a1.getType() == 6) {
			temp1.setColor(Color32.magenta);
		}

		temp2.setElement(a2.getType());
		temp2.x = a2.x3d;
		temp2.y = a2.y3d;
		temp2.z = a2.z3d;

		if (a2.getType() == 6) {
			temp2.setColor(Color32.magenta);
		}

		asMol.addBond(temp1, temp2);
	}
}
