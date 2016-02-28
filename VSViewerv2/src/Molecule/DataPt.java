package Molecule;
public class DataPt {
	public static DataPt read(String s) {
		String[] split = s.split(" ");
		double v = Double.parseDouble(split[0]);

		return new DataPt(v, split[1]);
	}

	private String label;
	private double value;

	public DataPt(double v, String l) {
		label = new String(l);
		value = v;
	}

	public DataPt(DataPt other) {
		this(other.value, other.label);
	}

	public double getValue() {
		return value;
	}

	public String getLabel() {
		return label;
	}

	public String toString() {
		return value + " " + label;
	}
}