package Molecule;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

import Util.PeriodicTable;

import com.sun.j3d.utils.geometry.Cylinder;

/**
 * Stores the atoms that are involved in the bond. Extends TransformGroup so
 * that it deals with the two end points of the atoms.
 * 
 * @author Kyle Diller
 *
 */
public class Bond extends TransformGroup {
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

		if (is3D) {
			createBond(color, pro);
		}
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

	private void createBond(float[] color, boolean pro) {
		if (!a1.isPolarH() || !a2.isPolarH())
			return;

		Vector3f v1 = new Vector3f();
		Vector3f v2 = new Vector3f();

		ColoringAttributes ca1 = getAtomColor(a1, pro);
		v1.x = (float) a1.x3d;
		v1.y = (float) a1.y3d;
		v1.z = (float) a1.z3d;
		v1 = convertCoord(v1);

		ColoringAttributes ca2 = getAtomColor(a2, pro);
		v2.x = (float) a2.x3d;
		v2.y = (float) a2.y3d;
		v2.z = (float) a2.z3d;
		v2 = convertCoord(v2);

		if (color != null) {
			ca1.setColor(color[0], color[1], color[2]);
			ca2.setColor(color[0], color[1], color[2]);
		}

		TransformGroup objTrans1 = createCylinder(v1, v2, (float) .01, ca1);
		this.addChild(objTrans1);

		TransformGroup objTrans2 = createCylinder(v2, v1, (float) .01, ca2);
		this.addChild(objTrans2);
	}

	/**
	 * Normalizes a vector
	 * 
	 * @param v1
	 *            the vector to noramlize
	 * @return the normalized vector
	 */
	private Vector3f convertCoord(Vector3f v1) {
		Vector3f temp = new Vector3f();

		temp.x = v1.x / 10;
		temp.y = v1.y / 10;
		temp.z = v1.z / 10;

		return temp;
	}

	/**
	 * Gets the color for a given atom
	 * 
	 * @param a
	 *            the atom to get the color of
	 * @param pro
	 *            whether the atom is part of a protein or not
	 * @return the color of a given atom
	 */
	private ColoringAttributes getAtomColor(Atom a, boolean pro) {
		Color c = PeriodicTable.getColor(a.getType(), pro);
		ColoringAttributes ca = new ColoringAttributes(new Color3f(c),
				ColoringAttributes.NICEST);

		return ca;
	}

	/**
	 * Creates a cylinder to represent the bond
	 * 
	 * @param v1
	 *            the vector of atom 1
	 * @param v2
	 *            the vector of atom 2
	 * @param rad
	 *            the radius of the cylinder
	 * @param ca
	 *            the color of the bond section
	 * @return a cylinder for the bond
	 */
	private TransformGroup createCylinder(Vector3f v1, Vector3f v2, float rad,
			ColoringAttributes ca) {
		float ht;
		float vx, vy, vz;
		float c, s;
		float ux, uy, uz;

		Vector3f v = new Vector3f((float) ((3.0 * v1.x + v2.x) / 4.0),
				(float) ((3.0 * v1.y + v2.y) / 4.0),
				(float) ((3.0 * v1.z + v2.z) / 4.0));
		Matrix3f A = new Matrix3f();

		vx = (v1.x - v2.x);
		vy = (v1.y - v2.y);
		vz = (v1.z - v2.z);

		ht = (float) Math.sqrt((double) (vx * vx + vy * vy + vz * vz));
		vx /= ht;
		vy /= ht;
		vz /= ht;

		c = vy; // original vector is in the y direction
		ux = vz;
		uy = 0.0f;
		uz = -vx;
		s = (float) Math.sqrt((double) (ux * ux + uy * uy + uz * uz));
		ux /= s;
		uy /= s;
		uz /= s;

		A.m00 = ux * ux * (1.0f - c);
		A.m01 = ux * uy * (1.0f - c);
		A.m02 = ux * uz * (1.0f - c);

		A.m10 = uy * ux * (1.0f - c);
		A.m11 = uy * uy * (1.0f - c);
		A.m12 = uy * uz * (1.0f - c);

		A.m20 = uz * ux * (1.0f - c);
		A.m21 = uz * uy * (1.0f - c);
		A.m22 = uz * uz * (1.0f - c);

		A.m00 += c;
		A.m11 += c;
		A.m22 += c;

		A.m01 -= (uz * s);
		A.m10 += (uz * s);

		A.m02 += uy * s;
		A.m20 -= uy * s;

		A.m12 -= (ux * s);
		A.m21 += (ux * s);

		// trans.set(v);
		Transform3D trans = new Transform3D(A, v, 1.0f);

		Color3f color = new Color3f();
		ca.getColor(color);
		Material m = new Material();
		Appearance ap = new Appearance();
		m.setAmbientColor(color);
		m.setDiffuseColor(color);
		ap.setMaterial(m);
		ap.setColoringAttributes(ca);

		TransformGroup tG = new TransformGroup(trans);
		tG.addChild(new Cylinder(rad, (ht / 2.0f), Cylinder.GENERATE_NORMALS,
				ap));
		return tG;
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
}
