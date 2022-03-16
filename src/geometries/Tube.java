package geometries;

import primitives.*;

import java.util.List;

import static primitives.Util.*;
/**
 * Triangle class Vertex polygon
 * system
 *
 * @author noga and noa
 */
public class Tube implements Geometry {
    final Ray axisRay;
    final double radius;

    public Tube(Ray axisRay, double radius) {
        this.axisRay = axisRay;
        this.radius = radius;
    }

    /**
     * return axisRay of Tube
     * @return Ray
     */
    public Ray getAxisRay() {
        return axisRay;
    }

    /**
     * return radius of Tube
     * @return double
     */
    public double getRadius() {
        return radius;
    }

    /**
     * return vector normal
     * @param p
     * @return vector normal
     */
    public Vector getNormal(Point p){
        Point P0 = axisRay.getP0();
        Vector v = axisRay.getDir();

        Vector P0_P = p.subtract(P0);

        double t = alignZero(v.dotProduct(P0_P));

        if (isZero(t)) {
            return P0_P.normalize();
        }

        Point P1 = P0.add(v.Scale(t));

        if (p.equals(P1)) {
            throw new IllegalArgumentException("point cannot be on the tube axis");
        }

        Vector n = p.subtract(P1).normalize();

        return n;
    }

    /**
     * with Ray (axisRay) and radius (double)
     * @return string
     */
    @Override
    public String toString() {
        return "Tube{" +
                "axisRay=" + axisRay +
                ", radius=" + radius +
                '}';
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
