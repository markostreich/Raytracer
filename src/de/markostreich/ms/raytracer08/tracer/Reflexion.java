package de.markostreich.ms.raytracer08.tracer;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;
import de.markostreich.ms.raytracer08.geometry.Point;
import de.markostreich.ms.raytracer08.geometry.Ray;
import de.markostreich.ms.raytracer08.geometry.Vector;
import de.markostreich.ms.raytracer08.scene.Scene;
import de.markostreich.ms.raytracer08.scene.primitive.Intersection;
import de.markostreich.ms.raytracer08.scene.primitive.Surface.Property;

import java.util.Optional;

/**
 * Lichtmodell Reflexion.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-13
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public class Reflexion implements LightModel {

	/** Minimalgewicht eines Rays. */
	private static final double RAY_MIN_WEIGHT = 0.00390625;
	/** Minimale Verschiebung. */
	private static final double SHIFT = 10E-8;
	/** Aktueller Strahl. */
	private final Ray ray;
	/** Aktueller Raytracer. */
	private final Raytracer raytracer;

	/**
	 * Konstruktor Reflexion.
	 * 
	 * @param ray
	 *            Ray
	 * @param raytracer
	 *            Raytracer
	 */
	public Reflexion(final Ray ray, final Raytracer raytracer) {
		assert ray != null : "null ray is impossible";
		assert raytracer != null : "null raytracer is impossible";
		this.ray = ray;
		this.raytracer = raytracer;
	}

	/**
	 * Berechnung des Beitrages der Reflexion zu einem Pixel.
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
		if (!intersection.isPresent())
			return 0.0;

		final double weightNextRay = intersection.get().getPrimitive()
				.getSurface().get(Property.ReflexionRatio)
				* ray.getMaxWeight();

		if (weightNextRay <= RAY_MIN_WEIGHT)
			return 0.0;

		final Vector normalVector = intersection.get().getPrimitive()
				.getNormal(intersection.get().getIntersectionPoint());
		final Point newIntersectionPoint = intersection.get()
				.getIntersectionPoint().makeVector()
				.add(normalVector.scale(SHIFT)).makePoint();

		final Ray rayMirrored = new Ray(newIntersectionPoint, ray.getVector()
				.mirror(normalVector), weightNextRay);

		final double result = intersection.get().getPrimitive().getSurface()
				.get(Property.ReflexionRatio)
				* raytracer.trace(rayMirrored);
		return result;
	}
}
