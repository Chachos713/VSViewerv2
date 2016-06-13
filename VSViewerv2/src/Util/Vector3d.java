package Util;

/**
 * A simple utility class used to store a 3 dimensional vector.
 * 
 * @author Kyle Diller
 *
 */
public class Vector3d {
	public double x, y, z;

	/**
	 * Starts the vector with initial values for x, y, and z.
	 * 
	 * @param i
	 *            initial value of x.
	 * @param j
	 *            initial value of y.
	 * @param k
	 *            iniital value of z.
	 */
	public Vector3d(int i, int j, int k) {
		x = i;
		y = j;
		z = k;
	}

}
