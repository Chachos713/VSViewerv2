package Molecule;

import java.text.DecimalFormat;
import java.util.ArrayList;

import Util.PeriodicTable;

public class Atom {
	public final double x2d, y2d;
	public final double x3d, y3d, z3d;
	protected int type, id;
	protected ArrayList<Atom> partners;

	public Atom(int t, int i, double x2, double y2) {
		this(t, i, x2, y2, 0, 0, 0);
	}

	public Atom(Atom other) {
		this(other.type, other.id, other.x2d, other.y2d, other.x3d, other.y3d,
				other.z3d);
	}

	public Atom(int t, int i, double x2, double y2, double x3, double y3,
			double z3) {
		type = t;
		id = i;
		x2d = x2;
		y2d = y2;
		x3d = x3;
		y3d = y3;
		z3d = z3;

		partners = new ArrayList<Atom>();
	}

	public void addPartner(Atom i) {
		partners.add(i);
	}

	public ArrayList<Atom> getBondedTo() {
		return partners;
	}

	public int getID() {
		return id;
	}

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

	public int getType() {
		return type;
	}

	public double disSq(double x, double y, double z) {
		return (x - x3d) * (x - x3d) + (y - y3d) * (y - y3d) + (z - z3d)
				* (z - z3d);
	}

	public double disSq(Atom a) {
		return disSq(a.x3d, a.y3d, a.z3d);
	}

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
