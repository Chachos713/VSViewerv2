package Util;

import java.util.ArrayList;
import java.util.Collections;

public class MolSorter {
	private class MolTemp implements Comparable<MolTemp> {
		public int loc;
		public double val;
		public boolean desc;

		public MolTemp(int l, double v, boolean d) {
			loc = l;
			val = v;
			desc = d;
		}

		@Override
		public int compareTo(MolTemp arg0) {
			if (desc) {
				return Double.compare(arg0.val, val);
			} else {
				return Double.compare(val, arg0.val);
			}
		}
	}

	private MolSorter() {
	}

	public static ArrayList<Integer> sort(ArrayList<Integer> ind,
			ArrayList<Double> val, boolean desc) {

		ArrayList<MolTemp> mt = new ArrayList<MolTemp>();
		MolSorter temp = new MolSorter();

		for (int i = 0; i < ind.size(); i++) {
			mt.add(temp.new MolTemp(ind.get(i), val.get(i), desc));
		}

		Collections.sort(mt);

		ArrayList<Integer> newInd = new ArrayList<Integer>();

		for (MolTemp m : mt) {
			newInd.add(m.loc);
		}

		return newInd;
	}
}
