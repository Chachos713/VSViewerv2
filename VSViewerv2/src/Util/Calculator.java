package Util;

import javax.vecmath.Point2d;

public final class Calculator {

	public static double round(double val, int dig) {
		long mag = (long) Math.pow(10, dig);
		val *= mag;
		val = (int) val;
		return val / mag;
	}

	public static double dis(double xValue, double yValue, int x2, int y2) {
		return Math.sqrt((xValue - x2) * (xValue - x2) + (yValue - y2)
				* (yValue - y2));
	}

	public static boolean inRange(double lo, double hi, double value) {
		return lo <= value && value <= hi;
	}

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
