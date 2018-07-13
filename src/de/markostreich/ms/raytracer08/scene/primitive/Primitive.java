package de.markostreich.ms.raytracer08.scene.primitive;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;
import de.markostreich.ms.raytracer08.geometry.Point;
import de.markostreich.ms.raytracer08.geometry.Ray;
import de.markostreich.ms.raytracer08.geometry.Vector;

import java.util.List;

/**
 * Interface Grundformen.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public interface Primitive {
	/**
	 * Interface Schnittpunktelieferant.
	 * 
	 * @param ray
	 *            Ray
	 * @return Liste von Schnittpunkten
	 */
	List<Intersection> intersections(final Ray ray);

	/**
	 * Normalenvector an einem Oberflaechenpunkt.
	 * 
	 * @param point
	 *            Point
	 * @return Normalenvector Vector
	 */
	Vector getNormal(final Point point);

	/**
	 * Getter Surface.
	 * 
	 * @return surface
	 */
	Surface getSurface();
}
