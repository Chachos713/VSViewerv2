package Util;

import java.util.ArrayList;
import java.util.Collections;

import Molecule.Atom;

/**
 * Creates the fingerprints for a molecule.
 * 
 * @author Kyle Diller
 *
 */
public class FingerprintMaker {
	private static ArrayList<Vector3d> fpLoc;
	public static double RADIUS = 1.7, DX = 1.5;

	/**
	 * Creates the list of loactions for the 3D fingerprint.
	 */
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

	/**
	 * Creates the 3D fingerprint for a molecule
	 * 
	 * @param m
	 *            the molecule
	 * @param xlo
	 *            the lower x bound
	 * @param ylo
	 *            the lower y bound
	 * @param zlo
	 *            the lower z bound
	 * @param nx
	 *            number of boxes to divide the width by
	 * @param ny
	 *            number of boxes to divide the hegiht by
	 * @param nz
	 *            number of boxes to divide the depth by
	 * @return a list of values for the 3D fingerprint for a molecule
	 */
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

	/**
	 * Removes points that are counted multiple times
	 * 
	 * @param fp3d
	 *            the finger print
	 */
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

	/**
	 * Creates a structural fingerprint for a molecule
	 * 
	 * @param m
	 *            the molecule. Note this is an Array of Atoms.
	 * @return the structural fingerprint for the molecule
	 */
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

	/**
	 * Creates paths of a given length throughout the molecule
	 * 
	 * @param length
	 *            the length of the paths
	 * @param m
	 *            the molecule to make the paths with
	 * @return the paths throughout the molecule of a given length
	 */
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

	/**
	 * Increases the length of the paths by one
	 * 
	 * @param paths
	 *            the paths to increase
	 * @param m
	 *            the molecule to increase the paths by
	 * @return the paths that have increased
	 */
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

	/**
	 * The constructor is private because there is no need to have instances of
	 * this class
	 */
	private FingerprintMaker() {
	}
}
