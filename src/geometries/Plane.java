package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Plane class a point in the vector anth space
 * @author noga and noa
 */

public class Plane extends Geometry{
    final Point p0;
    final Vector normal;

    // ***************** constructor ********************** //

    /**
     * constructor
     * @param q0
     * @param n vector for the normal (will be normalized automatically)
     */
    public Plane(Point q0, Vector n) {
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


         normal =  N.normalize();

    }

    // ***************** getter ********************** //

    /**
     * func getter
     * @return point
     */
    public Point getQ0()
    {
        return p0;
    }

    /**
     * getter for _normal field
     * @return
     */
    public Vector getNormal() {
        return normal;
    }

    // ***************** Override ********************** //

    /**
     * implementation of getNormal from Geometry
     * @param point
     * @return
     */
    @Override
    public Vector getNormal(Point point) {
        return getNormal();
    }

    /**
     * toString with parameter of plane
     * @return java.lang.String
     */
    @java.lang.Override
    public java.lang.String toString() {
        return "Plane{" +
                "p0=" + p0 +
                ", normal=" + normal +
                '}';
    }

    /**
     * Function for finding intersection points
     * @param ray
     * @return
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray,double maxDistance) {
        Vector n=getNormal();
        double nv=n.dotProduct(ray.getDirection());
        if(isZero(nv))
        {
            return  null;
        }
        Vector p0Q;
        try {
            p0Q = p0.subtract(ray.getP0());
        }
        catch (Exception e)
        {
            return  null;
        }
        double t=alignZero(n.dotProduct(p0Q)/nv);
        if(t<=0 || alignZero(t - maxDistance) >= 0)
            return null;
        return List.of(new GeoPoint(this,ray.getPoint(t)));
    }
}