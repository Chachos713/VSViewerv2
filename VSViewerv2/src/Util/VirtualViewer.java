package Util;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Group;
import javax.media.j3d.LineArray;
import javax.media.j3d.PointLight;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.behaviors.vp.ViewPlatformBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * A simple 3D viewer that is meant to be protable between programs. This is not
 * meant to only work within this project.
 * 
 * @author Kyle Diller
 *
 */
@SuppressWarnings("serial")
public class VirtualViewer extends JPanel {
	private static final boolean DEBUG = false;

	private ArrayList<BranchGroup> objects;

	private TransformGroup sceneRotate;
	private Background backg;
	private BoundingSphere bounds;

	/**
	 * Creates the 3D view for the 3D viewer.
	 * 
	 * @param middle
	 *            the point of rotation, but does not work.
	 */
	public VirtualViewer(Point3d middle) {
		setLayout(new BorderLayout());
		GraphicsConfiguration config = SimpleUniverse
				.getPreferredConfiguration();
		Canvas3D canvas3D = new Canvas3D(config);
		this.add(BorderLayout.CENTER, canvas3D);
		SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
		simpleU.getViewingPlatform().setNominalViewingTransform();

		Vector3d v3d = new Vector3d(middle);
		v3d.z = 2 * v3d.z;
		Transform3D move = new Transform3D();
		move.setTranslation(v3d);
		ViewPlatformBehavior centerer = new OrbitBehavior();
		centerer.setHomeTransform(move);
		simpleU.getViewingPlatform().setViewPlatformBehavior(centerer);

		simpleU.addBranchGraph(setUpScene(middle));

		objects = new ArrayList<BranchGroup>();

		this.setPreferredSize(new Dimension(1000, 1000));

		// Used for debugging purposes
		if (DEBUG) {
			Point3d center = new Point3d();
			double radius = 10;
			bounds.getCenter(center);
			Color[] cols = { Color.blue, Color.red, Color.green };
			addAxis(center, radius, cols);

			center = new Point3d();
			cols[0] = Color.cyan;
			cols[1] = Color.pink;
			cols[2] = Color.orange;
			addAxis(center, radius, cols);
		}
	}

	/**
	 * Adds 3 lines to represent the x, y, and z axis that help with debug
	 * purpose
	 * 
	 * @param center
	 *            the intersection of the 3 axis
	 * @param radius
	 *            the length of each axis
	 * @param col
	 *            the colors of the axis
	 */
	private void addAxis(Point3d center, double radius, Color[] col) {
		System.out.println(center);

		Point3f xMin = new Point3f((float) (center.x - radius),
				(float) center.y, (float) center.z);
		Point3f xMax = new Point3f((float) (center.x + radius),
				(float) center.y, (float) center.z);
		Color3f xColor = new Color3f(col[0]);

		Point3f yMin = new Point3f((float) center.x,
				(float) (center.y - radius), (float) center.z);
		Point3f yMax = new Point3f((float) center.x,
				(float) (center.y + radius), (float) center.z);
		Color3f yColor = new Color3f(col[1]);

		Point3f zMin = new Point3f((float) center.x, (float) center.y,
				(float) (center.z - radius));
		Point3f zMax = new Point3f((float) center.x, (float) center.y,
				(float) (center.z + radius));
		Color3f zColor = new Color3f(col[2]);

		LineArray xAxis = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		xAxis.setCoordinate(0, xMin);
		xAxis.setCoordinate(1, xMax);
		xAxis.setColor(0, xColor);
		xAxis.setColor(1, xColor);

		LineArray yAxis = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		yAxis.setCoordinate(0, yMin);
		yAxis.setCoordinate(1, yMax);
		yAxis.setColor(0, yColor);
		yAxis.setColor(1, yColor);

		LineArray zAxis = new LineArray(2, LineArray.COORDINATES
				| LineArray.COLOR_3);
		zAxis.setCoordinate(0, zMin);
		zAxis.setCoordinate(1, zMax);
		zAxis.setColor(0, zColor);
		zAxis.setColor(1, zColor);

		Shape3D shape = new Shape3D();
		shape.addGeometry(xAxis);
		shape.addGeometry(yAxis);
		shape.addGeometry(zAxis);

		BranchGroup axis = new BranchGroup();
		axis.addChild(shape);
		addBranchGroup(axis);
	}

	/**
	 * Creates the form to add to the viewer.
	 * 
	 * @param middle
	 *            the middle of the screen
	 * @return a brachgroup to add objects to
	 */
	private BranchGroup setUpScene(Point3d middle) {
		BranchGroup scene = new BranchGroup();
		scene.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		scene.setCapability(Group.ALLOW_CHILDREN_WRITE);
		scene.setCapability(BranchGroup.ALLOW_DETACH);

		bounds = new BoundingSphere(middle, 100.0D);

		backg = new Background(0.0F, 0.0F, 0.0F);
		backg.setCapability(Background.ALLOW_COLOR_WRITE);
		backg.setApplicationBounds(bounds);
		scene.addChild(backg);

		sceneRotate = new TransformGroup();
		sceneRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		sceneRotate.setCapability(Group.ALLOW_CHILDREN_WRITE);
		sceneRotate.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		scene.addChild(sceneRotate);

		createLights(scene, bounds);

		return scene;
	}

	/**
	 * Creates the lighting for the 3D view
	 * 
	 * @param scene
	 *            the scene to light up
	 * @param bounds
	 *            the area to light
	 */
	private void createLights(BranchGroup scene, BoundingSphere bounds) {
		Point3f pLightDir = new Point3f(1.0F, 1.0F, 1.0F);
		PointLight pLight = new PointLight();
		pLight.setPosition(pLightDir);
		pLight.setInfluencingBounds(bounds);

		Color3f aColor = new Color3f(0.3F, 0.3F, 0.3F);
		AmbientLight ambientLight = new AmbientLight(aColor);
		ambientLight.setInfluencingBounds(bounds);
		ambientLight.setInfluencingBounds(bounds);
		scene.addChild(ambientLight);
		scene.addChild(pLight);
	}

	/**
	 * Sets the form to accept the keys to move the form
	 */
	public void addKeyBehavior() {
		KeyNavigatorBehavior kBehavior = new KeyNavigatorBehavior(sceneRotate);
		kBehavior.setSchedulingBounds(bounds);

		BranchGroup b = new BranchGroup();
		b.addChild(kBehavior);

		sceneRotate.addChild(b);
	}

	/**
	 * Adds the wheel zoom to the view
	 */
	public void addZoom() {
		MouseWheelZoom mzBehavior = new MouseWheelZoom(sceneRotate);
		mzBehavior.setSchedulingBounds(bounds);

		BranchGroup b = new BranchGroup();
		b.addChild(mzBehavior);

		sceneRotate.addChild(b);
	}

	/**
	 * Allows the user to rotate the view
	 */
	public void addRotate() {
		MouseRotate mrBehavior = new MouseRotate(sceneRotate);
		mrBehavior.setSchedulingBounds(bounds);

		BranchGroup b = new BranchGroup();
		b.addChild(mrBehavior);

		sceneRotate.addChild(b);
	}

	/**
	 * Allows the user to move the view
	 */
	public void addMove() {
		MouseTranslate mtBehavior = new MouseTranslate(sceneRotate);
		mtBehavior.setSchedulingBounds(bounds);

		BranchGroup b = new BranchGroup();
		b.addChild(mtBehavior);

		sceneRotate.addChild(b);
	}

	/**
	 * Adds an object to the view
	 * 
	 * @param b
	 *            the object to add
	 */
	public void addBranchGroup(BranchGroup b) {
		if (!b.isCompiled()) {
			b.setCapability(Group.ALLOW_CHILDREN_WRITE);
			b.setCapability(Group.ALLOW_CHILDREN_EXTEND);
			b.setCapability(BranchGroup.ALLOW_DETACH);
		}
		b.compile();

		sceneRotate.addChild(b);
		objects.add(b);
	}

	/**
	 * Changes the center of the view
	 * 
	 * @param p
	 *            the new center
	 */
	public void setCenter(Point3d p) {
		bounds.setCenter(p);
		BranchGroup b = new BranchGroup();
		b.setBounds(bounds);
		b.setCapability(BranchGroup.ALLOW_DETACH);
		b.compile();
		sceneRotate.moveTo(b);
		this.repaint();
	}

	/**
	 * Removes an object from the view
	 * 
	 * @param b
	 *            the object to remove
	 */
	public void removeBranchGroup(BranchGroup b) {
		objects.remove(b);
		sceneRotate.removeChild(b);
	}

	/**
	 * Changes the background color of the view
	 * 
	 * @param c
	 *            the color to set
	 */
	public void setColor(Color c) {
		Color3f color = new Color3f(c);
		backg.setColor(color);
	}

	/**
	 * @return the current background color
	 */
	public Color getColor() {
		Color3f c = new Color3f();
		backg.getColor(c);
		return c.get();
	}

	/**
	 * Gets a screen capture of the current view
	 * 
	 * @param width
	 *            the width of the image
	 * @param height
	 *            the height of the image
	 * @return the image of the view
	 */
	public BufferedImage getSnapshot(int width, int height) {
		Robot r;
		try {
			r = new Robot();

			r.delay(500);
			BufferedImage bi = r.createScreenCapture(new java.awt.Rectangle(
					(int) this.getLocationOnScreen().getX(), (int) this
							.getLocationOnScreen().getY(),
					this.getBounds().width, this.getBounds().height));

			return bi;
		} catch (AWTException e) {
			return null;
		}
	}

	/**
	 * Transforms the view through code
	 * 
	 * @param trans
	 *            the set of transformations
	 */
	public void transform(Transform3D trans) {
		sceneRotate.setTransform(trans);
	}
}
