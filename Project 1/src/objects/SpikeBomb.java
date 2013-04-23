package objects;

import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;

import javax.media.j3d.*;
import javax.vecmath.Vector3d;

/**
 * The first of two compound objects, a spikey bomb.
 */
public class SpikeBomb extends BranchGroup {
    private final Sphere ball;
    private final TransformGroup[] spikeGroups= new TransformGroup[6];
    private final Cone[] spikes = new Cone[6];

    public SpikeBomb(float radius) {
        this(radius,radius);
    }
    public SpikeBomb(float radius, float spikeSize) {
        this(radius, (int)(radius * 2 * Math.PI * 20), spikeSize);
    }

    public SpikeBomb(float radius, int divisions, float spikeSize) {

        Appearance ballAppearance = new Appearance();
        Material ballMat = new Material();
        ballMat.setLightingEnable(true);
        ballMat.setAmbientColor(0f,0f,0f);
        ballMat.setDiffuseColor(0f,0f,0f);
        ballMat.setSpecularColor(1f,1f,1f);
        ballMat.setShininess(200f);
        ballAppearance.setMaterial(ballMat);
        ball = new Sphere(radius, Primitive.GENERATE_NORMALS, divisions,ballAppearance);

        Appearance spikeAppearance = new Appearance();
        Material spikeMat = new Material();
        spikeMat.setLightingEnable(true);
        spikeMat.setAmbientColor(0.2f,0.2f,0.2f);
        spikeMat.setDiffuseColor(1f,1f,1f);
        spikeMat.setSpecularColor(0f,0f,0f);
        spikeMat.setShininess(1f);
        spikeAppearance.setMaterial(spikeMat);

        float length = spikeSize;
        float width = radius/3;

        Transform3D t3d = new Transform3D();
        Transform3D slide = new Transform3D();
        slide.setTranslation(new Vector3d(0, 0.9 * radius + spikeSize / 2, 0)); //point out
        Transform3D rot3d = new Transform3D();

        //top
        t3d.set(rot3d);t3d.mul(slide);
        spikeGroups[0] = new TransformGroup(t3d);
        spikes[0] = new Cone(width,length,spikeAppearance);
        spikeGroups[0].addChild(spikes[0]);
        addChild(spikeGroups[0]);

        //bottom
        rot3d.rotX(Math.PI);
        t3d.set(rot3d);t3d.mul(slide);
        spikes[1] = new Cone(width,length,spikeAppearance);
        spikeGroups[1] = new TransformGroup(t3d);
        spikeGroups[1].addChild(spikes[1]);
        addChild(spikeGroups[1]);

        //front
        rot3d.rotX(Math.PI/2);
        t3d.set(rot3d);t3d.mul(slide);
        spikes[2] = new Cone(width,length,spikeAppearance);
        spikeGroups[2] = new TransformGroup(t3d);
        spikeGroups[2].addChild(spikes[2]);
        addChild(spikeGroups[2]);

        //back
        rot3d.rotX(3 * Math.PI / 2);
        t3d.set(rot3d);t3d.mul(slide);
        spikes[3] = new Cone(width,length,spikeAppearance);
        spikeGroups[3] = new TransformGroup(t3d);
        spikeGroups[3].addChild(spikes[3]);
        addChild(spikeGroups[3]);

        //right
        rot3d.rotZ(Math.PI / 2);
        t3d.set(rot3d);t3d.mul(slide);
        spikes[4] = new Cone(width,length,spikeAppearance);
        spikeGroups[4] = new TransformGroup(t3d);
        spikeGroups[4].addChild(spikes[4]);
        addChild(spikeGroups[4]);

        //left
        rot3d.rotZ(3 * Math.PI / 2);
        t3d.set(rot3d);t3d.mul(slide);
        spikes[5] = new Cone(width,length,spikeAppearance);
        spikeGroups[5] = new TransformGroup(t3d);
        spikeGroups[5].addChild(spikes[5]);
        addChild(spikeGroups[5]);



        this.addChild(ball);
    }
}
