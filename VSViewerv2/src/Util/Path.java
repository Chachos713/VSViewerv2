package Util;

import java.util.ArrayList;

/**
 * A simple class to help make the 2D structural fingerprint for molecules.
 * 
 * @author Kyle Diller
 *
 */
public class Path {
	private ArrayList<Integer> path;

	/**
	 * Creates an empty path.
	 */
	public Path() {
		path = new ArrayList<Integer>();
	}

	/**
	 * @return the length of the path
	 */
	public int length() {
		return path.size();
	}

	/**
	 * Gets the atom index at a given index in the path
	 * 
	 * @param i
	 *            the index
	 * @return the atom index at the given index
	 */
	public int get(int i) {
		return path.get(i);
	}

	/**
	 * Adds a new atom index to the path. Does not contain multiple copies of an
	 * atom index.
	 * 
	 * @param i
	 *            the index of the atom to add
	 * @return true if the atom was added, and false if the atom could not be
	 *         added.
	 */
	public boolean add(int i) {
		int loc = path.indexOf(i);

		if (loc == -1) {
			path.add(i);
			return true;
		}

		return false;
	}

	/**
	 * Copies the path to a new memory location.
	 * 
	 * @return a copy of the path
	 */
	public Path copy() {
		Path p = new Path();

		for (Integer i : path) {
			p.path.add(i);
		}

		return p;
	}

	/**
	 * Prints the path to standard out.
	 */
	public void print() {
		for (int i : path) {
			System.out.print(i + " ");
		}
		System.out.println();
	}
}
