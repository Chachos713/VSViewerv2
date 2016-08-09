package Util;

import java.util.ArrayList;

import astex.Atom;
import astex.Molecule;
import astex.MoleculeRenderer;

/**
 * Find hydrogen bonds between a peptide and protein.
 * 
 * @author Kyle Diller
 *
 */
public class HBondFinder {
	private static final double DIST = 2.5;

	/**
	 * Finds the hydrogen bonds between a protein and a peptide.
	 * 
	 * @param prevMol
	 *            the peptide.
	 * @param protein
	 *            the protein.
	 * @param mr
	 *            the renderer to draw them on.
	 */
	public static void addHBonds(Molecule prevMol, Molecule protein,
			MoleculeRenderer mr) {

		if (protein == null)
			return;

		findBonds(prevMol, protein, mr);
		findBonds(protein, prevMol, mr);
	}

	/**
	 * Finds all the hydrogen bonds where mol1 is the donor and mol2 is the
	 * acceptor.
	 * 
	 * @param mol1
	 *            the first molecule.
	 * @param mol2
	 *            the second molecule.
	 * @param mr
	 *            the renderer to draw them on.
	 */
	private static void findBonds(Molecule mol1, Molecule mol2,
			MoleculeRenderer mr) {
		ArrayList<Atom> donor = findDonors(mol1);
		ArrayList<Atom> accep = findAcceptors(mol2);

		for (int i = 0; i < donor.size(); i++) {
			for (int j = 0; j < accep.size(); j++) {
				if (donor.get(i).distance(accep.get(j)) < DIST) {
					mr.addDistance(donor.get(i), accep.get(j));
				}
			}
		}
	}

	/**
	 * Finds all hydrogen bond acceptors.
	 * 
	 * @param mol
	 *            the molecule to search.
	 * @return the list of acceptors.
	 */
	private static ArrayList<Atom> findAcceptors(Molecule mol) {
		ArrayList<Atom> atms = new ArrayList<Atom>();

		for (int i = 0; i < mol.getAtomCount(); i++) {
			if (mol.getAtom(i).getElement() == 8
					|| mol.getAtom(i).getElement() == 7) {
				atms.add(mol.getAtom(i));
			}
		}

		return atms;
	}

	/**
	 * Finds all the hydrogen bond donors.
	 * 
	 * @param mol
	 *            the molecule to search.
	 * @returnthe list of donors.
	 */
	private static ArrayList<Atom> findDonors(Molecule mol) {
		ArrayList<Atom> atms = new ArrayList<Atom>();
		Atom a1;

		for (int i = 0; i < mol.getAtomCount(); i++) {
			if (mol.getAtom(i).getElement() == 1) {
				a1 = mol.getAtom(i).getBondedAtom(0);
				if (a1.getElement() == 8 || a1.getElement() == 7) {
					atms.add(mol.getAtom(i));
				}
			}
		}

		return atms;
	}

}
