/* (C) 2016, M. Streich, mstreich@hm.edu
 * Oracle Corporation Java 1.8.0_91, Ubuntu 15.10 64 Bit
 * Intel Celeron 2957U 1.4 GHz x 2, 3.8 GiB RAM
 **/
package de.markostreich.ms.raytracer08.scene.primitive;

import de.markostreich.ms.raytracer08.geometry.Point;
import de.markostreich.ms.raytracer08.geometry.Ray;
import de.markostreich.ms.raytracer08.geometry.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author M. Streich, mstreich@hm.edu
 * @version 02.08.16.
 */
public class BoxNotOrthogonal extends PrimitiveObject {

    /**
     * Liste der Begrenzungsflaechen.
     */
    private final ArrayList<Plane> planes = new ArrayList<>();

    /** Hoehe. */
    private final double height;

    /** Breite. */
    private final double width;

    /** Tiefe. */
    private final double depth;

    public BoxNotOrthogonal(final Point centerPoint, final Vector directionA, final Vector directionB, final double height, final double width, final double depth) {
        this.height = height;
        this.width = width;
        this.depth = depth;

        Vector directA = directionA.normalize();
        planes.add(new Plane(centerPoint.makeVector().add(directA.scale(height/2)).makePoint(), directA));
        planes.add(new Plane(centerPoint.makeVector().add(directA.scale(-height/2)).makePoint(), directA));

        Vector directB = directionB.normalize();
        planes.add(new Plane(centerPoint.makeVector().add(directB.scale(depth/2)).makePoint(), directB));
        planes.add(new Plane(centerPoint.makeVector().add(directB.scale(-depth/2)).makePoint(), directB));

        Vector directC = directionA.crossProduct(directionB).normalize();
        planes.add(new Plane(centerPoint.makeVector().add(directC.scale(width/2)).makePoint(),directC));
        planes.add(new Plane(centerPoint.makeVector().add(directC.scale(-width/2)).makePoint(),directC));
    }

    /**
     * Interface Schnittpunktelieferant.
     *
     * @param ray Ray
     * @return Liste von Schnittpunkten
     */
    @Override
    public List<Intersection> intersections(final Ray ray) {
        final List<Intersection> result = new ArrayList<>();
        for (final Plane plane : planes){
            result.addAll(plane.intersections(ray));
            List<Intersection> planeIntersection = plane.intersections(ray);
            if (!planeIntersection.isEmpty())
                planeIntersection.forEach(result::add);
            if (result.size() == 2)
                return result;
        }
        return result;
    }

    /**
     * Normalenvector an einem Oberflaechenpunkt.
     *
     * @param point Point
     * @return Normalenvector Vector
     */
    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
