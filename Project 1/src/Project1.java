import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.pickfast.behaviors.PickRotateBehavior;
import com.sun.j3d.utils.pickfast.behaviors.PickTranslateBehavior;
import com.sun.j3d.utils.pickfast.behaviors.PickZoomBehavior;
import com.sun.j3d.utils.universe.*;
import com.sun.org.apache.bcel.internal.generic.NEW;
import objects.*;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;

public class Project1 {
	private Transform3D torus3D;

    private Point3d focus = new Point3d();
    private Point3d camera = new Point3d(0,0,1);
    private Vector3d up = new Vector3d(0,1,0);
    private static final double DISTANCE = 10d;
    private static final BoundingSphere BOUNDS = new BoundingSphere(new Point3d(),10);

    private static final BoundingSphere BOUNDS2 = new BoundingSphere(new Point3d(),20);
    private InsertBehavior insertBehavior;
    private TransformGroup curTransform;
    private SimpleUniverse simpleU;


    public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Project1().createAndShowGUI();
			}
		});
	}



    private void addBoundingSphere(final BoundingSphere bounds, final BranchGroup scene) {
        Sphere bounding1 = new Sphere((float)bounds.getRadius(), Primitive.GENERATE_NORMALS, (int)(bounds.getRadius() * 2 * Math.PI));
        //Sphere bounding = new Sphere(0.5f, Primitive.GENERATE_NORMALS, 50);

        Appearance appearance = new Appearance();

        Material meshMat = new Material();
        meshMat.setLightingEnable(false);
        appearance.setColoringAttributes(new ColoringAttributes(new Color3f(1f, 1f, 1f), ColoringAttributes.SHADE_GOURAUD));

        PolygonAttributes pa = new PolygonAttributes();
        pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        pa.setCullFace(PolygonAttributes.CULL_FRONT);
        appearance.setPolygonAttributes(pa);
        appearance.setMaterial(meshMat);

        bounding1.setAppearance(appearance);
        bounding1.setPickable(false);
        scene.addChild(bounding1);


        //outer radius   (float)bounds.getRadius()+0.1f
        bounding1 = new Sphere((float)bounds.getRadius()+0.1f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_NORMALS_INWARD, (int)(bounds.getRadius() * 2 * Math.PI));
        //bounding1 = new Sphere(0.5f, Primitive.GENERATE_NORMALS | Primitive.GENERATE_NORMALS_INWARD, (int)(bounds.getRadius() * 2 * Math.PI));
        //Sphere bounding = new Sphere(0.5f, Primitive.GENERATE_NORMALS, 50);

        appearance = new Appearance();

        meshMat = new Material();
        meshMat.setLightingEnable(true);
        meshMat.setEmissiveColor(0.3f,0.0f,0.0f);
        meshMat.setAmbientColor(0.8f, 0.0f, 0.0f);
        meshMat.setDiffuseColor(0.7f, 0.7f, 0.7f);
        meshMat.setSpecularColor(0.7f, 0.7f, 0.7f);
        appearance.setColoringAttributes(new ColoringAttributes(new Color3f(1f, 0f, 0f), ColoringAttributes.SHADE_GOURAUD));

        pa = new PolygonAttributes();
        pa.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        pa.setCullFace(PolygonAttributes.CULL_BACK);
        appearance.setPolygonAttributes(pa);
        appearance.setMaterial(meshMat);

        bounding1.setAppearance(appearance);
        bounding1.setPickable(false);
        scene.addChild(bounding1);
    }

	private void createAndShowGUI() {
		// Fix for background flickering on some platforms
		System.setProperty("sun.awt.noerasebackground", "true");

		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas3D = new Canvas3D(config);
		simpleU = new SimpleUniverse(canvas3D);
		simpleU.getViewingPlatform().setNominalViewingTransform();
		simpleU.getViewer().getView().setSceneAntialiasingEnable(true);

		BranchGroup scene = new BranchGroup();

        addBoundingSphere(BOUNDS, scene);

		torus3D = new Transform3D();
		final TransformGroup torusTG = new TransformGroup();
		torusTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        torusTG.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
		//torusTG.addChild(new Torus(0.4f, 0.1f, 200, 200));
        //torusTG.addChild(new SpikeBomb(0.4f,0.3f));
        //torusTG.addChild(new BoxFace());
        //torusTG.addChild(new IcosahedronPlanes());
        //torusTG.addChild(new Icosahedron());
        torusTG.addChild(new Triforce());
		scene.addChild(torusTG);
        Behavior b = new PickRotateBehavior(scene, canvas3D, BOUNDS, PickInfo.PICK_GEOMETRY);
        b.setSchedulingBounds(BOUNDS);
        scene.addChild(b);
        b = new PickTranslateBehavior(scene, canvas3D, BOUNDS, PickInfo.PICK_GEOMETRY);
        b.setSchedulingBounds(BOUNDS);
        scene.addChild(b);
        b = new PickZoomBehavior(scene, canvas3D, BOUNDS, PickInfo.PICK_GEOMETRY);
        b.setSchedulingBounds(BOUNDS);
        scene.addChild(b);


        //movement
        setupInteraction(scene);
//        MouseRotate mr = new MouseRotate(MouseBehavior.INVERT_INPUT);
//        mr.setTransformGroup(simpleU.getViewingPlatform().getViewPlatformTransform());
//        mr.setSchedulingBounds(BOUNDS);
//        scene.addChild(mr);

        scene.compile();

		simpleU.addBranchGraph(scene);

		JFrame appFrame = new JFrame("Project 1");
		appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		appFrame.add(canvas3D);
        canvas3D.setPreferredSize(new Dimension(800, 600));

//        MenuBar menuBar = new MenuBar();
//        appFrame.setMenuBar(menuBar);
//        menuBar.add(new Menu("Add Shape"));
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200,600));

        //setup insertion selection
        JRadioButton[] shapes = new JRadioButton[8];
        shapes[0] = new JRadioButton(new AddObjectAction("Sphere",new Sphere(),insertBehavior));
        ButtonGroup bg = new ButtonGroup();
        sidebar.add(new JLabel("What shape should be inserted?"));
        for(int i = 0; i < shapes.length; i++) {
            if(shapes[i] != null) {
                bg.add(shapes[i]);
                sidebar.add(shapes[i]);
            }
        }
        shapes[0].setSelected(true);
        appFrame.add(sidebar, BorderLayout.EAST);


		appFrame.pack();






//		if (Toolkit.getDefaultToolkit().isFrameStateSupported(JFrame.MAXIMIZED_BOTH))
//			appFrame.setExtendedState(appFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

//		canvas3D.addMouseMotionListener(new MouseMotionAdapter() {
//			private MouseEvent lastDragEvent;
//
//			public void mouseDragged(MouseEvent e) {
//				if (lastDragEvent != null) {
//					Transform3D newRotate = new Transform3D();
//					newRotate.rotX(Math.toRadians(e.getY() - lastDragEvent.getY()) / 2);
//					Transform3D tmp = new Transform3D();
//					tmp.rotY(Math.toRadians(e.getX() - lastDragEvent.getX()) / 2);
//					newRotate.mul(tmp);
//					newRotate.mul(torus3D);
//					torus3D = newRotate;
//					torusTG.setTransform(torus3D);
//				}
//				lastDragEvent = e;
//			}
//			public void mouseMoved(MouseEvent e) {
//				lastDragEvent = null;
//			}});
		
		appFrame.setVisible(true);
	}

    private void setupInteraction(BranchGroup scene) {

        //lighting moves
        scene.setBounds(BOUNDS2);
        Light light = new DirectionalLight(new Color3f(0.2f, 0.2f, 0.2f), new Vector3f(-1f, -1f, -1f));
        light.setInfluencingBounds(BOUNDS2);
        //scene.addChild(light);
        light = new PointLight(new Color3f(1f, 1f, 1f), new Point3f(0,0,0), new Point3f(2f,0f,0f));
        light.setInfluencingBounds(BOUNDS2);
        TransformGroup lightTransform = new TransformGroup();
        lightTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        lightTransform.addChild(light);
//        light = new DirectionalLight(new Color3f(.2f, .2f, .2f), new Vector3f(0f, 0f, -1f));
//        light.setInfluencingBounds(BOUNDS);
//        lightTransform.addChild(light);
        scene.addChild(lightTransform);




        //////////////////////////
        //insertion and movement//
        //////////////////////////
        //insertion point
        curTransform = new TransformGroup();
        curTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Appearance sa = new Appearance();
        sa.setColoringAttributes(new ColoringAttributes(0f, 0f, 0f, ColoringAttributes.SHADE_GOURAUD));
        curTransform.addChild(new Sphere(0.1f,sa));
        scene.addChild(curTransform);
        FlyCam fc = new FlyCam(simpleU.getViewingPlatform().getViewPlatformTransform(),focus,camera,up,DISTANCE, lightTransform, curTransform);
        fc.setSchedulingBounds(BOUNDS2);
        scene.addChild(fc);
        insertBehavior = new InsertBehavior(scene,curTransform);
        insertBehavior.setSchedulingBounds(BOUNDS2);
        scene.addChild(insertBehavior);
        scene.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
    }

    private class AddObjectAction extends AbstractAction {
        Node node;
        public AddObjectAction(String text, Node node, Object insertBehavior) {
            super(text);
            this.node = node;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}
