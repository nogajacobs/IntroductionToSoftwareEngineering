package primitives;

import geometries.Intersectable;

import java.util.List;
import java.util.Objects;

import static primitives.Util.*;

import static geometries.Intersectable.GeoPoint;

public class Ray {
    private static final double DELTA = 0.1 ;
    final Point p0;
    final Vector dir;

    // ***************** constructor ********************** //

    /**
     * constructor
     *
     * @param p0
     * @param dir
     */
    public Ray(Point p0, Vector dir) {
        this.p0 = p0;
        this.dir = dir.normalize();
    }
    /**
     * constructor
     *
     * @param p0
     * @param v
     */
    public Ray(Point p0, Vector v, Vector n) {
        this.dir = v.normalize();
        double vn = alignZero(v.dotProduct(n));
        Vector epsilon;
        if( vn < 0){
            epsilon = n.scale(-DELTA);
        }
        else{
            epsilon = n.scale(DELTA);
        }

        this.p0 = p0.add(epsilon);
    }
    // ***************** getter ********************** //

    /**
     * start of the ray
     *
     * @return Point
     */
    public Point getP0() {
        return p0;
    }

    /**
     * the direction of the ray
     *
     * @return Vector
     */
    public Vector getDir() {
        return dir;
    }

    // ***************** Override ********************** //

    /**
     * toString
     *
     * @return
     */
    @Override
    public String toString() {
        return "Ray{" +
                "p0=" + p0 +
                ", dir=" + dir +
                '}';
    }

    /**
     * equals
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return p0.equals(ray.p0) && dir.equals(ray.dir);
    }

    /**
     * hash Code
     *
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(p0, dir);
    }

    // ***************** getter ********************** //

    /**
     * func of get
     *
     * @return vector
     */
    public Vector getDirection() {
        return new Vector(dir.xyz);
    }

    /**
     * Multiply by double the point
     *
     * @param t
     * @return point
     */
    public Point getPoint(double t) {
        if (isZero(t)) {
            return p0;
        }
        return p0.add(dir.scale(t));
    }

    // ***************** func ********************** //

    /**
     * @param points
     * @return the point closest to the beginning of the foundation.
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findGeoClosestPoint(points
                .stream()
                .map(p -> new GeoPoint(null, p))
                .toList()).point;
    }


    /**
     * find Geo point Closest Point,  geo point it is the point from list of cross geometries
     *
     * @param intersections
     * @return GeoPoint
     */
    public GeoPoint findGeoClosestPoint(List<GeoPoint> intersections) {
        GeoPoint closestPoint = null;
        if (intersections == null)
            return null;
        double distance = Double.POSITIVE_INFINITY;
        double d;
        if (!intersections.isEmpty()) {
            for (var pt : intersections) {
                d = p0.distance(pt.point);

                if (d < distance) {
                    distance = d;
                    closestPoint = pt;
                }
            }
        }
        return closestPoint;
    }
}
