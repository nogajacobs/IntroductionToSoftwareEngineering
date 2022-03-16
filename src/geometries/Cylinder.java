package geometries;

import primitives.*;
import static primitives.Util.*;
/**
 * Triangle class Vertex polygon
 * system
 *
 * @author noga and noa
 */
public class Cylinder extends Tube {

    final double height;

    /**
     * constctor using super main using Polygon

     */
    public Cylinder(Ray axisRay, double radius, double height) {
        super(axisRay, radius);
        this.height = height;
    }

    /**
     * height of Cylinder
     * @return double
     */
    public double getHeight() {
        return height;
    }

    /**
     * return vector normal
     * @param p
     * @return vector normal
     */
    @Override
    public Vector getNormal(Point p){
        Point o =  axisRay.getP0();
        Vector v = axisRay.getDirection();

        // projection of P-O on the ray:
        double t;
        try {
            t = alignZero(p.subtract(o).dotProduct(v));
        } catch (IllegalArgumentException e) { // P = O
            return v;
        }

        // if the point is at a base
        if (t == 0 || isZero(height - t)) // if it's close to 0, we'll get ZERO vector exception
            return v;

        o = o.add(v.Scale(t));
        return p.subtract(o).normalize();
    }

    @Override
    public String toString() {
        return "Cylinder{" +
                "height=" + height +
                '}';
    }
}