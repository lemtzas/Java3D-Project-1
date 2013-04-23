package objects;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/**
 * A "Triforce" made of subdivided triangles. The subdivision is to improve the lighting.
 *
 * The subdivision code is based off the sierpinski triangle code developed in class.
 */
public class Triforce extends BranchGroup {
    /**
     * Creates a "Triforce" with 5 subdivisions
     */
    public Triforce() {
        this(5);
    }
    /**
     * Creates a "Triforce" with the specified number of recursive divisions
     */
    public Triforce(int divisions) {
        for (int i = 0; i < 3; i++) {
            Transform3D t3d = new Transform3D();
            t3d.setScale(.5f);
            t3d.setTranslation(new Vector3f((float)-Math.sin(i * 2 * Math.PI / 3) / 2, (float)Math.cos(i * 2 * Math.PI / 3) / 2, 0f));
            TransformGroup tg = new TransformGroup(t3d);
            tg.addChild(createFilledSierpinksi(divisions - 1));
            this.addChild(tg);
        }
    }

    /**
     * Creates a filled triangle that is subdivided using a method similar to a Sierpinski triangle.
     *
     * Based off the code from class.
     * @param divisions
     * @return
     */
    private Node createFilledSierpinksi(int divisions) {
        if (divisions == 1) {
            Point3f[] points = new Point3f[3];
            Color3f[] colors = new Color3f[3];
            Vector3f[] normals = new Vector3f[3];

            for (int i = 0; i < points.length; i++) {
                points[i] = new Point3f((float)-Math.sin(i * 2 * Math.PI / 3), (float)Math.cos(i * 2 * Math.PI / 3), 0f);
                colors[i] = new Color3f(0xB9/255f, 0xA2/255f, 0x3C/255f); //#B9A23C (a triforce-y color)
                normals[i] = new Vector3f(0f, 0f, 1f);
            }

            TriangleArray geometry = new TriangleArray(3, GeometryArray.COORDINATES | GeometryArray.COLOR_3 | GeometryArray.NORMALS);

            int geometryIndex = 0;
            for (int i = 0; i < points.length; i++) {
                geometry.setCoordinate(geometryIndex, points[i]);
                geometry.setColor(geometryIndex, colors[i]);
                geometry.setNormal(geometryIndex++, normals[i]);
            }

            Shape3D mesh = new Shape3D(geometry);
            Appearance meshApp = new Appearance();
            PolygonAttributes polyAttr = new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 0, true);
            meshApp.setPolygonAttributes(polyAttr);
            Material meshMat = new Material();
            meshApp.setMaterial(meshMat);
            mesh.setAppearance(meshApp);
            return mesh;
        } else {
            BranchGroup bg = new BranchGroup();
            for (int i = 0; i < 3; i++) {
                Transform3D t3d = new Transform3D();
                t3d.setScale(.5f);
                t3d.setTranslation(new Vector3f((float)-Math.sin(i * 2 * Math.PI / 3) / 2, (float)Math.cos(i * 2 * Math.PI / 3) / 2, 0f));
                TransformGroup tg = new TransformGroup(t3d);
                tg.addChild(createFilledSierpinksi(divisions - 1));
                bg.addChild(tg);
            }

            Transform3D t3d = new Transform3D();
            t3d.setScale(new Vector3d(.5,-.5,-.5));
            TransformGroup tg = new TransformGroup(t3d);
            tg.addChild(createFilledSierpinksi(divisions - 1));
            bg.addChild(tg);

            return bg;
        }
    }
}
