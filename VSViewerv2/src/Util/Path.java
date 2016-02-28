package Util;

import java.util.ArrayList;

public class Path {
	private ArrayList<Integer> path;

	public Path() {
		path = new ArrayList<Integer>();
	}

	public int length() {
		return path.size();
	}

	public int get(int i) {
		return path.get(i);
	}

	public boolean add(int i) {
		int loc = path.indexOf(i);

		if (loc == -1) {
			path.add(i);
			return true;
		}

		return false;
	}

	public Path copy() {
		Path p = new Path();

		for (Integer i : path) {
			p.path.add(i);
		}

		return p;
	}

	public void print() {
		for (int i : path) {
			System.out.print(i + " ");
		}
		System.out.println();
	}
}
