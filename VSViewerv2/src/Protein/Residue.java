package Protein;

import java.util.ArrayList;

/**
 * A simple class to keep track of atoms contained within a residue.
 * 
 * @author Kyle Diller
 *
 */
public class Residue {
	private char chain;
	private String resNum;
	private ArrayList<Integer> myAtm;
	private ArrayList<String> atomName;

	/**
	 * Creates an empty residue.
	 */
	public Residue() {
		chain = ' ';
		resNum = "";
		myAtm = new ArrayList<Integer>();
		atomName = new ArrayList<String>();
	}

	/**
	 * Creates a residue with a given chain and number.
	 * 
	 * @param c
	 *            the chain
	 * @param r
	 *            the residue number
	 */
	public Residue(char c, String r) {
		chain = c;
		resNum = new String(r);
		myAtm = new ArrayList<Integer>();
		atomName = new ArrayList<String>();
	}

	/**
	 * @return a copy of the current residue
	 */
	public Residue copy() {
		Residue r = new Residue();
		r.chain = chain;
		r.resNum = new String(resNum);
		r.myAtm = new ArrayList<Integer>(myAtm);
		r.atomName = new ArrayList<String>(atomName);
		return r;
	}

	/**
	 * @return the description of the residue <br>
	 *         This is just the chain followed by the number
	 */
	public String getResidueDesc() {
		return chain + resNum;
	}

	/**
	 * Gets the index of an atom with a given name
	 * 
	 * @param aName
	 *            the name of the atom to look for
	 * @return the index of that atom
	 */
	public Integer getAtomIndex(String aName) {
		Integer a;

		for (a = 0; a < myAtm.size(); a++) {
			if (aName.equals(atomName.get(a))) {
				return myAtm.get(a);
			}
		}
		return -1;
	}

	/**
	 * Adds an atom with a given name and index
	 * 
	 * @param a
	 *            the index of the atom
	 * @param aName
	 *            the name of the atom
	 */
	public void addAtom(int a, String aName) {
		myAtm.add(a);
		atomName.add(aName);
	}

	/**
	 * Checks if an atom belongs to a given residue.
	 * 
	 * @param c
	 *            the chain of the atom
	 * @param r
	 *            the residue of the atom
	 * @return true if the chain of the atom and residue of the atom match this
	 *         residue
	 */
	public boolean doIBelong(char c, String r) {
		if (chain != c) {
			return false;
		}
		if (!resNum.equals(r)) {
			return false;
		}

		return true;
	}

	/**
	 * @return the indices of the atoms in this residue
	 */
	public ArrayList<Integer> getAtom() {
		return myAtm;
	}
}
