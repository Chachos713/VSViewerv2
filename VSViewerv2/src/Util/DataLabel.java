package Util;

public class DataLabel {
	private String label;
	private boolean log, doLog;

	public DataLabel(String l, boolean p) {
		log = p;
		label = l;
		doLog = false;
	}

	public void addData(boolean pos) {
		log |= pos;
	}

	public void setDoLog(boolean p) {
		doLog = p;
	}

	public String getLabel() {
		return label;
	}

	public boolean canLog() {
		return log;
	}

	public boolean doLog() {
		return doLog;
	}
}
