package Molecule;

import java.text.DecimalFormat;
import java.util.ArrayList;

import Util.PeriodicTable;

/**
 * A simple class to store the information of atoms.
 * 
 * @author Kyle Diller
 *
 */
public class Atom {
	public final float x2d, y2d;
	public final float x3d, y3d, z3d;
	protected byte type, id;
	protected ArrayList<Atom> partners;

	/**
	 * Creates a 2D atom.
	 * 
	 * @param t
	 *            the type of the atom
	 * @param i
	 *            the index in the molecule
	 * @param x2
	 *            the x coordinate in 2D
	 * @param y2
	 *            the y coordinate in 2D
	 */
	public Atom(byte t, byte i, float x2, float y2) {
		this(t, i, x2, y2, 0, 0, 0);
	}

	/**
	 * Copies an atom to a new memory location
	 * 
	 * @param other
	 *            the atom copy
	 */
	public Atom(Atom other) {
		this(other.type, other.id, other.x2d, other.y2d, other.x3d, other.y3d,
				other.z3d);
	}

	/**
	 * Creates an atom in both 3 and 2 dimensional space
	 * 
	 * @param t
	 *            the atom type
	 * @param i
	 *            the index in the molecule
	 * @param x2
	 *            the 2D x coordinate
	 * @param y2
	 *            the 2D y coordinate
	 * @param x3
	 *            the 3D x coordinate
	 * @param y3
	 *            the 3D y coordiante
	 * @param z3
	 *            the 3D z coordinate
	 */
	public Atom(byte t, byte i, float x2, float y2, float x3, float y3,
			float z3) {
		type = t;
		id = i;
		x2d = x2;
		y2d = y2;
		x3d = x3;
		y3d = y3;
		z3d = z3;

		partners = new ArrayList<Atom>(4);
	}

	/**
	 * Adds an atom that this one is bonded to
	 * 
	 * @param i
	 *            the other atom
	 */
	public void addPartner(Atom i) {
		partners.add(i);
	}

	/**
	 * @return the list of atoms this one is bonded to
	 */
	public ArrayList<Atom> getBondedTo() {
		return partners;
	}

	/**
	 * @return the index of the molecule in the array
	 */
	public int getID() {
		return id;
	}

	/**
	 * @return whether this atom is a polar hydrogen or not
	 */
	public boolean isPolarH() {
		if (type == 1) {
			for (Atom a : partners) {
				if (a.type == 7 || a.type == 8 || a.type == 16)
					return true;
			}

			return false;
		}

		return true;
	}

	/**
	 * @return the atomic number of this atom
	 */
	public int getType() {
		return type;
	}

	/**
	 * The distance squared between this atom and a point
	 * 
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param z
	 *            the z coordinate
	 * @return the distance squared
	 */
	public double disSq(double x, double y, double z) {
		return (x - x3d) * (x - x3d) + (y - y3d) * (y - y3d) + (z - z3d)
				* (z - z3d);
	}

	/**
	 * The distance squared between this atom and another atom
	 * 
	 * @param a
	 *            the other atom
	 * @return the distance squared
	 */
	public double disSq(Atom a) {
		return disSq(a.x3d, a.y3d, a.z3d);
	}

	/**
	 * @return the fingerprint value for the strucutral fingerprint of the
	 *         molecule
	 */
	public int getFPType() {
		switch (type) {
		case 1:
			return -1;
		case 8:
			return 3;
		case 7:
			return 2;
		case 6:
			if (partners.size() == 4)
				return 0;
			else
				return 1;
		case 16:
			return 4;
		default:
			return 5;
		}
	}

	/**
	 * Generates a sd file line for the atom
	 * 
	 * @param d3
	 *            whether the sd file is 3D or 2D
	 * @return a string for a sd file for this atom
	 */
	public String getMolLine(boolean d3) {
		String line;
		String field;
		int ns;
		int i;

		DecimalFormat format = new DecimalFormat("0.0000");
		String type = PeriodicTable.getElement(this.type);

		line = "";
		if (d3) {// Outputs as a 3D file
			field = format.format(x3d);
			ns = 10 - field.length();
			for (i = 0; i < ns; i++) {
				line += " ";
			}
			line += field;

			field = format.format(y3d);
			ns = 10 - field.length();
			for (i = 0; i < ns; i++) {
				line += " ";
			}
			line += field;

			field = format.format(z3d);
			ns = 10 - field.length();
			for (i = 0; i < ns; i++) {
				line += " ";
			}
			line += field;
			line += " ";
			line += type;
			ns = 3 - type.length();
			for (i = 0; i < ns; i++) {
				line += " ";
			}
		} else {// OutPuts as a 2D file
			field = format.format(x2d);
			ns = 10 - field.length();
			for (i = 0; i < ns; i++) {
				line += " ";
			}
			line += field;

			field = format.format(y2d);
			ns = 10 - field.length();
			for (i = 0; i < ns; i++) {
				line += " ";
			}
			line += field;

			field = format.format(0);
			ns = 10 - field.length();
			for (i = 0; i < ns; i++) {
				line += " ";
			}
			line += field;

			line += " ";
			line += type;
			ns = 3 - type.length();
			for (i = 0; i < ns; i++) {
				line += " ";
			}
		}

		line += " 0  0  0  0  0  0  0  0  0  0  0  0";
		return line;
	}
}
