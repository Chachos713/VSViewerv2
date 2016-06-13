package Util;

public class Point2d {
	public double x, y;

	public Point2d(double xValue, double yValue) {
		x = xValue;
		y = yValue;
	}

	public double distance(Point2d o) {
		return Math.sqrt((x - o.x) * (x - o.x) + (y - o.y) * (y - o.y));
	}
}
