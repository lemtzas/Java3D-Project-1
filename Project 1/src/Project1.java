import com.sun.j3d.utils.universe.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;

public class Project1 {
	private Transform3D torus3D;

	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Project1().createAndShowGUI();
			}
		});
	}

	private void createAndShowGUI() {
		// Fix for background flickering on some platforms
		System.setProperty("sun.awt.noerasebackground", "true");

		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas3D = new Canvas3D(config);
		SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
		simpleU.getViewingPlatform().setNominalViewingTransform();
		simpleU.getViewer().getView().setSceneAntialiasingEnable(true);
		
		BranchGroup scene = new BranchGroup();
        BoundingSphere bounds = new BoundingSphere(new Point3d(),100);
        scene.setBounds(bounds);
		Light light = new DirectionalLight(new Color3f(1f, 1f, 1f), new Vector3f(-1f, -1f, -1f));
		light.setInfluencingBounds(bounds);
		scene.addChild(light);
		light = new DirectionalLight(new Color3f(.2f, .2f, .2f), new Vector3f(1f, 1f, -1f));
		light.setInfluencingBounds(bounds);
		scene.addChild(light);

		torus3D = new Transform3D();
		final TransformGroup torusTG = new TransformGroup();
		torusTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		torusTG.addChild(new Torus(0.4f, 0.1f, 200, 200));
		scene.addChild(torusTG);
		scene.compile();
		simpleU.addBranchGraph(scene);

		JFrame appFrame = new JFrame("Java 3D Demo 5");
		appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		appFrame.add(canvas3D);
		appFrame.pack();
		if (Toolkit.getDefaultToolkit().isFrameStateSupported(JFrame.MAXIMIZED_BOTH))
			appFrame.setExtendedState(appFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		
		canvas3D.addMouseMotionListener(new MouseMotionAdapter() {
			private MouseEvent lastDragEvent;
			
			public void mouseDragged(MouseEvent e) {
				if (lastDragEvent != null) {
					Transform3D newRotate = new Transform3D();
					newRotate.rotX(Math.toRadians(e.getY() - lastDragEvent.getY()) / 2);
					Transform3D tmp = new Transform3D();
					tmp.rotY(Math.toRadians(e.getX() - lastDragEvent.getX()) / 2);
					newRotate.mul(tmp);
					newRotate.mul(torus3D);
					torus3D = newRotate;
					torusTG.setTransform(torus3D);
				}
				lastDragEvent = e;
			}
			public void mouseMoved(MouseEvent e) {
				lastDragEvent = null;
			}});
		
		appFrame.setVisible(true);
	}
}
