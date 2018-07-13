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
 * @version 03.08.16.
 */
public class Box extends PrimitiveObject {

    /** TOLERANCE. Die Rundungstoleranz. */
    private static final double TOLERANCE = 1e-12;
    private final Point l;

    private final Point h;

    private double t1;
    private double t2;
    private double tNear;
    private double tFar;


    public Box(final Point minimumExtend, final Point maximumExtend) {
        this.l = minimumExtend;
        this.h = maximumExtend;
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
        final Point rayPoint = ray.getPoint();
        final Vector rayVector = ray.getVector();
        /* Parallelitaet des Strahles zu den Ebenen pruefen. */
        if (rayVector.getX() == 0)
            if (rayPoint.getX() < l.getX() || rayPoint.getX() > l.getX())
                return result;
        if (rayVector.getY() == 0)
            if (rayPoint.getY() < l.getY() || rayPoint.getY() > l.getY())
                return result;
        if (rayVector.getZ() == 0)
            if (rayPoint.getZ() < l.getZ() || rayPoint.getZ() > l.getZ())
                return result;

        tNear = Double.NEGATIVE_INFINITY;
        tFar = Double.POSITIVE_INFINITY;

        t1 = (l.getX() - rayPoint.getX()) / rayVector.getX();
        t2 = (h.getX() - rayPoint.getX()) / rayVector.getX();
        swapAndSet();
        if (tNear > tFar) return result;
        if (tFar < 0) return result;
        t1 = (l.getY() - rayPoint.getY()) / rayVector.getY();
        t2 = (h.getY() - rayPoint.getY()) / rayVector.getY();
        swapAndSet();
        if (tNear > tFar) return result;
        if (tFar < 0) return result;
        t1 = (l.getZ() - rayPoint.getZ()) / rayVector.getZ();
        t2 = (h.getZ() - rayPoint.getZ()) / rayVector.getZ();
        swapAndSet();
        if (tNear > tFar) return result;
        if (tFar < 0) return result;
        result.add(new Intersection(ray.makePoint(tNear), this, tNear, true));
        result.add(new Intersection(ray.makePoint(tFar), this, tFar, false));
        return result;
    }

    private void swapAndSet() {
        if (t1 > t2) {
            double tTemp = t1;
            t1 = t2;
            t2 = tTemp;
        }
        if (t1 > tNear)
            tNear = t1;
        if (t2 < tFar)
            tFar = t2;
    }

    /**
     * Normalenvector an einem Oberflaechenpunkt.
     *
     * @param point Point
     * @return Normalenvector Vector
     */
    @Override
    public Vector getNormal(final Point point) {
        if (Math.abs(point.getX() - l.getX())<=TOLERANCE || Math.abs(point.getX() - h.getX())<=TOLERANCE  )
            return new Vector(1,0,0);
        if (Math.abs(point.getY() - l.getY())<=TOLERANCE || Math.abs(point.getY() - h.getY())<=TOLERANCE  )
            return new Vector(0,1,0);
        if (Math.abs(point.getZ() - l.getZ())<=TOLERANCE || Math.abs(point.getZ() - h.getZ())<=TOLERANCE  )
            return new Vector(0,0,1);
        return null;
    }
}
