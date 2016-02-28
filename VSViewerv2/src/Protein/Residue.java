package Protein;

import java.util.ArrayList;

public class Residue {
	private char chain;
	private String resNum;
	private ArrayList<Integer> myAtm;
	private ArrayList<String> atomName;

	public Residue() {
		chain = ' ';
		resNum = "";
		myAtm = new ArrayList<Integer>();
		atomName = new ArrayList<String>();
	}

	public Residue(char c, String r) {
		chain = c;
		resNum = new String(r);
		myAtm = new ArrayList<Integer>();
		atomName = new ArrayList<String>();
	}

	public Residue copy() {
		Residue r = new Residue();
		r.chain = chain;
		r.resNum = new String(resNum);
		r.myAtm = new ArrayList<Integer>(myAtm);
		r.atomName = new ArrayList<String>(atomName);
		return r;
	}

	public String getResidueDesc() {
		return chain + resNum;
	}

	public Integer getAtomIndex(String aName) {
		Integer a;

		for (a = 0; a < myAtm.size(); a++) {
			if (aName.equals(atomName.get(a))) {
				return myAtm.get(a);
			}
		}
		return -1;
	}

	public void addAtom(int a, String aName) {
		myAtm.add(a);
		atomName.add(aName);
	}

	public boolean doIBelong(char c, String r) {
		if (chain != c) {
			return false;
		}
		if (!resNum.equals(r)) {
			return false;
		}

		return true;
	}

	public ArrayList<Integer> getAtom() {
		return myAtm;
	}
}
