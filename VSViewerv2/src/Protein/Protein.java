package Protein;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.media.j3d.BranchGroup;

import Molecule.Bond;
import Util.PeriodicTable;

/**
 * A simple representation of a protein. Extends BranchGroup so that this class
 * can store the 3D coordinates, and be easily added to the 3D viewer, without
 * anyone else knowing the position of all atoms.
 * 
 * @author Kyle Diller
 *
 */
public class Protein extends BranchGroup {
	/**
	 * Reads a protein from a file.
	 * 
	 * @param file
	 *            the file to read
	 * @return a protein read from the file
	 * @throws Exception
	 *             if there is a problem reading the file
	 */
	public static Protein read(String file) throws Exception {
		Scanner sc = null;
		Protein pro = null;
		System.out.println("Started Reading: " + file);
		try {
			sc = new Scanner(new File(file));

			int na = 0;
			ArrayList<String> atomLines = new ArrayList<String>();
			String temp;

			while (sc.hasNextLine()) {
				temp = sc.nextLine();

				if (temp.length() >= 54 && temp.startsWith("ATOM")
						&& (temp.charAt(16) == 'A' || temp.charAt(16) == ' ')) {
					atomLines.add(temp);
					na++;
				}
			}

			ProAtom[] atms = new ProAtom[na];
			String name, res, t;
			int type;
			double x, y, z;
			char chain;
			double dis1, dis2;
			boolean found;

			ArrayList<Bond> bond = new ArrayList<Bond>();
			ArrayList<Residue> residues = new ArrayList<Residue>();

			// Creates the bonds between the atoms and places each atom in it's
			// respective residue
			for (int i = 0; i < na; i++) {
				temp = atomLines.get(i);
				chain = temp.charAt(21);
				res = temp.substring(22, 27);

				type = 1;

				if (temp.charAt(12) == ' ') {
					t = "" + temp.charAt(13);
					type = PeriodicTable.getAtomicNumber(t);
				}

				name = temp.substring(12, 16);
				x = Double.parseDouble(temp.substring(30, 38).trim());
				y = Double.parseDouble(temp.substring(38, 46).trim());
				z = Double.parseDouble(temp.substring(46, 54).trim());

				atms[i] = new ProAtom(type, i, x, y, z, chain, res, name);
				dis1 = atms[i].getBondLength();

				for (int j = 0; j < i; j++) {
					dis2 = atms[j].getBondLength();

					if (atms[i].disSq(atms[j]) <= ((dis1 + dis2) * 1.5)
							* ((dis1 + dis2) * 1.5)) {
						bond.add(new Bond(atms[i], atms[j], 1, true, null, true));
						atms[i].addPartner(atms[j]);
						atms[j].addPartner(atms[i]);
					}
				}

				found = false;

				for (Residue r : residues) {
					if (r.doIBelong(atms[i].getChain(), atms[i].getResidue())) {
						found = true;
						r.addAtom(i, atms[i].getName());
						break;
					}
				}

				if (!found) {
					Residue newRes = new Residue(atms[i].getChain(),
							atms[i].getResidue());
					newRes.addAtom(i, atms[i].getName());
					residues.add(newRes);
				}
			}

			Bond[] bnd = new Bond[bond.size()];

			for (int i = 0; i < bond.size(); i++) {
				bnd[i] = bond.get(i);
			}

			pro = new Protein(atms, bnd, residues);
		} finally {
			sc.close();
		}

		System.out.println("DONE");
		return pro;
	}

	private ProAtom[] atms;
	private Bond[] bnds;
	private ArrayList<Residue> residues;
	private ArrayList<Integer> chain;

	/**
	 * Creates a protein
	 * 
	 * @param pa
	 *            the atoms within the protein
	 * @param b
	 *            the bonds between atoms
	 * @param r
	 *            the list of residues
	 */
	public Protein(ProAtom[] pa, Bond[] b, ArrayList<Residue> r) {
		atms = pa;
		bnds = b;
		residues = r;

		for (Bond bd : bnds) {
			if (bd.doDraw())
				this.addChild(bd);
		}

		createChain();

		Integer caLast = -1;
		Bond temp;

		for (Integer ca : chain) {
			if (caLast >= 0 && ca >= 0) {
				temp = new Bond(atms[caLast], atms[ca], 1, true, new float[] {
						.3f, .1f, .9f }, true);
				this.addChild(temp);
			}
			caLast = ca;
		}

		System.out.println(chain.size());
	}

	/**
	 * Creates the chain of atoms that form a residue.
	 */
	private void createChain() {
		Integer c;
		Integer ca;
		double d;
		Integer n;

		chain = new ArrayList<Integer>();
		c = -1;
		ca = -1;
		n = -1;
		for (Residue r : residues) {
			n = r.getAtomIndex(" N  ");
			ca = r.getAtomIndex(" CA ");
			if (n < 0 || ca < 0) {
				chain.add(-1);
			} else if (c >= 0) {
				d = Math.sqrt(atms[n].disSq(atms[c]));
				if (!(d < (atms[n].getBondLength() + atms[c].getBondLength()) * 1.25)) {
					chain.add(-1);
				}

			}
			if (ca >= 0) {
				chain.add(ca);
			}
			c = r.getAtomIndex(" C  ");
			if (c < 0) {
				chain.add(-1);
			}
		}
	}
}
