package geometry;

import primitives.Point;
import primitives.Vector;
/**
 * Plane class a point in the vector anth space
 * @author noga and noa
 */

public class Plane implements Geometry{
    final Point p0;
    final Vector normal;

    /**
     * constructor
     * @param q0
     * @param n vector for the normal (will be normalized automatically)
     */public Plane(Point q0, Vector n) {
        p0 = q0;
        normal = n;
    }

    /**
     * from three point found the vector normal (now put null in the next level we do it)
     * @param p1
     * @param p2
     * @param p3
     */
    public Plane(Point p1, Point p2, Point p3) {
        p0 =p1;
//        //TODO check direction of vectors
//        Vector U = p1.subtract(p2);
//        Vector V = p3.subtract(p2);

        Vector U = p2.subtract(p1);
        Vector V = p3.subtract(p1);
        Vector N = U.crossProduct(V);

        //normal = null;
        N.normalize();

        //right-hand rule;
         normal = N;
    }

    public Point getQ0() {
        return p0;
    }

    /**
     * getter for _normal field
     * @return
     */
    public Vector getNormal() {
        return normal;
    }

    /**
     * implementation of getNormal from Geometry
     * @param point
     * @return
     */
    @Override
    public Vector getNormal(Point point) {
        return getNormal();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Plane{" +
                "p0=" + p0 +
                ", normal=" + normal +
                '}';
    }
}