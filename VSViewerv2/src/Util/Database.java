package Util;

import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.vecmath.Point2d;
import javax.vecmath.Vector3d;

import Molecule.Molecule;
import Panels.SplitPanel;

public class Database extends Observable {
	public static Database read(String file) throws Exception {
		if (file.endsWith(".vsv"))
			return readVSV(file);
		else if (file.endsWith(".sdf"))
			return readSDF(file);
		else
			return null;
	}

	@SuppressWarnings("resource")
	private static Database readVSV(String file) throws Exception {
		long start = System.currentTimeMillis();

		int count = 0;
		try {
			Scanner sc = new Scanner(new File(file));

			double[] limits;

			String[] lines = sc.nextLine().split(" ");
			limits = new double[8];

			if (lines.length != 8) {
				throw new LimitsNotDefinedException(
						"The limits are not defined for this file");
			}

			for (int i = 0; i < 8; i++) {
				try {
					limits[i] = Double.parseDouble(lines[i]);
				} catch (Exception e) {
					throw new NumberFormatException("Limit " + i
							+ " can't be read: " + lines[i]);
				}
			}

			// The start for a simple queue like implementation
			Node<Molecule> header = new Node<Molecule>(null);
			Node<Molecule> last = header;
			Molecule temp;
			Node<Molecule> tempNode;

			while (sc.hasNextLine()) {
				temp = Molecule.readMoleculeVSV(sc, limits[0], limits[1],
						limits[2], limits[3], limits[4], limits[5], limits[6],
						limits[7]);

				count++;
				tempNode = new Node<Molecule>(temp);
				last.setNext(tempNode);
				last = tempNode;
			}

			Molecule[] mols = new Molecule[count];

			int i = 0;
			tempNode = header;

			while (tempNode.hasNext()) {
				tempNode = tempNode.getNext();
				mols[i] = tempNode.getData();
				i++;
			}

			sc.close();

			Database d = new Database(limits, mols, file);

			System.out
					.println("Time:\n" + (System.currentTimeMillis() - start));

			return d;
		} catch (Exception e) {
			if (e instanceof EndFileException) {
				throw new EndFileException(
						"File ended before molecule was finished reading");
			} else if (e instanceof MissingPartException) {
				throw new MissingPartException(
						"Missing information at molecule " + (count + 1) + "\n"
								+ e.getMessage());
			} else {
				throw e;
			}
		}
	}

	private static Database readSDF(String file) throws Exception {
		Scanner sc = null;

		try {
			sc = new Scanner(new File(file));

			int count = 0;
			Node<Molecule> head = new Node<Molecule>(null);
			Node<Molecule> tail = head;
			Molecule temp;
			Node<Molecule> tempNode;

			while (sc.hasNext()) {
				temp = Molecule.readSDF(sc);
				tempNode = new Node<Molecule>(temp);
				tail.setNext(tempNode);
				tail = tempNode;
				count++;
			}

			Molecule[] mol = new Molecule[count];

			int i = 0;
			tempNode = head;

			while (tempNode.hasNext()) {
				tempNode = tempNode.getNext();
				mol[i] = tempNode.getData();
				i++;
			}

			double[] lim = { 0, 0, 0, 0, 0, 0, 0, 0 };

			Database d = new Database(lim, mol, file);
			return d;
		} finally {
			sc.close();
		}
	}

	private double[] limits;
	private Molecule[] mols;
	private boolean changed;
	private String file;
	private boolean[] selected;
	private ArrayList<String> commentList;
	private ArrayList<DataLabel> labelList;
	private int lastClicked;

	private boolean[] displays;

	private Database(double[] l, Molecule[] m, String f) {
		limits = l;
		mols = m;
		file = f;
		changed = false;
		selected = new boolean[mols.length];
		lastClicked = -1;

		createMasterLists();

		displays = new boolean[4];
		displays[0] = true;
		displays[1] = true;
		displays[2] = true;
		displays[3] = true;
	}

	private void createMasterLists() {
		commentList = new ArrayList<String>();
		labelList = new ArrayList<DataLabel>();

		for (Molecule m : mols) {
			m.addToList(labelList, commentList);
		}
	}

	public boolean isSelected(int i) {
		return selected[i];
	}

	public int getNumMols() {
		return mols.length;
	}

	public Molecule getMolecule(int i) {
		return mols[i];
	}

	public Vector3d getMiddle() {
		return new Vector3d((float) ((limits[2] + limits[3]) / 2),
				(float) ((limits[4] + limits[5]) / 2),
				(float) ((limits[6] + limits[7]) / 2));
	}

	public ArrayList<String> getMasterComments() {
		return commentList;
	}

	public ArrayList<DataLabel> getLabel() {
		return labelList;
	}

	public void modifyComment(int mol, String comment, boolean add) {
		changed = true;
		if (!add) {
			mols[mol].removeComment(comment);
			return;
		}

		mols[mol].addComment(comment);

		if (!commentList.contains(comment))
			commentList.add(comment);

		this.setChanged();
		this.notifyObservers();
	}

	public boolean close() {
		if (changed) {
			int choice = JOptionPane.showConfirmDialog(null,
					"Unsaved Changes\nSave?", "Unsaved Changes",
					JOptionPane.YES_NO_CANCEL_OPTION);

			switch (choice) {
			case JOptionPane.YES_OPTION:
				try {
					save();
				} catch (Exception e) {
				}
				break;
			case JOptionPane.CANCEL_OPTION:
				return false;
			case JOptionPane.NO_OPTION:
			default:
				break;
			}
		}

		return true;
	}

	public void save() throws Exception {
		FileWriter os = new FileWriter(file);

		os.write(limits[0] + " " + limits[1] + " " + limits[2] + " "
				+ limits[3] + " " + limits[4] + " " + limits[5] + " "
				+ limits[6] + " " + limits[7]);

		for (int i = 0; i < mols.length; i++) {
			os.write("\n");
			mols[i].saveVSV(os);
		}

		os.close();
		changed = false;
	}

	public void saveAs(String newDest) throws Exception {
		file = newDest;
		save();
	}

	public void setSelected(ArrayList<Point> circ, Point2d[] molLoc,
			boolean invert) {
		if (molLoc == null || molLoc.length != mols.length || circ.size() < 3)
			return;

		int i, j;

		for (int k = 0; k < mols.length; k++) {
			selected[k] = invert;

			if (molLoc[k] == null)
				continue;

			// Parametric type approach to this problem
			for (i = 0, j = circ.size() - 1; i < circ.size(); j = i++) {
				if ((circ.get(i).y > molLoc[k].y) != (circ.get(j).y > molLoc[k].y)
						&& (molLoc[k].x < (circ.get(j).x - circ.get(i).x)
								* (molLoc[k].y - circ.get(i).y)
								/ (circ.get(j).y - circ.get(i).y)
								+ circ.get(i).x)) {
					selected[k] = !selected[k];
				}
			}
		}

		this.setChanged();
		this.notifyObservers();
	}

	public void findClicked(int x, int y, Point2d[] molLoc) {
		double dis = 6;
		double temp;
		int min = -1;

		for (int i = 0; i < molLoc.length; i++) {
			if (molLoc[i] == null)
				continue;

			temp = molLoc[i].distance(new Point2d(x, y));

			if (temp < dis) {
				dis = temp;
				min = i;
			}
		}

		lastClicked = min;

		this.setChanged();
		this.notifyObservers();
	}

	public int getLastClicked() {
		int temp = lastClicked;
		lastClicked = -1;
		return temp;
	}

	public void searchComments() {
		DefaultListModel<String> adder = new DefaultListModel<String>();
		JList<String> comments = new JList<String>(adder);
		comments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		comments.setSelectedIndex(0);
		comments.setVisibleRowCount(5);
		comments.setFixedCellHeight(16);

		for (String c : commentList) {
			adder.addElement(c);
		}

		JScrollPane scroller = new JScrollPane(comments);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		int choice = JOptionPane.showConfirmDialog(null, scroller,
				"Search by Comment", JOptionPane.OK_CANCEL_OPTION);

		String com = comments.getSelectedValue();

		if (choice == JOptionPane.CANCEL_OPTION
				|| comments.getSelectedIndex() < 0)
			return;

		for (int i = 0; i < mols.length; i++) {
			selected[i] = mols[i].hasComment(com);
		}

		this.setChanged();
		this.notifyObservers();
	}

	public void flipScreen(char c) {
		switch (c) {
		case 'P':
			displays[0] = !displays[0];
			break;
		case 'M':
			displays[1] = !displays[1];
			break;
		case 'C':
			displays[2] = !displays[2];
			break;
		case '3':
			displays[3] = !displays[3];
			break;
		}

		this.setChanged();
		this.notifyObservers();
	}

	public boolean display(int i) {
		return displays[i];
	}

	public void createSplit() {
		SplitPanel sp = new SplitPanel(this);

		int choice = JOptionPane.showConfirmDialog(null, sp, "Split By Data",
				JOptionPane.OK_CANCEL_OPTION);

		if (choice != JOptionPane.OK_OPTION)
			return;

		String label = sp.getLabel();
		double value = sp.getValue();
		String name = sp.getName();

		if (name.isEmpty())
			name = label + " - " + value;

		for (int i = 0; i < mols.length; i++) {
			mols[i].split(label, value, name);
		}

		createMasterLists();

		changed = true;
		this.setChanged();
		this.notifyObservers();
	}

	public void save(ArrayList<Integer> display) throws Exception {
		KFileChooser kfc = KFileChooser.create();
		int choice = kfc.save(null, 1);

		if (choice != JFileChooser.APPROVE_OPTION)
			return;

		File f = kfc.getFile();

		if (f.toString().endsWith(".vsv")) {
			FileWriter os = new FileWriter(f);

			os.write(limits[0] + " " + limits[1] + " " + limits[2] + " "
					+ limits[3] + " " + limits[4] + " " + limits[5] + " "
					+ limits[6] + " " + limits[7]);

			for (int i : display) {
				os.write("\n");
				mols[i].saveVSV(os);
			}

			os.close();
		} else {
			FileWriter os = new FileWriter(f);

			JRadioButton d3 = new JRadioButton("3D");
			JRadioButton d2 = new JRadioButton("2D");

			d3.setSelected(true);

			ButtonGroup b = new ButtonGroup();
			b.add(d3);
			b.add(d2);

			Box box = Box.createVerticalBox();
			box.add(new JLabel("SDF File Type:"));
			box.add(d3);
			box.add(d2);

			JOptionPane.showConfirmDialog(null, box, "SDF Save Type",
					JOptionPane.OK_CANCEL_OPTION);

			for (int i : display) {
				if (display.indexOf(i) != 0)
					os.write("\n");
				mols[i].saveSDF(os, d3.isSelected());
			}

			os.close();
		}
	}

	public void compare(int molQuery, boolean d3) {
		String labelName = mols[molQuery].getName() + " - Sim2D";

		if (d3) {
			labelName = mols[molQuery].getName() + " - Sim3D";
		}

		ArrayList<Double> values = new ArrayList<Double>();
		ArrayList<Integer> index = new ArrayList<Integer>();
		double value;

		for (int i = 0; i < mols.length; i++) {
			selected[i] = false;
			value = mols[i].compare(mols[molQuery], d3);

			index.add(i);
			values.add(value);

			mols[i].addNewData(value, labelName);
		}

		ArrayList<Integer> sort = MolSorter.sort(index, values, true);

		for (int i = 0; i < 100 && i < mols.length; i++) {
			selected[sort.get(i)] = true;
		}

		createMasterLists();

		changed = true;
		this.setChanged();
		this.notifyObservers();
	}
}
