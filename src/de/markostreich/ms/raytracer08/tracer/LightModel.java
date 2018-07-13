package de.markostreich.ms.raytracer08.tracer;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;
import de.markostreich.ms.raytracer08.scene.Scene;
import de.markostreich.ms.raytracer08.scene.primitive.Intersection;

import java.util.Optional;

/**
 * Interface Lichtmodell.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public interface LightModel {
	/**
	 * Berechnung des Beitrages eines Lichtmodells zu einem Pixel.
	 * 
	 * @param scene
	 *            Scene
	 * @param intersection
	 *            Optional<Intersection>
	 * @return Beitrag des Lichtmodells zu einem Pixel
	 */
	double calculate(Scene scene, Optional<Intersection> intersection);
}
