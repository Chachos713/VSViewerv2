package Molecule;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;

import Util.DataLabel;
import Util.EndFileException;
import Util.FingerprintMaker;
import Util.MissingPartException;
import Util.PeriodicTable;

public class Molecule extends BranchGroup {
	private static final String D2C = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789`~@#$%^&*()_+-=[]{};:,<.>?/|";
	private static final int D2C_MAX = 90;

	public static Molecule readMoleculeVSV(Scanner sc, double lo2d,
			double hi2d, double xlo, double xhi, double ylo, double yhi,
			double zlo, double zhi) throws Exception {

		if (!PeriodicTable.created())
			PeriodicTable.create();

		String name = sc.nextLine().substring(10);

		Atom[] a = null;
		Bond[] b = null;
		ArrayList<Comment> c = new ArrayList<Comment>();
		ArrayList<DataPt> d = new ArrayList<DataPt>();
		String two = null, three = null;

		String[] typesA = null, typesB = null;

		String line = "";

		while (sc.hasNext()) {
			line = sc.nextLine();

			if (line.equals("</MOLECULE>"))
				break;

			if (line.startsWith("<ATOM>")) {
				typesA = line.substring(7).split(" ");
			} else if (line.startsWith("<BOND>")) {
				typesB = line.substring(7).split(" ");
			} else if (line.startsWith("<2D>")) {
				two = line.substring(4);
			} else if (line.startsWith("<3D>")) {
				three = line.substring(4);
			} else if (line.startsWith("<COMMENT>")) {
				while (sc.hasNext()) {
					line = sc.nextLine();

					if (line.equals("</COMMENT>"))
						break;

					c.add(new Comment(line));
				}
			} else if (line.startsWith("<DATA>")) {
				while (sc.hasNext()) {
					line = sc.nextLine();

					if (line.equals("</DATA>"))
						break;

					d.add(DataPt.read(line));
				}
			}
		}

		if (!line.equals("</MOLECULE>"))
			throw new EndFileException("File Ended Early");

		try {
			a = new Atom[typesA.length];
			b = new Bond[typesB.length];
			two.length();
			three.length();
		} catch (NullPointerException e) {
			throw new MissingPartException("Missing Information in the File");
		}

		String d2sub, d3sub;
		int d2loc = 0, d3loc = 0;
		double x2 = 0, y2 = 0, x3, y3, z3;
		int type;
		int i = 0;

		try {
			for (i = 0; i < a.length; i++) {

				type = PeriodicTable.getAtomicNumber(typesA[i]);

				d3sub = three.substring(d3loc, d3loc + 6);
				d3loc += 6;

				x3 = getDouble(d3sub.substring(0, 2)) * (xhi - xlo) + xlo;
				y3 = getDouble(d3sub.substring(2, 4)) * (yhi - ylo) + ylo;
				z3 = getDouble(d3sub.substring(4, 6)) * (zhi - zlo) + zlo;

				if (type != 1) {
					d2sub = two.substring(d2loc, d2loc + 4);
					d2loc += 4;

					x2 = getDouble(d2sub.substring(0, 2)) * (hi2d - lo2d)
							+ lo2d;
					y2 = getDouble(d2sub.substring(2, 4)) * (hi2d - lo2d)
							+ lo2d;
				} else {
					x2 = 0;
					y2 = 0;
				}

				a[i] = new Atom(type, i, x2, y2, x3, y3, z3);
			}
		} catch (Exception e) {
			throw new MissingPartException(
					"Missing Atom Location Information: " + (i + 1));
		}

		int a1, a2, t;
		String[] bond;
		for (i = 0; i < b.length; i++) {
			bond = typesB[i].split(":");
			a1 = Integer.parseInt(bond[0]) - 1;
			a2 = Integer.parseInt(bond[1]) - 1;
			t = Integer.parseInt(bond[2]);

			b[i] = new Bond(a[a1], a[a2], t, true, null, false);
		}

		ArrayList<Integer> fp3 = FingerprintMaker.make3d(a, xlo, ylo, zlo,
				(int) ((xhi - xlo) / FingerprintMaker.DX),
				(int) ((yhi - ylo) / FingerprintMaker.DX),
				(int) ((zhi - zlo) / FingerprintMaker.DX));

		ArrayList<Integer> fp2 = FingerprintMaker.make2d(a);

		return new Molecule(name, a, b, c, d, two, three, fp3, fp2);
	}

	public static double getDouble(String substring) {
		return getPer(substring.charAt(0), substring.charAt(1));
	}

	private static double getPer(char c1, char c2) {
		double per = 0.0;
		int b1 = 0;
		int b2 = 0;

		b1 = D2C.indexOf(c1);
		if (b1 == -1) {
			return Double.NEGATIVE_INFINITY;
		}
		b2 = D2C.indexOf(c2);
		if (b2 == -1) {
			return Double.NEGATIVE_INFINITY;
		}

		per = (double) b1 / (double) (D2C_MAX) + (double) (b2)
				/ ((double) (D2C_MAX) * (double) (D2C_MAX));
		return per;
	}

	public static Molecule readSDF(Scanner sc) throws Exception {
		if (!PeriodicTable.created())
			PeriodicTable.create();

		String name = sc.nextLine();

		sc.nextLine();
		sc.nextLine();

		int na = sc.nextInt();
		int nb = sc.nextInt();

		sc.nextLine();

		Atom[] atms = new Atom[na];
		Bond[] bnds = new Bond[nb];

		double x, y, z;
		String type;
		int t;

		for (int i = 0; i < na; i++) {
			x = sc.nextDouble();
			y = sc.nextDouble();
			z = sc.nextDouble();
			type = sc.next();
			sc.nextLine();

			t = PeriodicTable.getAtomicNumber(type);
			atms[i] = new Atom(t, i, x, y, x, y, z);
		}

		int a1, a2;

		for (int i = 0; i < nb; i++) {
			a1 = sc.nextInt();
			a2 = sc.nextInt();
			t = sc.nextInt();
			sc.nextLine();
			bnds[i] = new Bond(atms[a1 - 1], atms[a2 - 1], t, true, null, false);
		}

		sc.nextLine();
		sc.nextLine();

		return new Molecule(name, atms, bnds, new ArrayList<Comment>(),
				new ArrayList<DataPt>(), "Zwei", "Drei",
				new ArrayList<Integer>(), new ArrayList<Integer>());
	}

	private String name;
	private Atom[] atms;
	private Bond[] bnds;
	private ArrayList<Comment> comments;
	private ArrayList<DataPt> dataPts;

	private ArrayList<Integer> fp3d, fp2d;

	private String d3, d2;

	private Molecule(String n, Atom[] a, Bond[] b, ArrayList<Comment> c,
			ArrayList<DataPt> d, String two, String three,
			ArrayList<Integer> fp3, ArrayList<Integer> fp2) {

		name = new String(n);
		d2 = new String(two);
		d3 = new String(three);

		atms = new Atom[a.length];
		bnds = new Bond[b.length];
		comments = new ArrayList<Comment>(c.size());
		dataPts = new ArrayList<DataPt>(d.size());

		TransformGroup objTg = new TransformGroup();

		for (int i = 0; i < atms.length; i++) {
			atms[i] = new Atom(a[i]);
		}

		for (int i = 0; i < bnds.length; i++) {
			bnds[i] = new Bond(b[i], !d3.isEmpty());
			objTg.addChild(bnds[i]);
		}

		for (int i = 0; i < c.size(); i++) {
			comments.add(new Comment(c.get(i)));
		}

		for (int i = 0; i < d.size(); i++) {
			dataPts.add(new DataPt(d.get(i)));
		}

		fp3d = new ArrayList<Integer>(fp3.size());
		fp2d = new ArrayList<Integer>(fp2.size());

		for (int i : fp3) {
			fp3d.add(i);
		}

		for (int i : fp2) {
			fp2d.add(i);
		}

		this.addChild(objTg);
	}

	public Molecule(Molecule other) {
		this(other.name, other.atms, other.bnds, other.comments, other.dataPts,
				other.d2, other.d3, other.fp3d, other.fp2d);
	}

	public String getName() {
		return name;
	}

	public void addComment(String c) {
		c = c.toLowerCase();
		for (Comment com : comments) {
			if (com.getComment().equals(c)) {
				return;
			}
		}
		comments.add(new Comment(c));
	}

	public void removeComment(String c) {
		for (Comment com : comments) {
			if (com.getComment().equals(c)) {
				comments.remove(com);
				return;
			}
		}
	}

	public double getData(String label) {
		for (DataPt d : dataPts) {
			if (d.getLabel().equals(label))
				return d.getValue();
		}

		return Double.NEGATIVE_INFINITY;
	}

	public ArrayList<String> getComments() {
		ArrayList<String> com = new ArrayList<String>();

		for (Comment c : comments) {
			com.add(new String(c.getComment()));
		}

		return com;
	}

	public void addToList(ArrayList<DataLabel> labelList,
			ArrayList<String> commentList) {
		for (Comment c : comments) {
			if (!commentList.contains(c.getComment()))
				commentList.add(c.getComment());
		}

		boolean found;
		for (DataPt d : dataPts) {
			found = false;
			for (DataLabel l : labelList) {
				if (l.getLabel().equals(d.getLabel())) {
					found = true;
					l.addData(Math.signum(d.getValue()) > 0);
					break;
				}
			}

			if (!found) {
				labelList.add(new DataLabel(d.getLabel(), Math.signum(d
						.getValue()) > 0));
			}
		}
	}

	public void draw(Graphics g, int x, int y, int width, int height, int k) {
		if (d2.isEmpty())
			return;

		g.setColor(Color.black);
		g.drawLine(x, y, x + width, y);
		g.drawLine(x, y, x, y + height);
		g.drawLine(x, y + height, x + width, y + height);
		g.drawLine(x + width, y, x + width, y + height);

		final double dy = 20;

		x += 5;
		y += 5;
		width -= 10;
		height -= dy + 5;

		double xMin = Double.POSITIVE_INFINITY;
		double xMax = Double.NEGATIVE_INFINITY;
		double yMin = Double.POSITIVE_INFINITY;
		double yMax = Double.NEGATIVE_INFINITY;

		for (int i = 0; i < atms.length; i++) {
			if (atms[i].getType() == 1)
				continue;

			xMin = Math.min(xMin, atms[i].x2d);
			xMax = Math.max(xMax, atms[i].x2d);
			yMin = Math.min(yMin, atms[i].y2d);
			yMax = Math.max(yMax, atms[i].y2d);
		}

		double xRatio = width / (xMax - xMin);
		double yRatio = height / (yMax - yMin);

		double minRatio = Math.min(xRatio, yRatio);

		xRatio = (width - minRatio * (xMax - xMin)) / 2;
		yRatio = (height - minRatio * (yMax - yMin)) / 2;
		Atom a1, a2;
		int type;
		int x1, x2, y1, y2;
		double vx, vy, dist;

		for (int i = 0; i < bnds.length; i++) {
			a1 = bnds[i].getA1();
			a2 = bnds[i].getA2();
			type = bnds[i].getType();

			if (a1.getType() == 1 || a2.getType() == 1)
				continue;

			x1 = (int) ((a1.x2d - xMin) * minRatio + x + xRatio);
			x2 = (int) ((a2.x2d - xMin) * minRatio + x + xRatio);
			y1 = (int) ((a1.y2d - yMin) * minRatio + y + yRatio);
			y2 = (int) ((a2.y2d - yMin) * minRatio + y + yRatio);

			vx = y1 - y2;
			vy = x2 - x1;
			dist = Math.sqrt(vx * vx + vy * vy);
			vx /= dist;
			vy /= dist;

			if (type % 2 == 1) {// Draws a single, or triple bond
				g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
				if (type == 3) {// If a triple bond
					g.drawLine((int) (x1 + vx * 5.0), (int) (y1 + vy * 5.0),
							(int) (x2 + vx * 5.0), (int) (y2 + vy * 5.0));
					g.drawLine((int) (x1 - vx * 5.0), (int) (y1 - vy * 5.0),
							(int) (x2 - vx * 5.0), (int) (y2 - vy * 5.0));
				}
			} else if (type == 2) {// DRaws a double bond
				g.drawLine((int) (x1 + vx * 2.5), (int) (y1 + vy * 2.5),
						(int) (x2 + vx * 2.5), (int) (y2 + vy * 2.5));
				g.drawLine((int) (x1 - vx * 2.5), (int) (y1 - vy * 2.5),
						(int) (x2 - vx * 2.5), (int) (y2 - vy * 2.5));
			}
		}

		Font f = new Font("SansSerif", Font.BOLD, 12);
		g.setFont(f);
		g.setColor(Color.black);

		String tmp;

		for (int i = 0; i < atms.length; i++) {
			if (atms[i].getType() == 1 || atms[i].getType() == 6)
				continue;

			x1 = (int) (minRatio * (atms[i].x2d - xMin) + x + xRatio);
			y1 = (int) (minRatio * (atms[i].y2d - yMin) + y + yRatio);

			tmp = PeriodicTable.getElement(atms[i].getType());

			g.setColor(Color.white);
			g.fillOval((int) (x1 - 4.0), (int) (y1 - 4.0), 8, 8);
			g.setColor(Color.black);
			drawCenteredString(tmp, (int) (x1), (int) (y1), g);
		}

		if (k > -1)
			drawCenteredString(k + " : " + name, (int) ((2 * x + width) / 2.0),
					(int) (y + height + 13), g);
		else
			drawCenteredString(name, (int) ((2 * x + width) / 2.0), (int) (y
					+ height + 13), g);
	}

	private void drawCenteredString(String s, int u, int v, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		int x = u - fm.stringWidth(s) / 2;
		int y = v + fm.getAscent() / 2;
		g.drawString(s, x, y);
	}

	public void saveVSV(FileWriter os) throws Exception {
		os.write("<MOLECULE>" + name);

		os.write("\n<ATOM>");

		for (int i = 0; i < atms.length; i++) {
			os.write(" " + (PeriodicTable.getElement(atms[i].getType())));
		}

		os.write("\n<BOND>");

		for (int i = 0; i < bnds.length; i++) {
			os.write(" " + bnds[i]);
		}

		os.write("\n<2D>" + d2);
		os.write("\n<3D>" + d3);

		os.write("\n<COMMENT>");

		for (int i = 0; i < comments.size(); i++) {
			os.write("\n" + comments.get(i).getComment());
		}

		os.write("\n</COMMENT>");

		os.write("\n<DATA>");

		for (int i = 0; i < dataPts.size(); i++) {
			os.write("\n" + dataPts.get(i).getValue() + " "
					+ dataPts.get(i).getLabel());
		}

		os.write("\n</DATA>");
		os.write("\n</MOLECULE>");
	}

	public void saveSDF(FileWriter os, boolean d3) throws Exception {
		os.write(name + "\n");

		String progName = "Viewer";
		String buf = progName;
		if (progName.length() > 8) {
			buf = progName.substring(0, 8);
		}
		int ns = 8 - buf.length();
		for (int i = 0; i < ns; i++) {
			buf += " ";
		}
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		int sec = c.get(Calendar.SECOND);

		if (month < 10) {
			buf += "0";
		}
		buf += month;
		if (day < 10) {
			buf += "0";
		}
		buf += day;
		year = (year % 100);
		if (year < 1) {
			buf += "0";
		}
		if (year < 10) {
			buf += "0";
		}
		buf += year;

		if (hour < 1) {
			buf += "0";
		}
		if (hour < 10) {
			buf += "0";
		}
		buf += hour;
		if (min < 1) {
			buf += "0";
		}
		if (min < 10) {
			buf += "0";
		}
		buf += min;
		if (sec < 1) {
			buf += "0";
		}
		if (sec < 10) {
			buf += "0";
		}
		buf += sec;

		if (d3)
			buf += "3D";
		else
			buf += "2D";

		os.write(buf + "\n");
		os.write("\n");

		buf = "";
		if (atms.length < 10) {
			buf += " ";
		}
		if (atms.length < 100) {
			buf += " ";
		}
		buf += atms.length;
		if (bnds.length < 10) {
			buf += " ";
		}
		if (bnds.length < 100) {
			buf += " ";
		}
		buf += bnds.length;
		os.write(buf + "  0  0  1  0  0  0  0  0999 V2000\n");

		for (int i = 0; i < atms.length; i++) {
			buf = atms[i].getMolLine(d3);
			os.write(buf + "\n");
		}

		String field;
		for (int i = 0; i < bnds.length; i++) {
			buf = "";
			field = "";
			field += (bnds[i].getA1().getID() + 1);
			ns = 3 - field.length();
			for (int j = 0; j < ns; j++) {
				buf += " ";
			}
			buf += field;

			field = "";
			field += (bnds[i].getA2().getID() + 1);
			ns = 3 - field.length();
			for (int j = 0; j < ns; j++) {
				buf += " ";
			}
			buf += field;

			field = "";
			field += bnds[i].getType();
			ns = 3 - field.length();
			for (int j = 0; j < ns; j++) {
				buf += " ";
			}
			buf += field;

			field = "";
			field += "0";
			ns = 3 - field.length();
			for (int j = 0; j < ns; j++) {
				buf += " ";
			}
			buf += field;

			buf += "  0  0";
			os.write(buf + "\n");
		}
		os.write("M  END\n$$$$");
	}

	public int getDimension() {
		int dimension = 1;

		if (!d2.isEmpty())
			dimension++;

		if (!d3.isEmpty())
			dimension++;

		return dimension;
	}

	public boolean hasComment(String com) {
		for (Comment c : comments) {
			if (c.getComment().equals(com))
				return true;
		}

		return false;
	}

	public void split(String label, double value, String dataName) {
		double v = this.getData(label);

		int val = (v < value) ? 0 : 1;

		addNewData(val, dataName);
	}

	public void addNewData(double value, String name) {
		if (this.getData(name) != Double.NEGATIVE_INFINITY)
			return;

		dataPts.add(new DataPt(value, name));
	}

	public double compare(Molecule query, boolean d3) {
		int numEqu = 0;
		int i1 = 0, i2 = 0;
		int k1, k2;
		ArrayList<Integer> mol1 = this.fp2d;
		ArrayList<Integer> mol2 = query.fp2d;

		if (d3) {
			mol1 = this.fp3d;
			mol2 = query.fp3d;
		}

		while (i1 < mol1.size() && i2 < mol2.size()) {
			k1 = mol1.get(i1);
			k2 = mol2.get(i2);
			if (k1 == k2) {
				i1++;
				i2++;
				numEqu++;
			} else if (k1 > k2) {
				i2++;
			} else {
				i1++;
			}
		}

		return (double) (numEqu)
				/ (double) (mol1.size() + mol2.size() - numEqu);
	}
}
