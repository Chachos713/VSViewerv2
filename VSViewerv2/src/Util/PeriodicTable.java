package Util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

public class PeriodicTable {
	private static HashMap<String, Double> mass;
	private static ArrayList<String> elements;

	public static void create() {
		mass = new HashMap<String, Double>();
		elements = new ArrayList<String>();
		String list = "H 1.0079 He 4.0026 Li 6.941 Be 9.0122 B 10.811 C 12.0107 N 14.0067 O 15.9994 F 18.9984 Ne 20.1797 Na 22.9897 Mg 24.305 Al 26.9815 Si 28.0855 P 30.9738 S 32.065 Cl 35.453 K 39.0983 Ar 39.948 Ca 40.078 Sc 44.9559 Ti 47.867 V 50.9415 Cr 51.9961 Mn 54.938 Fe 55.845 Ni 58.6934 Co 58.9332 Cu 63.546 Zn 65.39 Ga 69.723 Ge 72.64 As 74.9216 Se 78.96 Br 79.904 Kr 83.8 Rb 85.4678 Sr 87.62 Y 88.9059 Zr 91.224 Nb 92.9064 Mo 95.94 Tc 98 Ru 101.07 Rh 102.9055 Pd 106.42 Ag 107.8682 Cd 112.411 In 114.818 Sn 118.71 Sb 121.76 I 126.9045 Te 127.6 Xe 131.293 Cs 132.9055 Ba 137.327 La 138.9055 Ce 140.116 Pr 140.9077 Nd 144.24 Pm 145 Sm 150.36 Eu 151.964 Gd 157.25 Tb 158.9253 Dy 162.5 Ho 164.9303 Er 167.259 Tm 168.9342 Yb 173.04 Lu 174.967 Hf 178.49 Ta 180.9479 W 183.84 Re 186.207 Os 190.23 Ir 192.217 Pt 195.078 Au 196.9665 Hg 200.59 Tl 204.3833 Pb 207.2 Bi 208.9804 Po 209 At 210 Rn 222 Fr 223 Ra 226 Ac 227 Pa 231.0359 Th 232.0381 Np 237 U 238.0289";

		String[] all = list.split(" ");
		double weight;

		for (int i = 0; i < all.length / 2; i++) {
			weight = Double.parseDouble(all[i * 2 + 1]);
			elements.add(all[i * 2]);
			mass.put(all[i * 2], weight);
		}
	}

	public static boolean created() {
		return mass != null;
	}

	public static int getAtomicNumber(String element) {
		return elements.indexOf(element) + 1;
	}

	public static String getElement(int atm) {
		return elements.get(atm - 1);
	}

	public static double getAtomicWeight(String element) {
		Double d = mass.get(element);

		return d == null ? 0 : d;
	}

	public static double getAtomicWeight(int atm) {
		return getAtomicWeight(getElement(atm));
	}

	public static Color getColor(int type, boolean pro) {
		switch (type) {
		case 6:
			if (pro)
				return Color.magenta;
			else
				return Color.green;
		case 7:
			return Color.cyan;
		case 8:
			return Color.red;
		case 9:
			return Color.green.brighter().brighter().brighter();
		case 16:
			return Color.yellow;
		case 17:
			return Color.green.darker().darker().darker();
		case 35:
			return Color.magenta;
		default:
			return Color.white;
		}
	}
}
