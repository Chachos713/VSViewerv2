package Protein;

import Molecule.Atom;

/**
 * An extension of atom to allow for it's use in proteins. Keeps track of it's
 * chain, residue, and name.
 * 
 * @author Kyle Diller
 *
 */
public class ProAtom extends Atom {
	private char chain;
	private String residue, name;

	/**
	 * Copies an atom to a new memory location
	 * 
	 * @param other
	 *            the atom to copy
	 */
	public ProAtom(ProAtom other) {
		super(other);
		chain = other.chain;
		residue = other.residue;
		name = other.name;
	}

	/**
	 * Creates a new atom
	 * 
	 * @param t
	 *            element
	 * @param i
	 *            the index
	 * @param x
	 *            x component of 3D coordinateds
	 * @param y
	 *            y component of 3D coordinates
	 * @param z
	 *            z component of 3D coordinates
	 * @param c
	 *            the chain
	 * @param r
	 *            the residue
	 * @param n
	 *            it's name
	 */
	public ProAtom(int t, int i, double x, double y, double z, char c,
			String r, String n) {
		super(t, i, 0, 0, x, y, z);
		chain = c;
		residue = r;
		name = n;
	}

	/**
	 * @return the radius of the atom to help find it's bonds
	 */
	public double getBondLength() {
		switch (super.type) {
		case 6:
			return 0.75;
		case 1:
			return 0.33;
		case 7:
			return 0.7;
		case 8:
			return 0.66;
		case 9:
			return 0.6;
		default:
			return 1.01;
		}
	}

	/**
	 * @return the chain
	 */
	public char getChain() {
		return chain;
	}

	/**
	 * @return the residue
	 */
	public String getResidue() {
		return residue;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
