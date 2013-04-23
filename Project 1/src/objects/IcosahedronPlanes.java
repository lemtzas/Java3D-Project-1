package objects;

import com.sun.j3d.utils.geometry.GeometryInfo;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

/**
 * The 3, golden-ratio rectangles used to produce the points of a d20.
 */
public class IcosahedronPlanes extends Shape3D {
    /**
     * Creates three intersecting planes with a radius of approximately 1
     */
    public IcosahedronPlanes() {
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
        meshApp.setColoringAttributes(new ColoringAttributes(0f, 1f, 1f, ColoringAttributes.SHADE_GOURAUD));
        setAppearance(meshApp);
    }

    private Geometry createGeometry() {
        IndexedTriangleArray geometry = new IndexedTriangleArray(12, GeometryArray.COORDINATES | GeometryArray.COLOR_3, 6*3);// | GeometryArray.COLOR_3 | GeometryArray.NORMALS);
        Point3d  points[]  = new Point3d[12];
        int indices[] = new int[6*3];
        Color3f colors[]  = new Color3f[12];
//        Vector3d normals[] = new Vector3d[20*3];

        //computation trick
        double golden_ratio = (1 + Math.sqrt(5)) / 2d;

        //all combinations http://upload.wikimedia.org/wikipedia/commons/thumb/9/9c/Icosahedron-golden-rectangles.svg/200px-Icosahedron-golden-rectangles.svg.png
        for(int i = 0; i <= 1; i+=1) {
            int i_factor = i*2 - 1; //-1 or 1
            for(int j = 0; j <= 1; j+=1) {
                int j_factor = j*2 - 1; //-1 or 1
                int vertex_number = i * 2 + j;

                int loc1 = (i * 2 + j + 4*0);
                int loc2 = (i * 2 + j + 4*1);
                int loc3 = (i * 2 + j + 4*2);

                points[loc1] = new Point3d(0 , i_factor , j_factor*golden_ratio);
                points[loc2] = new Point3d(i_factor , j_factor*golden_ratio , 0);
                points[loc3] = new Point3d(j_factor*golden_ratio , 0 , i_factor);

                float off_color = 1 - vertex_number / 4f;

                colors[loc1] = new Color3f(1,off_color,off_color);
                colors[loc2] = new Color3f(off_color,1,off_color);
                colors[loc3] = new Color3f(off_color,off_color,1);

                //System.out.printf("(%d) %4d\t\t\t\t%4d\t\t\t\t%4d\n", vertex_number, loc1, loc2, loc3);
                //System.out.printf("       %s\t\t%s\t\t%s\n", colors[loc1], colors[loc2], colors[loc3]);
//                //5 copies of each, for proper lighting
//                for(int c = 0; c < 5; c++) {
//                    int loc1 = (i * 2 + j + 4*0)*5 + c;
//                    int loc2 = (i * 2 + j + 4*1)*5 + c;
//                    int loc3 = (i * 2 + j + 4*2)*5 + c;
//                    points[loc1] = new Point3d(0 , i_factor , j_factor*golden_ratio);
//                    points[loc2] = new Point3d(i_factor , j_factor*golden_ratio , 0);
//                    points[loc3] = new Point3d(j_factor*golden_ratio , 0 , i_factor);
//                    //System.out.printf("%4d\t%4d\t%4d\n", loc1, loc2, loc3);
//                }
            }
        }

        //specify indices
////        //first points
        indices[0] = 0;
        indices[1] = 1;
        indices[2] = 2;
        indices[3] = 1;
        indices[4] = 2;
        indices[5] = 3;
        indices[6+0] = 4;
        indices[6+1] = 5;
        indices[6+2] = 6;
        indices[6+3] = 5;
        indices[6+4] = 6;
        indices[6+5] = 7;
        indices[6+6+0] = 8;
        indices[6+6+1] = 9;
        indices[6+6+2] = 10;
        indices[6+6+3] = 9;
        indices[6+6+4] = 10;
        indices[6+6+5] = 11;
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
        geometry.setColorIndices(0, indices);

        // Utility code to automatically generate normals.
        GeometryInfo gInfo = new GeometryInfo(geometry);
        //new NormalGenerator().generateNormals(gInfo);
        return gInfo.getIndexedGeometryArray();
    }
}
