package Util;

import javax.vecmath.Point2d;

/**
 * A class that handles a lot of the math calculations.
 * 
 * @author Kyle Diller
 *
 */
public final class Calculator {

	/**
	 * Rounds a double to a certain number of digits.
	 * 
	 * @param val
	 *            the value to round
	 * @param dig
	 *            the number of digits to keep
	 * @return the rounded value
	 */
	public static double round(double val, int dig) {
		long mag = (long) Math.pow(10, dig);
		val *= mag;
		val = (int) val;
		return val / mag;
	}

	/**
	 * Gets the distance between two points
	 * 
	 * @param xValue
	 *            the first x coordinate
	 * @param yValue
	 *            the first y coordinate
	 * @param x2
	 *            the second x coordinate
	 * @param y2
	 *            the second y coordinate
	 * @return the distance between the 2 points
	 */
	public static double dis(double xValue, double yValue, int x2, int y2) {
		return Math.sqrt((xValue - x2) * (xValue - x2) + (yValue - y2)
				* (yValue - y2));
	}

	/**
	 * Checks if a value is in a range
	 * 
	 * @param lo
	 *            the minimum
	 * @param hi
	 *            the maximum
	 * @param value
	 *            the value to check
	 * @return true if the value if between lo and hi
	 */
	public static boolean inRange(double lo, double hi, double value) {
		return lo <= value && value <= hi;
	}

	/**
	 * Calculates the least squares regression line for a set of points
	 * 
	 * @param locs
	 *            the set of points
	 * @return the values for the least squares regression line <br>
	 *         0 = slope <br>
	 *         1 = y-intercept <br>
	 *         2 = the correlation coefficient
	 */
	public static double[] LSRL(Point2d[] locs) {
		try {
			double[] lsrl = new double[3];
			int count = 0;
			double xMean, yMean, xStd, yStd, r;
			xMean = 0;
			yMean = 0;
			xStd = 0;
			yStd = 0;
			r = 0;

			// Calculates the mean and standard deviation
			for (int i = 0; i < locs.length; i++) {
				if (locs[i] == null)
					continue;

				count++;
				xMean += locs[i].x;
				yMean += locs[i].y;
				xStd += locs[i].x * locs[i].x;
				yStd += locs[i].y * locs[i].y;
			}

			xMean /= count;
			yMean /= count;

			xStd /= count;
			yStd /= count;

			xStd = Math.sqrt(xStd - xMean * xMean);
			yStd = Math.sqrt(yStd - yMean * yMean);

			// Calculates r
			for (int i = 0; i < locs.length; i++) {
				if (locs[i] == null)
					continue;

				r += locs[i].x * locs[i].y;
			}

			r /= count;
			r = (r - xMean * yMean) / (xStd * yStd);

			lsrl[0] = r * yStd / xStd;
			lsrl[1] = yMean - lsrl[0] * xMean;
			lsrl[2] = r;

			return lsrl;
		} catch (Exception e) {
			return null;
		}
	}
}
