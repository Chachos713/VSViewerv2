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
import javax.media.j3d.PointLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
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
		add("Center", canvas3D);
		SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
		simpleU.getViewingPlatform().setNominalViewingTransform();

		simpleU.addBranchGraph(setUpScene(middle));

		objects = new ArrayList<BranchGroup>();

		this.setPreferredSize(new Dimension(1000, 1000));
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
