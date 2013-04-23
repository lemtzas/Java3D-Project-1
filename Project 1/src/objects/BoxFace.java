package objects;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Sphere;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/**
 * radius 0.5 boxy face
 */
public class BoxFace extends TransformGroup {
    private static final float HALFWIDTH = 0.5f;
    public BoxFace() {
        //head
        addChild(new ColorCube(0.5));
        //nose
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(new Vector3f(0,0,HALFWIDTH));
        TransformGroup tg = new TransformGroup(t3d);
        tg.addChild(new Sphere(HALFWIDTH/8));
        addChild(tg);
        //eye1
        t3d = new Transform3D();
        t3d.setTranslation(new Vector3f(HALFWIDTH/2,HALFWIDTH/2,HALFWIDTH));
        tg = new TransformGroup(t3d);
        tg.addChild(new Sphere(HALFWIDTH/4));
        addChild(tg);
        //eye2
        t3d = new Transform3D();
        t3d.setTranslation(new Vector3f(-HALFWIDTH/2,HALFWIDTH/2,HALFWIDTH));
        tg = new TransformGroup(t3d);
        tg.addChild(new Sphere(HALFWIDTH/4));
        addChild(tg);
        //mouth
        t3d = new Transform3D();
        t3d.rotY(Math.PI);
        t3d.setTranslation(new Vector3f(0,-HALFWIDTH/2,HALFWIDTH - (HALFWIDTH/5)));
        t3d.setScale(new Vector3d(0.9f,0.25f,0.25f));
        tg = new TransformGroup(t3d);
        tg.addChild(new ColorCube(HALFWIDTH));
        addChild(tg);
    }
}
