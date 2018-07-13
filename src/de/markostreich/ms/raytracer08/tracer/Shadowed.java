package de.markostreich.ms.raytracer08.tracer;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;
import de.markostreich.ms.raytracer08.geometry.Point;
import de.markostreich.ms.raytracer08.geometry.Ray;
import de.markostreich.ms.raytracer08.geometry.Vector;
import de.markostreich.ms.raytracer08.scene.Scene;
import de.markostreich.ms.raytracer08.scene.primitive.Intersection;

import java.util.Optional;

/**
 * Lichtmodell Shadowed.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-13
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public class Shadowed implements LightModel {

	/** Minimale Verschiebung. */
	private static final double SHIFT = 10E-8;

	/**
	 * Suche nach einem Schatten.
	 * 
	 * @param scene
	 *            Scene
	 * @param intersection
	 *            Optional<Intersection>
	 * @return Beitrag des Lichtmodells zu einem Pixel
	 */
	@Override
	public double calculate(final Scene scene,
			final Optional<Intersection> intersection) {
		assert scene != null : "null scene is impossible";
		if (!intersection.isPresent() || !scene.getLight().isPresent())
			return 0.0;
		final Vector lightVector = scene.getLight().get().makeVector()
				.sub(intersection.get().getIntersectionPoint().makeVector());
		final Point newIntersectionPoint = intersection
				.get()
				.getIntersectionPoint()
				.makeVector()
				.add(intersection.get().getPrimitive()
						.getNormal(intersection.get().getIntersectionPoint())
						.scale(SHIFT)).makePoint();
		final Ray secondaryRay = new Ray(newIntersectionPoint, lightVector, 1);
		final Optional<Intersection> intersectionSecondaryRay = scene
				.findIntersection(secondaryRay);
		if (!intersectionSecondaryRay.isPresent())
			return 0.0;
		if (intersection
				.get()
				.getIntersectionPoint()
				.distance(intersectionSecondaryRay.get().getIntersectionPoint()) > intersection
				.get().getIntersectionPoint().distance(scene.getLight().get()))
			return 0.0;
		if (intersection.get().getPrimitive()
				.equals(intersectionSecondaryRay.get().getPrimitive()))
			return 0.0;
		return 1.0;
	}

}
