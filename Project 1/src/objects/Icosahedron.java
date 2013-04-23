package objects;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * A psychadelic glowing d20.
 */
public class Icosahedron extends Shape3D {

    /**
     * Creates a d20 of approximately radius 1
     */
    public Icosahedron() {
        setGeometry(createGeometry());

        Appearance meshApp = new Appearance();
        RenderingAttributes ra = new RenderingAttributes();
        ra.setIgnoreVertexColors(false);

        meshApp.setRenderingAttributes(ra);

        PolygonAttributes pa = new PolygonAttributes();
        pa.setPolygonMode(PolygonAttributes.POLYGON_FILL);
        pa.setCullFace(PolygonAttributes.CULL_NONE);

        meshApp.setPolygonAttributes(pa);

        Material meshMat = new Material();
        meshMat.setLightingEnable(true);
        meshMat.setDiffuseColor(0f, 1f, 1f);
        meshApp.setMaterial(meshMat);
        meshApp.setColoringAttributes(new ColoringAttributes(0f, 0f, 0f, ColoringAttributes.SHADE_GOURAUD));
        setAppearance(meshApp);
    }

    private Geometry createGeometry() {
        IndexedTriangleArray geometry = new IndexedTriangleArray(12, GeometryArray.COORDINATES | GeometryArray.COLOR_3, 20*3);// | GeometryArray.COLOR_3 | GeometryArray.NORMALS);
        Point3d  points[]  = new Point3d[12];
        int indices[] = new int[20*3];
        Color3f colors[]  = new Color3f[12];
//        Vector3d normals[] = new Vector3d[20*3];

        //a simple way to get the points
        double golden_ratio = (1 + Math.sqrt(5)) / 2d;

        //all combinations http://upload.wikimedia.org/wikipedia/commons/thumb/9/9c/Icosahedron-golden-rectangles.svg/200px-Icosahedron-golden-rectangles.svg.png
        for(int j = 0; j <= 1; j+=1) {
            int j_factor = j*2 - 1; //-1 or 1
            for(int i = 0; i <= 1; i+=1) {
                int i_factor = i*2 - 1; //-1 or 1
                int vertex_number = j * 2 + i+1;

                int loc1 = (j * 2 + i + 4*0);
                int loc2 = (j * 2 + i + 4*1);
                int loc3 = (j * 2 + i + 4*2);

                points[loc1] = new Point3d(0 , i_factor , j_factor*golden_ratio);
                points[loc2] = new Point3d(i_factor , j_factor*golden_ratio , 0);
                points[loc3] = new Point3d(j_factor*golden_ratio , 0 , i_factor);

                float off_color = 1 - vertex_number / 4;

                colors[loc1] = new Color3f(0.5f,off_color,off_color);
                colors[loc2] = new Color3f(off_color,0.5f,off_color);
                colors[loc3] = new Color3f(off_color,off_color,0.5f);


//                System.out.printf("(%d) %4d\t\t\t\t%4d\t\t\t\t%4d\n", vertex_number, loc1, loc2, loc3);
//                System.out.printf("       %s\t\t%s\t\t%s\n", colors[loc1], colors[loc2], colors[loc3]);
            }
        }




        //specify indices - I couldn't find any clever way to do this within the time constraints

        //top
        int i = 0;
        indices[i++] = 4;
        indices[i++] = 8;
        indices[i++] = 0;

        indices[i++] = 5;
        indices[i++] = 4;
        indices[i++] = 0;

        indices[i++] = 10;
        indices[i++] = 5;
        indices[i++] = 0;

        indices[i++] = 1;
        indices[i++] = 10;
        indices[i++] = 0;

        indices[i++] = 8;
        indices[i++] = 1;
        indices[i++] = 0;

        //side1
        indices[i++] = 4;
        indices[i++] = 9;
        indices[i++] = 8;

        indices[i++] = 5;
        indices[i++] = 2;
        indices[i++] = 4;

        indices[i++] = 10;
        indices[i++] = 11;
        indices[i++] = 5;

        indices[i++] = 1;
        indices[i++] = 7;
        indices[i++] = 10;

        indices[i++] = 8;
        indices[i++] = 6;
        indices[i++] = 1;

        //side2
        indices[i++] = 4;
        indices[i++] = 2;
        indices[i++] = 9;

        indices[i++] = 8;
        indices[i++] = 6;
        indices[i++] = 9;

        indices[i++] = 7;
        indices[i++] = 6;
        indices[i++] = 1;

        indices[i++] = 11;
        indices[i++] = 7;
        indices[i++] = 10;

        indices[i++] = 2;
        indices[i++] = 11;
        indices[i++] = 5;

        //bottom
        indices[i++] = 3;
        indices[i++] = 7;
        indices[i++] = 11;

        indices[i++] = 11;
        indices[i++] = 2;
        indices[i++] = 3;

        indices[i++] = 3;
        indices[i++] = 2;
        indices[i++] = 9;

        indices[i++] = 3;
        indices[i++] = 9;
        indices[i++] = 6;

        indices[i++] = 7;
        indices[i++] = 3;
        indices[i++] = 6;



////        //last points
//        indices[3] = 3;
//        indices[4] = 7;
////        indices[5] = 11;
//        for(int i = 0; i < 4; i++) {
//            indices[(i*3)  ] = i;
//            indices[(i*3)+1] = i+1;
//            indices[(i*3)+2] = i+2;
//            indices[(i*3)+3] = i+1;
//            indices[(i*3)+4] = i+2;
//            indices[(i*3)+5] = i+3;
//        }


        //the top two levels

        //points[0] =


//        for(int level = 0; level < 2; level++ ){
//            double radius_X1 = radius * Math.cos(level * Math.PI / 3);
//            double radius_X2 = radius * Math.cos((level+1) * Math.PI / 3);
//            double y1 = radius * Math.sin(level * Math.PI / 3);
//            double y2 = radius * Math.sin((level+1) * Math.PI / 3);
//            double offset_theta1 = level * Math.PI / 5;
//            double offset_theta2 = (level+1) * Math.PI / 5;
//        }

        //the bottom two levels are mirrors of the first


        geometry.setColors(0,colors);
        geometry.setCoordinates(0, points);
        geometry.setCoordinateIndices(0, indices);
        geometry.setColorIndices(0,indices);

        // Utility code to automatically generate normals.
        GeometryInfo gInfo = new GeometryInfo(geometry);
        //new NormalGenerator().generateNormals(gInfo);
        return gInfo.getIndexedGeometryArray();
    }
}
