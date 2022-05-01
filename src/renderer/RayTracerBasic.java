package renderer;

import Scene.Scene;
import geometries.Intersectable;
import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;

import static primitives.Util.alignZero;

public class RayTracerBasic extends RayTracerBase {


    /**
     * Constructor
     * @param scene
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

    /**
     * @param geoPoint
     * @return color
     */
    private Color calcColor(GeoPoint geoPoint,Ray ray) {
        Color color = scene.getAmbientLight().getIntensity();
        color = color.add(calcLocalEffects(geoPoint,ray));
        return color;
    }

    private Color calcLocalEffects(GeoPoint geoPoint, Ray ray) {
        Color color = geoPoint.geometry.getEmission();
        Vector v = ray.getDir();
        Vector n = geoPoint.geometry.getNormal(geoPoint.point);
        double nv = alignZero(n.dotProduct(v));
        Material material = geoPoint.geometry.getMaterial();
        if(nv==0)
            return color;
        for (LightSource lightSource : scene.getLights()) {
            Vector l = lightSource.getL(geoPoint.point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) {
                    Color iL = lightSource.getIntensity(geoPoint.point);
                    color = color.add(iL.scale(calcDiffusive(material, nl)),
                            iL.scale(calcSpecular(material, n, l, nl, v)));
            }
        }
        return color;
    }

    /**
     *
     * @param material
     * @param n
     * @param l
     * @param nl
     * @param v
     * @return
     */
    private Double3 calcSpecular(Material material, Vector n, Vector l, double nl, Vector v) {
        Vector r = l.add(n.Scale(-2*nl));
        double minusVR = -alignZero(r.dotProduct(v));
        if (minusVR<=0)
            return Double3.ZERO;
        Double3 result =  material.getkS().scale(Math.pow(minusVR,material.nShininess));
        return result;
    }

    /**
     *
     * @param material
     * @param nl
     * @return
     */
    private Double3 calcDiffusive(Material material, double nl) {
        nl = Math.abs(nl);
        Double3 result = material.getkD().scale(nl);
        return result;
    }


    /**
     * trace ray, and find cross point and return calcColor of the point if dont have point return Backgroung
     * @param ray
     * @return Color
     */
    @Override
    public Color traceRay(Ray ray) {
        var intersections = scene.getGeometries().findGeoIntersections(ray);
        if (intersections == null)
            return scene.getBackground();
        GeoPoint closestPoint = ray.findGeoClosestPoint(intersections);
        return calcColor(closestPoint,ray);

    }
}