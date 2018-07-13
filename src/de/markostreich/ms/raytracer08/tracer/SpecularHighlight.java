package de.markostreich.ms.raytracer08.tracer;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;
import de.markostreich.ms.raytracer08.geometry.Ray;
import de.markostreich.ms.raytracer08.geometry.Vector;
import de.markostreich.ms.raytracer08.scene.Scene;
import de.markostreich.ms.raytracer08.scene.primitive.Intersection;
import de.markostreich.ms.raytracer08.scene.primitive.Surface;

import java.util.Optional;

/**
 * Lichtmodell SpecularHighlight.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-13
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public class SpecularHighlight implements LightModel {

	/** Schattenwert. */
	private final BooleanPromise isShadowed;
	/** Aktueller Strahl. */
	private final Ray ray;

	/**
	 * Konstruktor SpecularHighlight.
	 * 
	 * @param isShadowed
	 *            BooleanPromise
	 * @param ray
	 *            Ray
	 */
	SpecularHighlight(final BooleanPromise isShadowed, final Ray ray) {
		assert isShadowed != null : "null isShadowed is impossible";
		assert ray != null : "null ray is impossible";
		this.isShadowed = isShadowed;
		this.ray = ray;
	}

	/**
	 * Berechnung des Beitrages des Speculars zu einem Pixel.
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
		if (!intersection.isPresent() || isShadowed.get()
				|| !scene.getLight().isPresent())
			return 0.0;
		final Vector normalVector = intersection.get().getPrimitive()
				.getNormal(intersection.get().getIntersectionPoint());
		final Vector vectorMirrored = ray.getVector().mirror(normalVector);
		final Vector lightVector = scene.getLight().get().makeVector()
				.sub(intersection.get().getIntersectionPoint().makeVector());
		final double alpha = vectorMirrored.normalize().scalar(
				lightVector.normalize());
		final double alphaPotenz;
		if (alpha > 0)
			alphaPotenz = Math.pow(alpha, intersection.get().getPrimitive()
					.getSurface().get(Surface.Property.SpecularExponent));
		else
			return 0.0;
		final double specular = intersection.get().getPrimitive().getSurface()
				.get(Surface.Property.SpecularRatio);

		return specular * alphaPotenz;
	}
}
