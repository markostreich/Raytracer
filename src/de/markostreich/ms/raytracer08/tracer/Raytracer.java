package de.markostreich.ms.raytracer08.tracer;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;
import de.markostreich.ms.raytracer08.geometry.Ray;
import de.markostreich.ms.raytracer08.scene.Scene;
import de.markostreich.ms.raytracer08.scene.primitive.Intersection;

import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Raytracer.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-13
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public class Raytracer {

	/** Szene. */
	private final Scene scene;

	/**
	 * Konstruktor Raytracer.
	 * 
	 * @param scene
	 *            Scene
	 */
	public Raytracer(final Scene scene) {
		assert scene != null : "null scene is impossible";
		this.scene = scene;
	}

	/**
	 * Schnittpunkttest und Helligkeitsausgabe an Raster.
	 * 
	 * @param viewportX
	 *            double
	 * @param viewportY
	 *            double
	 * @return Helligkeit im Bereich von 0.0 bis 1.0 double
	 */
	public int tracePrimary(final double viewportX, final double viewportY) {
		final Ray primaryRay = scene.getLooker().getPrimaryRay(viewportX,
				viewportY);
		return trace(primaryRay);
	}

	/**
	 * Schnittpunkttest und Helligkeitsausgabe.
	 * 
	 * @param primaryRay
	 *            Ray
	 * @return Helligkeit im Bereich von 0.0 bis 1.0 double
	 */
	public int trace(final Ray primaryRay) {
		assert primaryRay != null : "nullprimaryRay is impossible";
		final Optional<Intersection> optionalIntersection = scene
				.findIntersection(primaryRay);
		int result;
		if (optionalIntersection.isPresent()) {
			final double brightnessDouble;
			if (optionalIntersection.get().isRayEntrance()) {
				final BooleanPromise isShadowed = new BooleanPromise(
						() -> new Shadowed().calculate(scene,
								optionalIntersection) > 0.0);
				brightnessDouble = Stream
						.of(new Ambient(),
								new SpecularHighlight(isShadowed, primaryRay),
								new Diffuse(isShadowed),
								new Reflexion(primaryRay, this),
								new Transmission(primaryRay, this))
						.map(lightModel -> lightModel.calculate(scene,
								optionalIntersection))
						.mapToDouble(Double::doubleValue).sum();

			} else {
				brightnessDouble = new Transmission(primaryRay, this).calculate(scene,
						optionalIntersection);
			}
			int brightness = 255;
			if (brightnessDouble < 1) {
				brightness = (int) (brightnessDouble * 255);
			}
			final int colour = (int) new Colour().calculate(scene, optionalIntersection);
			if (colour != 0.0) {
				byte[] colours = ByteBuffer.allocate(4).putInt(colour).array();
				result = (0xFF << 24) | ((int) (Byte.toUnsignedInt(colours[1])*brightnessDouble) << 16) | (((int)(Byte.toUnsignedInt(colours[2])*brightnessDouble)) << 8) | ((int)(Byte.toUnsignedInt(colours[3])*brightnessDouble));
			}
			else
				result = (0xFF << 24) | (brightness << 16) | (brightness << 8) | brightness;
			//result = result | optionalIntersection.get().getColour();
//			int result1= result | optionalIntersection.get().getColour();
			return result;
		}
		return 0xFF << 24;
	}

	@Override
	public String toString() {
		return "Raytracer [scene=" + scene + "]";
	}
}
