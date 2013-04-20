import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.pickfast.behaviors.*;
import com.sun.j3d.utils.universe.*;
import java.awt.*;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;

public class Demo8 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Demo8().createAndShowGUI();
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
        BoundingSphere bounds = new BoundingSphere(new Point3d(),25);
        BoundingSphere bounds2 = new BoundingSphere(new Point3d(),35);

        ViewingPlatform viewingPlatform = new ViewingPlatform();
        viewingPlatform.setCapability(ViewingPlatform.ALLOW_BOUNDS_WRITE);
        viewingPlatform.setBounds(bounds);
        Viewer viewer = new Viewer(canvas3D);

        SimpleUniverse simpleU = new SimpleUniverse(viewingPlatform, viewer);
        simpleU.getViewingPlatform().setNominalViewingTransform();
        Transform3D cam = new Transform3D();
        simpleU.getViewingPlatform().getLocalToVworld(cam);
        Point3f cam_point = new Point3f();
        System.out.println(cam_point);
        cam.transform(cam_point);
        System.out.println(cam_point);
        simpleU.getViewer().getView().setSceneAntialiasingEnable(true);


        BranchGroup scene = new BranchGroup();
        scene.setBounds(bounds);
        addBoundingSphere(bounds,scene);

        Light light = new DirectionalLight(new Color3f(1f, 1f, 1f), new Vector3f(-1f, -1f, -1f));
        light.setInfluencingBounds(bounds);
        //scene.addChild(light);
        light = new DirectionalLight(new Color3f(.2f, .2f, .2f), new Vector3f(1f, 1f, -1f));
        light.setInfluencingBounds(bounds);
        //scene.addChild(light);
        light = new PointLight(true, new Color3f(1f,1f,1f), cam_point, new Point3f(1.0f,1.0f,0.1f) );
        light.setInfluencingBounds(bounds2);
        scene.addChild(light);

        Transform3D t3D = new Transform3D();
        t3D.setTranslation(new Vector3f(-.25f, 0f, 0f));
        TransformGroup torusTG = new TransformGroup(t3D);
        torusTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        torusTG.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        torusTG.addChild(new Torus(.4f, .1f, 60, 30));
        scene.addChild(torusTG);
        t3D = new Transform3D();
        t3D.rotX(Math.PI / 2);
        t3D.setTranslation(new Vector3f(.25f, 0f, 0f));
        torusTG = new TransformGroup(t3D);
        torusTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        torusTG.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        torusTG.addChild(new Torus(.4f, .1f, 60, 30));
        scene.addChild(torusTG);

        Behavior b = new PickRotateBehavior(scene, canvas3D, bounds, PickInfo.PICK_GEOMETRY);
        b.setSchedulingBounds(bounds);
        scene.addChild(b);
        b = new PickTranslateBehavior(scene, canvas3D, bounds, PickInfo.PICK_GEOMETRY);
        b.setSchedulingBounds(bounds);
        scene.addChild(b);
        b = new PickZoomBehavior(scene, canvas3D, bounds, PickInfo.PICK_GEOMETRY);
        b.setSchedulingBounds(bounds);
        scene.addChild(b);
        scene.compile();
        simpleU.addBranchGraph(scene);




        JFrame appFrame = new JFrame("Java 3D Demo 8");
        appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        canvas3D.setPreferredSize(new Dimension(800,600));
        appFrame.add(canvas3D);
        appFrame.pack();
        //if (Toolkit.getDefaultToolkit().isFrameStateSupported(JFrame.MAXIMIZED_BOTH))
            //appFrame.setExtendedState(appFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        appFrame.setVisible(true);
    }
}