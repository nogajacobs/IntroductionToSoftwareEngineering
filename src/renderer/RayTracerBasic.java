package renderer;

import Scene.Scene;
import geometries.*;
import lighting.LightSource;
import primitives.*;

import static geometries.Intersectable.GeoPoint;

import java.util.List;

import static primitives.Util.alignZero;

/**
 *  author noga and noa
 */
public class RayTracerBasic extends RayTracerBase {

    /**
     * MAX CALC COLOR LEVEL
     */
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    /**
     *  MIN CALC COLOR K
     */
    private static final double MIN_CALC_COLOR_K = 0.001;
    /**
     *  Stop conditions of k
     */
    private static final Double3 INITIAL_K = Double3.ONE;
    /**
     *  ...
     */
    private static final double DELTA = 0.1;

    /**
     * true if use soft shadows
     */
    private boolean useSoftShadows =false;

    /**
     * setrr of soft shadows
     * @param useSoftShadows - boolean
     * @return RayTracerBasic - RayTracerBasic
     */
    public RayTracerBasic setUseSoftShadows(boolean useSoftShadows) {
        this.useSoftShadows = useSoftShadows;
        return  this;
    }
    // ***************** Constructors ********************** //

    /**
     * Constructor
     *
     * @param scene - Scene
     */
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    /**
     * default Constructor
     */
    public RayTracerBasic() {
        super(null);
    }

    // ***************** func's calcColor ********************** //

    /**
     * Calculate the Color
     * @param gp-geopoint cut
     * @param ray-ray cut
     * @return color- The final color before a point
     */
    private Color calcColor(GeoPoint gp, Ray ray) {
        Color color=calcColor(gp, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K);
        return color.add(scene.getAmbientLight().getIntensity());
    }

    /**
     * A recursive function that returns the color at a specific point
     * @param gp- poibt cut
     * @param ray- ray cut
     * @param level-stop ryorya
     * @param k- foctor of color
     * @return Color-  the final color before a point
     */
    private Color calcColor(GeoPoint gp, Ray ray, int level, Double3 k) {
        Color color = calcLocalEffects(gp, ray,k);
        if(level == 1){
            return color;
        }
        color = color.add(calcGlobalEffects(gp, ray, level, k));
        return color;
    }

    /**
     *  She is responsible for the global effects
     *  reflected and refracted rays )
     * @param ray- ray cut
     * @param level-stop ryorya
     * @param k- foctor of color
     * @return Color-     The final color before a point
     */
    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Color color = Color.BLACK;

        Vector n = gp.geometry.getNormal(gp.point);

        Double3 kr = gp.geometry.getMaterial().kR;
        Double3 kt = gp.geometry.getMaterial().kT;

        Double3 kkr = k.product(kr);
        Double3 kkt = k.product(kt);

        if (!kkr.lowerThan(MIN_CALC_COLOR_K)) {
            Ray reflectedRay = constructReflectedRay(gp.point, ray, n);
            GeoPoint reflectedPoint = findClosestIntersection(reflectedRay);
            if (reflectedPoint!=null){
                color = color.add(calcColor(reflectedPoint, reflectedRay, level - 1, kkr).scale(kr));
            }}
        if (!kkt.lowerThan(MIN_CALC_COLOR_K)) {
            Ray refractedRay = constructRefractedRay(gp.point, ray, n);
            GeoPoint refractedPoint = findClosestIntersection(refractedRay);
            if (refractedPoint!=null) {
                color = color.add(calcColor(refractedPoint, refractedRay, level - 1, kkt).scale(kt));
            }
        }
        return color;
    }

    /**Construct
     *  The thought of transparency

     * @param point- point on the geometry
     * @param ray -ray cut
     * @param n-normal vector
     * @return refracted ray
     */
    private Ray constructRefractedRay(Point point, Ray ray, Vector n) {
        return  new Ray(point,ray.getDir(),n.normalize());
    }

    /**
     *  Thought reflection     *
     *  @param point- point on the geometry
     *      * @param ray -ray cut
     *      * @param n-normal vector
     * @return ray   reflection ray
     */
    private Ray constructReflectedRay(Point point, Ray ray, Vector n) {
        Vector v = ray.getDir();
        double vn = alignZero(v.dotProduct(n));
        Vector r = v.subtract(n.scale(2*vn));
        return new Ray(point,r,n);
    }

    /**
     * Calculate effect of lights
     *
     * @param geoPoint -piont cut
     * @param ray-ray cut
     * @return Color-Color according to calculations of light effects
     */
    private Color calcLocalEffects(GeoPoint geoPoint, Ray ray,Double3 k) {
        Color color = geoPoint.geometry.getEmission();

        Vector v = ray.getDir();
        Vector n = geoPoint.geometry.getNormal(geoPoint.point);
        double nv = alignZero(n.dotProduct(v));
        Material material = geoPoint.geometry.getMaterial();
        if (nv == 0)
            return color.BLACK;
        Double3 ktr=Double3.ZERO;
        double nl=0.0;
        for (LightSource lightSource : scene.getLights()) {
            List<Vector> listVector = lightSource.listGetL(geoPoint.point,n);//for the soft shadows
            Vector l = lightSource.getL(geoPoint.point);
            int count = 0;
            if (useSoftShadows) {
                for (Vector vector : listVector)
                {
                    nl = alignZero(n.dotProduct(vector));
                    if (nl * nv > 0) {
                        count ++;
                        ktr = ktr.add(transparency(geoPoint, lightSource, vector, n, nv));
                    }
                }
                if (nl * nv > 0) {
                    if (!k.product(ktr).lowerThan(MIN_CALC_COLOR_K)) {
                        Color iL = lightSource.getIntensity(geoPoint.point).scale(ktr.reduce(count));
                        color = color.add(iL.scale(calcDiffusive(material, nl)),
                                iL.scale(calcSpecular(material, n, l, nl, v)));
                    }
                }

            }
            else {
                Vector vector = lightSource.getL(geoPoint.point);
                nl = alignZero(n.dotProduct(vector));
                if (nl * nv > 0) {
                    ktr = transparency(geoPoint, lightSource, vector, n, nv);
                    if (!k.product(ktr).lowerThan(MIN_CALC_COLOR_K)) {
                        Color iL = lightSource.getIntensity(geoPoint.point).scale(ktr);
                        color = color.add(iL.scale(calcDiffusive(material, nl)),
                                iL.scale(calcSpecular(material, n, vector, nl, v)));
                    }
                }
            }
        }
        return color;
    }

    /**
     * Calculate Flashes, a kind of reflection of light, on the surface
     * @param material thr data
     * @param n-normal of cur geometry
     * @param l-light
     * @param nl- nukt n,l
     * @param v-dinction of ray
     * @return Double3- reflection
     */
    private Double3 calcSpecular(Material material, Vector n, Vector l, double nl, Vector v) {
        Vector r = l.add(n.scale(-2 * nl));
        double minusVR = -alignZero(r.dotProduct(v));
        if (minusVR <= 0)
            return Double3.ZERO;
        Double3 result = material.getkS().scale(Math.pow(minusVR, material.nShininess));
        return result;
    }

    /**
     * Calculate Diffusive, The surfaces of the bodies are not smooth, so the reflected light is scattered
     * In all directions, affects light and shadow on the body, creates a deep look.
     *
     * @param material= data
     * @param nl-scale of n l
     * @return Double3-Coefficient of the distributing color
     */
    private Double3 calcDiffusive (Material material, double nl) {
        nl = Math.abs(nl);
        Double3 result = material.getkD().scale(nl);
        return result;

    }

    /**
     Is there a shadow and how many
     * @param gp-point og gomtry
     * @param lightSource-light
     * @param l- direction of ray from lught
     * @param n- normal vectoor
     * @param nv- mult n v
     * @return Double3- transparency factor
     */
    private Double3 transparency(GeoPoint gp, LightSource lightSource, Vector l, Vector n, double nv) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(gp.point, lightDirection, n);
        double maxdistance = lightSource.getDistance(gp.point);
        List<GeoPoint> intersections = scene.getGeometries().findGeoIntersections(lightRay);

        if (intersections==null){
            return Double3.ONE;
        }
        Double3 ktr=Double3.ONE;
        for (var item: intersections ) {
            var kT=item.geometry.getMaterial().kT;
            ktr=kT.product(ktr);
        }
        return ktr;
    }

    /**
     *  Finds the point of intersection closest to you
     * @param ray- ray cut
     * @return point closest
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> intersections = scene.getGeometries().findGeoIntersections(ray);
        if(intersections==null)  {
            return null;
        }
        GeoPoint closestPoint = ray.findGeoClosestPoint(intersections);
        return closestPoint;
    }


    /**
     * trace ray, and find cross point and return calcColor of the point if dont have point return Backgroung
     *
     * @param ray-ray cut
     * @return Color
     */
    @Override
    public Color traceRay(Ray ray) {
        var intersections = scene.getGeometries().findGeoIntersections(ray);
        if (intersections == null)
            return scene.getBackground();
        GeoPoint closestPoint = ray.findGeoClosestPoint(intersections);
        return calcColor(closestPoint, ray);

    }
}

