package de.markostreich.ms.raytracer08.tracer;

import de.markostreich.ms.raytracer08.geometry.Point;
import de.markostreich.ms.raytracer08.geometry.Ray;
import de.markostreich.ms.raytracer08.geometry.Vector;
import de.markostreich.ms.raytracer08.scene.Scene;
import de.markostreich.ms.raytracer08.scene.primitive.Intersection;
import de.markostreich.ms.raytracer08.scene.primitive.Surface.Property;

import java.util.Optional;

public class Transmission implements LightModel {
	/** Index of refraction air to glass. */
	private static final double INDEX_OF_REFRACTION_IN = 1.53;
	/** Index of refraction glass to air. */
	private static final double INDEX_OF_REFRACTION_OUT = 0.65395;
	/** Minimalgewicht eines Rays. */
	private static final double RAY_MIN_WEIGHT = 0.00390625;
	/** Minimale Verschiebung. */
	private static final double SHIFT = 10E-8;
	/** Aktueller Strahl. */
	private final Ray ray;
	/** Aktueller Raytracer. */
	private final Raytracer raytracer;

	/**
	 * Konstruktor Transmission.
	 * 
	 * @param ray
	 *            Ray
	 * @param raytracer
	 *            Raytracer
	 */
	public Transmission(final Ray ray, final Raytracer raytracer) {
		assert ray != null : "null ray is impossible";
		assert raytracer != null : "null raytracer is impossible";
		this.ray = ray;
		this.raytracer = raytracer;
	}

	/**
	 * Berechnung des Beitrages der Transmission durch ein Primitive zu einem
	 * Pixel.
	 * 
	 * @param scene
	 *            Scene
	 * @param intersection
	 *            Optional<Intersection>
	 * @return Beitrag des Lichtmodells zu einem Pixel
	 */
	@Override
	public double calculate(Scene scene, Optional<Intersection> intersection) {
		if (!intersection.isPresent())
			return 0.0;

		final double weightNextRay = intersection.get().getPrimitive()
				.getSurface().get(Property.TransmissionRatio)
				* ray.getMaxWeight();

		if (weightNextRay <= RAY_MIN_WEIGHT)
			return 0.0;

		Vector normalVector = intersection.get().getPrimitive()
				.getNormal(intersection.get().getIntersectionPoint());
		final double refractionIndex;
		Point newIntersectionPoint;
		if (!intersection.get().isRayEntrance()){
			normalVector = normalVector.scale(-1);
			refractionIndex = INDEX_OF_REFRACTION_OUT;
			//System.out.println("reached");
			newIntersectionPoint = intersection.get()
					.getIntersectionPoint().makeVector()
					.add(normalVector.scale(SHIFT)).makePoint();
//			return 1;
		} else {
			refractionIndex = INDEX_OF_REFRACTION_IN;
			newIntersectionPoint = intersection.get()
					.getIntersectionPoint().makeVector()
					.add(normalVector.scale(-SHIFT)).makePoint();
//			return 1;
		}
			
		

		final double cosTauI = normalVector.scalar(ray.getVector().scale(-1));

		final double c = 1 + refractionIndex * refractionIndex
				* (cosTauI * cosTauI - 1);

		if (c < 0) {
			newIntersectionPoint = intersection.get()
					.getIntersectionPoint().makeVector()
					.add(normalVector.scale(SHIFT)).makePoint();
			final Ray rayMirrored = new Ray(newIntersectionPoint, ray
					.getVector().mirror(normalVector), weightNextRay);

			final double result = intersection.get().getPrimitive()
					.getSurface().get(Property.TransmissionRatio)
					* raytracer.trace(rayMirrored);
//			System.out.println("reached");
			return result;
		}
		final double b = refractionIndex * cosTauI - Math.sqrt(c);

		final Vector vectorTransmissioned = ray.getVector()
				.scale(refractionIndex).add(normalVector.scale(b));

		final Ray rayTransmissed = new Ray(newIntersectionPoint,
				vectorTransmissioned, weightNextRay);
		final double result = intersection.get().getPrimitive().getSurface()
				.get(Property.TransmissionRatio)
				* raytracer.trace(rayTransmissed)*0.9;
		System.out.println("Transmission: "+result);
		return result;
	}
}
