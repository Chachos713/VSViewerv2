package Util;

/**
 * A simple utility class used to store points as doubles instead of integers.
 * 
 * @author Kyle Diller
 *
 */
public class Point2d {
	public double x, y;

	/**
	 * Creates a point with initial values for x and y.
	 * 
	 * @param xValue
	 *            initial value of x.
	 * @param yValue
	 *            initial value of y.
	 */
	public Point2d(double xValue, double yValue) {
		x = xValue;
		y = yValue;
	}

	/**
	 * Calculates the distance between 2 points.
	 * 
	 * @param o
	 *            the other point.
	 * @return the distance between the 2 points.
	 */
	public double distance(Point2d o) {
		return Math.sqrt((x - o.x) * (x - o.x) + (y - o.y) * (y - o.y));
	}
}
