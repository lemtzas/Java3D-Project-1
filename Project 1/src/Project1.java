import com.sun.j3d.utils.behaviors.mouse.MouseBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Cone;
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
    private Canvas3D canvas3D;


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
		canvas3D = new Canvas3D(config);
		simpleU = new SimpleUniverse(canvas3D);
		simpleU.getViewingPlatform().setNominalViewingTransform();
		simpleU.getViewer().getView().setSceneAntialiasingEnable(true);

		BranchGroup scene = new BranchGroup();

        addBoundingSphere(BOUNDS, scene);

//		torus3D = new Transform3D();
//		final TransformGroup torusTG = new TransformGroup();
//		torusTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//        torusTG.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
//		//torusTG.addChild(new Torus(0.4f, 0.1f, 200, 200));
//        //torusTG.addChild(new SpikeBomb(0.4f,0.3f));
//        //torusTG.addChild(new BoxFace());
//        //torusTG.addChild(new IcosahedronPlanes());
//        //torusTG.addChild(new Icosahedron());
//        torusTG.addChild(new Triforce());
//		scene.addChild(torusTG);

        //REPLACED BY CamGrabBehavior
//        b = new PickTranslateBehavior(scene, canvas3D, BOUNDS, PickInfo.PICK_GEOMETRY);
//        b.setSchedulingBounds(BOUNDS);
//        scene.addChild(b);
//        b = new PickZoomBehavior(scene, canvas3D, BOUNDS, PickInfo.PICK_GEOMETRY);
//        b.setSchedulingBounds(BOUNDS);
//        scene.addChild(b);


        //movement
        setupInteraction(scene);
//        MouseRotate mr = new MouseRotate(MouseBehavior.INVERT_INPUT);
//        mr.setTransformGroup(simpleU.getViewingPlatform().getViewPlatformTransform());
//        mr.setSchedulingBounds(BOUNDS);
//        scene.addChild(mr);

        scene.compile();

		simpleU.addBranchGraph(scene);

		JFrame appFrame = new JFrame("Project 1 - David Sharer");
		appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		appFrame.add(canvas3D);
        canvas3D.setPreferredSize(new Dimension(800, 600));

//        MenuBar menuBar = new MenuBar();
//        appFrame.setMenuBar(menuBar);
//        menuBar.add(new Menu("Add Shape"));
        appFrame.add(generateSidebar(), BorderLayout.EAST);




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

    private JPanel generateSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar,BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200,600));

        //setup insertion selection
        JRadioButton[] shapes = new JRadioButton[9];
        ButtonGroup bg = new ButtonGroup();
        sidebar.add(new JSeparator());
        sidebar.add(new JSeparator());
        sidebar.add(new JLabel("What shape should be inserted?"));
        sidebar.add(new JSeparator());
        sidebar.add(new JLabel("Basic"));
        int j = 0;
        shapes[j] = new JRadioButton(new AddObjectAction("(util) Sphere",new Sphere(),insertBehavior));
        bg.add(shapes[j]); sidebar.add(shapes[j]);j++;
        shapes[j] = new JRadioButton(new AddObjectAction("(util) Cone",new Cone(),insertBehavior));
        bg.add(shapes[j]); sidebar.add(shapes[j]);j++;
        shapes[j] = new JRadioButton(new AddObjectAction("(util) Color Cube",new ColorCube(),insertBehavior));
        bg.add(shapes[j]); sidebar.add(shapes[j]);j++;
        shapes[j] = new JRadioButton(new AddObjectAction("(class) Torus",new Torus(.4f,0.2f,20,20),insertBehavior));
        bg.add(shapes[j]); sidebar.add(shapes[j]);j++;
        shapes[j] = new JRadioButton(new AddObjectAction("Triforce",new Triforce(),insertBehavior));
        bg.add(shapes[j]); sidebar.add(shapes[j]);j++;
        shapes[j] = new JRadioButton(new AddObjectAction("Icosahedron",new Icosahedron(),insertBehavior));
        bg.add(shapes[j]); sidebar.add(shapes[j]);j++;
        shapes[j] = new JRadioButton(new AddObjectAction("Icosahedron Planes",new IcosahedronPlanes(),insertBehavior));
        bg.add(shapes[j]); sidebar.add(shapes[j]);j++;
        sidebar.add(new JLabel("Compound"));
        shapes[j] = new JRadioButton(new AddObjectAction("Bowser Bomb",new SpikeBomb(1f),insertBehavior));
        bg.add(shapes[j]); sidebar.add(shapes[j]);j++;
        shapes[j] = new JRadioButton(new AddObjectAction("Face",new BoxFace(),insertBehavior));
        bg.add(shapes[j]); sidebar.add(shapes[j]);j++;
        shapes[0].setSelected(true);

        //the instructions
        sidebar.add(new JSeparator());
        sidebar.add(new JSeparator());
        sidebar.add(new JLabel("Instructions"));
        sidebar.add(new JSeparator());

        JTextArea explanation = new JTextArea();
        explanation.setEditable(false);
        explanation.setWrapStyleWord(true);
        explanation.setLineWrap(true);
        explanation.append("WASD for planar movement (XZ)\n");
        explanation.append("QE for up-down movement (Y)\n");
        explanation.append("Right click and drag to look\n");
        explanation.append("Left click to \"grab\" an object\n");
        explanation.append("2 to \"release\" the objects\n");
        explanation.append("Left click and drag to rotate an object\n");
        explanation.append("\n");
        explanation.append("1 to insert an object (\"grabbed\")\n");
        explanation.append("2 to place \"grabbed\" objects\n");
        explanation.append("3 to delete \"grabbed\" objects\n");
        explanation.append("\n");
        explanation.append("You have a light (always on)\n");
        explanation.append("The black ball is your \"cursor\".\n");
        explanation.append("\n");
        explanation.append("The rendering area must be selected to move.\n");
        sidebar.add(explanation);

        //left align everything, for style
        for(Component c : sidebar.getComponents()) {
            if(c instanceof JComponent) {
                JComponent jc = (JComponent)c;
                jc.setAlignmentX(Component.LEFT_ALIGNMENT);
            }
        }
        return sidebar;
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
        curTransform.setName("curTransform");
        curTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        curTransform.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
        curTransform.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
        curTransform.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
        curTransform.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        Appearance sa = new Appearance();
        sa.setColoringAttributes(new ColoringAttributes(0f, 0f, 0f, ColoringAttributes.SHADE_GOURAUD));
        curTransform.addChild(new Sphere(0.1f,sa));
        scene.addChild(curTransform);

        //movement
        FlyCam fc = new FlyCam(simpleU.getViewingPlatform().getViewPlatformTransform(),focus,camera,up,DISTANCE, lightTransform, curTransform);
        fc.setSchedulingBounds(BOUNDS2);
        scene.addChild(fc);

        //insertion
        insertBehavior = new InsertBehavior(scene,curTransform);
        insertBehavior.setSchedulingBounds(BOUNDS2);
        scene.addChild(insertBehavior);
        scene.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);

        //selection
        CamGrabBehavior camGrabBehavior = new CamGrabBehavior(canvas3D, scene, insertBehavior);
        camGrabBehavior.setSchedulingBounds(BOUNDS2);
        scene.addChild(camGrabBehavior);

        //rotation
        Behavior b = new PickRotateBehavior(scene, canvas3D, BOUNDS, PickInfo.PICK_GEOMETRY);
        b.setSchedulingBounds(BOUNDS);
        scene.addChild(b);
    }

    private class AddObjectAction extends AbstractAction {
        Node node;
        InsertBehavior insertBehavior;
        public AddObjectAction(String text, Node node, InsertBehavior insertBehavior) {
            super(text);
            this.node = node;
            this.insertBehavior = insertBehavior;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            insertBehavior.setObject(node);
        }
    }
}
