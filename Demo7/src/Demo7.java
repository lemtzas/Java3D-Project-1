import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.universe.*;
import java.awt.*;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;

public class Demo7 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Demo7().createAndShowGUI();
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
        Light light = new DirectionalLight(new Color3f(1f, 1f, 1f), new Vector3f(-1f, -1f, -1f));
        light.setInfluencingBounds(new BoundingSphere());
        scene.addChild(light);
        light = new DirectionalLight(new Color3f(.2f, .2f, .2f), new Vector3f(1f, 1f, -1f));
        light.setInfluencingBounds(new BoundingSphere());
        scene.addChild(light);

        TransformGroup torusTG = new TransformGroup();
        torusTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        torusTG.addChild(new Torus(.5f, .2f, 60, 30));
        scene.addChild(torusTG);
        Behavior b = new MouseRotate(torusTG);
        b.setSchedulingBounds(new BoundingSphere());
        scene.addChild(b);
        b = new MouseTranslate(torusTG);
        b.setSchedulingBounds(new BoundingSphere());
        scene.addChild(b);
        b = new MouseZoom(torusTG);
        b.setSchedulingBounds(new BoundingSphere());
        scene.addChild(b);
        scene.compile();
        simpleU.addBranchGraph(scene);

        JFrame appFrame = new JFrame("Java 3D Demo 7");
        appFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        appFrame.add(canvas3D);
        appFrame.pack();
        if (Toolkit.getDefaultToolkit().isFrameStateSupported(JFrame.MAXIMIZED_BOTH))
            appFrame.setExtendedState(appFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        appFrame.setVisible(true);
    }
}