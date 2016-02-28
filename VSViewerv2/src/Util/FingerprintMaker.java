package Util;

import java.util.ArrayList;
import java.util.Collections;

import javax.vecmath.Vector3d;

import Molecule.Atom;

public class FingerprintMaker {
	private static ArrayList<Vector3d> fpLoc;
	public static double RADIUS = 1.7, DX = 1.5;

	private static void makeLocs() {
		int move = (int) (Math.round(RADIUS / DX));
		fpLoc = new ArrayList<Vector3d>();

		for (int i = -move; i <= move; i++) {
			for (int j = -move; j <= move; j++) {
				for (int k = -move; k <= move; k++) {
					if (i * i + j * j + k * k <= move * move) {
						fpLoc.add(new Vector3d(i, j, k));
					}
				}
			}
		}
	}

	public static ArrayList<Integer> make3d(Atom[] m, double xlo, double ylo,
			double zlo, int nx, int ny, int nz) {
		if (fpLoc == null)
			makeLocs();

		double x, y, z;
		double i, j, k;
		int n;

		ArrayList<Integer> fp3d = new ArrayList<Integer>();

		for (int a = 0; a < m.length; a++) {
			if (m[a].getType() == 1)
				continue;
			i = (int) ((m[a].x3d - xlo - .5) / DX);
			j = (int) ((m[a].y3d - ylo - .5) / DX);
			k = (int) ((m[a].z3d - zlo - .5) / DX);

			for (Vector3d v : fpLoc) {
				z = k + v.z;
				y = j + v.y;
				x = i + v.x;
				n = (int) (z + y * nz + x * ny * nz);

				x = xlo + x * DX + DX / 2.0;
				y = ylo + y * DX + DX / 2.0;
				z = zlo + z * DX + DX / 2.0;

				if (m[a].disSq(x, y, z) < RADIUS * RADIUS && n >= 0) {
					fp3d.add(n);
				}
			}
		}

		Collections.sort(fp3d);
		removeDoubles(fp3d);

		return fp3d;
	}

	private static void removeDoubles(ArrayList<Integer> fp3d) {
		ArrayList<Integer> temp = new ArrayList<Integer>();
		int last = -1;
		int now;

		for (int i = 0; i < fp3d.size(); i++) {
			now = fp3d.get(i);
			if (now != last) {
				temp.add(now);
			}
			last = now;
		}
		fp3d = temp;
	}

	public static ArrayList<Integer> make2d(Atom[] m) {
		final int maxType = 6;
		ArrayList<Path> paths = getPath(4, m);

		ArrayList<Integer> fp2d = new ArrayList<Integer>();

		for (Path p : paths) {
			int tot = 0;
			int fac = 1;
			for (int i = 0; i < p.length(); i++) {
				int val = m[p.get(i)].getFPType();
				val = val * fac;
				tot += val;
				fac *= maxType;
			}

			fp2d.add(tot);
		}

		Collections.sort(fp2d);
		return fp2d;
	}

	private static ArrayList<Path> getPath(int length, Atom[] m) {
		ArrayList<Path> paths = new ArrayList<Path>();

		for (int i = 0; i < m.length; i++) {
			if (m[i].getFPType() != -1) {
				Path p = new Path();
				p.add(m[i].getID());
				paths.add(p);
			}
		}

		for (int i = 1; i < length; i++) {
			paths = grow(paths, m);
		}

		return paths;
	}

	private static ArrayList<Path> grow(ArrayList<Path> paths, Atom[] m) {
		ArrayList<Path> newPaths = new ArrayList<Path>();

		for (Path p : paths) {
			for (Atom i : m[p.get(p.length() - 1)].getBondedTo()) {
				if (i.getFPType() < 0)
					continue;

				Path pN = p.copy();

				if (pN.add(i.getID()))
					newPaths.add(pN);
			}
		}

		return newPaths;
	}

	private FingerprintMaker() {
	}
}
