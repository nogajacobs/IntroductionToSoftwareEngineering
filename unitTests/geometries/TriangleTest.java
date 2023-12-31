package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {
    /**
     * *Function of normal test of Triangle
     */
    @Test
    void getNormal() {

        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Point p1=new Point(1,1,1);
        Point p2=new Point(1,2,3);
        Point p3=new Point(2,0,0);
        Triangle tr = new Triangle(p1,p2,p3);
        Vector temp = new Vector(1/Math.sqrt(6),2/Math.sqrt(6),-1/Math.sqrt(6) );
        assertEquals(temp,tr.getNormal(new Point(1, 1, 1)), "Triangle-getNormal=> Simple test failure");
         }

    @Test
    public void testfindIntersectionsRay() {
        Triangle tr = new Triangle(new Point(0, 0, 2), new Point(2, 0, 0), new Point(0, 2, 0));
        Plane pl = new Plane(new Point(0, 0, 2), new Point(2, 0, 0), new Point(0, 2, 0));
        Ray ray;
        // ============ Equivalence Partitions Tests ==============
        // TC01: Inside triangle
        ray = new Ray(new Point(1, 1, 1), new Vector(-1, -1, -1));
        assertEquals(List.of(new Point(2d / 3, 2d / 3, 2d / 3)), tr.findIntersections(ray),
                "Bad intersection");

        // TC02: Against edge
        ray = new Ray(new Point(0, 0, -0.59), new Vector(1.09, 1.45, 0));
        assertEquals(List.of(new Point(1.111456692913386, 1.478543307086614, -0.59)), pl.findIntersections(ray),
                "Wrong intersection with plane");
        assertNull(tr.findIntersections(ray), "Bad intersection");

        // TC03: Against vertex
        ray = new Ray(new Point(0, 0, 2.3), new Vector(-1, -1, 0));
        assertEquals(List.of(new Point(-0.1499999999999999, -0.1499999999999999, 2.3)), pl.findIntersections(ray),
                "Wrong intersection with plane");
        assertNull(tr.findIntersections(ray), "Bad intersection");

        // =============== Boundary Values Tests ==================
        // TC11: In vertex
        ray = new Ray(new Point(-2, 0, 0), new Vector(1, 1, 0));
        assertEquals(List.of(new Point(0, 2, 0)), pl.findIntersections(ray),
                "Wrong intersection with plane");
        assertNull(tr.findIntersections(ray), "Bad intersection");

        // TC12: On edge
        ray = new Ray(new Point(-1, -1, 0), new Vector(1, 1, 0));
        assertEquals(List.of(new Point(1, 1, 0)), pl.findIntersections(ray),
                "Wrong intersection with plane");
        assertNull(tr.findIntersections(ray), "Bad intersection");

        // TC13: On edge continuation
        ray = new Ray(new Point(-4, 0, 0), new Vector(1, 1, 0));
        assertEquals(List.of(new Point(-1, 3, 0)), pl.findIntersections(ray),
                "Wrong intersection with plane");
        assertNull(tr.findIntersections(ray), "Bad intersection");
    }
}

