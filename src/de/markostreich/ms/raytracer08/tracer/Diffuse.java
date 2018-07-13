/**
 * 
 */
package de.markostreich.ms.raytracer08.tracer;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;
import de.markostreich.ms.raytracer08.geometry.Point;
import de.markostreich.ms.raytracer08.geometry.Vector;
import de.markostreich.ms.raytracer08.scene.Scene;
import de.markostreich.ms.raytracer08.scene.primitive.Intersection;
import de.markostreich.ms.raytracer08.scene.primitive.Surface;

import java.util.Optional;

/**
 * Lichtmodell Ambient.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public class Diffuse implements LightModel {

	/** Im Schatten. */
	private final BooleanPromise isShadowed;

	/**
	 * Konstruktor Diffuse.
	 * 
	 * @param isShadowed
	 *            boolean
	 */
	Diffuse(final BooleanPromise isShadowed) {
		assert isShadowed != null : "null isShadowed is impossible";
		this.isShadowed = isShadowed;
	}

	/**
	 * Berechnung des Beitrages der diffusen Helligkeit zu einem Pixel.
	 * 
	 * @param scene
	 *            Scene
	 * @param intersection
	 *            Optional<Intersection>
	 * @return Beitrag des Lichtmodells zu einem Pixel
	 */
	@Override
	public double calculate(final Scene scene, final Optional<Intersection> intersection) {
		assert scene != null : "null scene is impossible";
		final Optional<Point> light = scene.getLight();
		final boolean lightIsPresent = light.isPresent();
		if (!lightIsPresent || !intersection.isPresent() || isShadowed.get()) {
			return 0.0;
		}
		final Vector lightVector = light.get().makeVector()
				.sub(intersection.get().getIntersectionPoint().makeVector());
		final Vector normalVector = intersection.get().getPrimitive()
				.getNormal(intersection.get().getIntersectionPoint());
		double diffuse = intersection.get().getPrimitive().getSurface()
				.get(Surface.Property.DiffuseRatio)
				* normalVector.cosAngle(lightVector);
		if (diffuse < 0.0)
			diffuse = 0.0;
		return diffuse;
	}

}
