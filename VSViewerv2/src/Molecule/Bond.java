package Molecule;

/**
 * Stores the atoms that are involved in the bond. Extends TransformGroup so
 * that it deals with the two end points of the atoms.
 * 
 * @author Kyle Diller
 *
 */
public class Bond {

	private Atom a1, a2;
	private byte type; // Needs to be stored but might not be used

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
	 */
	public Bond(Atom a, Atom b, byte t) {
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
	public Bond(Bond other) {
		this(other.a1, other.a2, other.type);
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
	public void addToAsMol(astex.Molecule mol) {
		mol.addBond(a1.getAsMol(mol), a2.getAsMol(mol));
	}
}
