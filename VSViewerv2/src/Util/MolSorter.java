package Util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Sorts molecule indices based on a value.
 * 
 * @author Kyle Diller
 *
 */
public class MolSorter {
	/**
	 * A simple class to help sort the molecule indicies.
	 * 
	 * @author Kyle Diller
	 *
	 */
	private class MolTemp implements Comparable<MolTemp> {
		public int loc;
		@SuppressWarnings("rawtypes")
        public Comparable val;
		public boolean desc;

		/**
		 * Creates an instance to hold onto the index, value, and descending or
		 * ascending.
		 * 
		 * @param l
		 *            the index
		 * @param v
		 *            the value
		 * @param d
		 *            descending or ascending
		 */
		@SuppressWarnings("rawtypes")
        public MolTemp(int l, Comparable v, boolean d) {
			loc = l;
			val = v;
			desc = d;
		}

		@SuppressWarnings("unchecked")
        @Override
		public int compareTo(MolTemp arg0) {
			if (desc) {
				return arg0.val.compareTo(val);
			} else {
				return val.compareTo(arg0.val);
			}
		}
	}

	/**
	 * Private because there is no need to make an instance of the class.
	 */
	private MolSorter() {
	}

	/**
	 * Sorts a list of molecule indicies based on the list of values.
	 * 
	 * @param ind
	 *            the list of indices
	 * @param val
	 *            the list of values
	 * @param desc
	 *            descending or ascending
	 * @return the sorted list of molecule indicies
	 */
	@SuppressWarnings("rawtypes")
    public static ArrayList<Integer> sort(ArrayList<Integer> ind,
			ArrayList<Comparable> val, boolean desc) {

		// Creates the list of classes to sort the indicies by
		ArrayList<MolTemp> mt = new ArrayList<MolTemp>();
		MolSorter temp = new MolSorter();

		for (int i = 0; i < ind.size(); i++) {
			mt.add(temp.new MolTemp(ind.get(i), val.get(i), desc));
		}

		// Sorts and puts the sorted indices into a list
		Collections.sort(mt);

		ArrayList<Integer> newInd = new ArrayList<Integer>();

		for (MolTemp m : mt) {
			newInd.add(m.loc);
		}

		return newInd;
	}
}
