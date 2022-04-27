package lighting;

import primitives.*;

public interface LightSource {

    /**
     * Get light intensity at a point IL
     * @param p
     * @return Color
     */
    Color getIntensity(Point p);

    /**
     *
     * @param p
     * @return Vector
     */
    public Vector getL(Point p);
}
