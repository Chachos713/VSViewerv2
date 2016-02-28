package Protein;

import Molecule.Atom;

public class ProAtom extends Atom {
	private char chain;
	private String residue, name;

	public ProAtom(ProAtom other) {
		super(other);
		chain = other.chain;
		residue = other.residue;
		name = other.name;
	}

	public ProAtom(int t, int i, double x, double y, double z, char c,
			String r, String n) {
		super(t, i, 0, 0, x, y, z);
		chain = c;
		residue = r;
		name = n;
	}

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

	public char getChain() {
		return chain;
	}

	public String getResidue() {
		return residue;
	}
	
	public String getName(){
		return name;
	}
}
