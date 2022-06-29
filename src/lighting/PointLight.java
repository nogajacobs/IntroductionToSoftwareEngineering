package lighting;

import geometries.Circle;
import geometries.Cylinder;
import geometries.Sphere;
import primitives.*;
import primitives.Color;
import primitives.Point;


import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class PointLight extends Light implements LightSource {
    private int size = 20;
    private int lenVector = 300;
    /**
     * the point that start the ray of this light
     */
    private Point position;
    /**
     * Fixed discount coefficient
     */
    private Double3 kC = Double3.ONE;
    /**
     * Discount coefficient
     */
    private Double3 kL = Double3.ZERO;
    /**
     * Discount coefficient
     */
    private Double3 kQ = Double3.ZERO;

    // ***************** constructor ********************** //

    /**
     * constructor
     *
     * @param intensity
     */
    public PointLight(Color intensity, Point _position) {
        super(intensity);
        this.position = _position;
    }

    /**
     * call to func distance in class point
     *
     * @param point
     * @return
     */
    public double getDistance(Point point) {
        double distance = point.distance(position);
        return distance;
    }

    // ***************** setter ********************** //

    /**
     * func setter (type builder)
     *
     * @param position
     * @return PointLight
     */
    public PointLight setPosition(Point position) {
        this.position = position;
        return this;
    }

    /**
     * func setter (type builder)
     *
     * @param kC Double3
     * @return PointLight
     */
    public PointLight setkC(Double3 kC) {
        this.kC = kC;
        return this;
    }

    /**
     * func setter (type builder)
     *
     * @param _kL Double3
     * @return PointLight
     */
    public PointLight setkL(Double3 _kL) {
        this.kL = _kL;
        return this;
    }

    /**
     * func setter (type builder)
     *
     * @param _kQ Double3
     * @return PointLight
     */
    public PointLight setkQ(Double3 _kQ) {
        this.kQ = _kQ;
        return this;
    }

    /**
     * func setter (type builder)
     *
     * @param kC Double3
     * @return PointLight
     */
    public PointLight setkC(double kC) {
        this.kC = new Double3(kC);
        return this;
    }

    /**
     * func setter (type builder)
     *
     * @param _kL double
     * @return PointLight
     */
    public PointLight setkL(double _kL) {
        this.kL = new Double3(_kL);
        return this;
    }

    /**
     * func setter (type builder)
     *
     * @param _kQ double
     * @return PointLight
     */
    public PointLight setkQ(double _kQ) {
        this.kQ = new Double3(_kQ);
        return this;
    }
    // ***************** Override ********************** //

    /**
     * Get light intensity at a point IL
     *
     * @param p
     * @return Color
     */
    @Override
    public Color getIntensity(Point p) {
        double distance = (p.distance(position));
        double distancesquared = (p.distanceSquared(position));
        Double3 factor = (kC.add(kL.scale(distance)).add(kQ.scale(distancesquared)));
        return getIntensity().reduce(factor);
    }

    /**
     * get for L, Returns the calculation of l
     *
     * @param p
     * @return Vector
     */
    @Override
    public List<Vector> listGetL(Point p,Vector n) {
        // ????? ?? ???????
        List<Vector> vectorList = new LinkedList<>();
        //?????? ?? ?????? ???????
        vectorList.add(p.subtract(position).normalize());
        //????? ?? ?????? ?????
        Sphere sphere = new Sphere(position, lenVector);
        Ray ray= new Ray(p, getL(p), n);
        p=ray.getP0();
        Random r = new Random();
        int in = size / 2;
        int out = size - size / 2;
        if (size > 1) {
            int t;
            for (int i = 0; i < in; i++) {
                t = r.nextInt(0, (int) sphere.getRadius() - 5);
                Point pt = new Point(position.getX() + t, position.getY() + t, position.getZ() + t);
                Vector v = pt.subtract(p).normalize();
                vectorList.add(v);
            }
            for (int i = 0; i < out; i++) {
                t = r.nextInt((int) -sphere.getRadius() + 5, 0);
                Point pt = new Point(position.getX() + t, position.getY() + t, position.getZ() + t);
                Vector v = pt.subtract(p).normalize();
                vectorList.add(v);
            }
        }
        return vectorList;
    }

    public Vector getL(Point p) {
        return p.subtract(position).normalize();
    }
}