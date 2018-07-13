package de.markostreich.ms.raytracer08.tracer;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;
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
public class Ambient implements LightModel {

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
	public double calculate(final Scene scene, final Optional<Intersection> intersection) {
		assert scene != null : "null scene is impossilbe";
		if (intersection.isPresent())
			return intersection.get().getPrimitive().getSurface()
					.get(Surface.Property.AmbientRatio);
		else
			return 0.0;
	}

}
