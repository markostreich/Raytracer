package de.markostreich.ms.raytracer08.image;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;
import de.markostreich.ms.raytracer08.raster.Raster;

/**
 * Ausgabe eines Rasters von Pixeln auf dem Viewport.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 * 
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public interface Image {

	/**
	 * Imagebauer.
	 * 
	 * @param args
	 *            String
	 * @return Objekt des Interfaces Image
	 * @throws ClassNotFoundException
	 *             nicht PGMOut oder PNGImage
	 * @throws NullPointerException
	 *             keine Argumente
	 * @throws IllegalArgumentException
	 *             nicht korrekte Anzahl Argumente
	 */
	static Image make(final String... args)
			throws ClassNotFoundException {
		if (args.length == 0)
			return new PGMOut();
		switch (args[0]) {
		case "PGMOut":
			if (args.length != 1)
				throw new IllegalArgumentException(
						"PGMOut braucht keine Parameter!");
			return new PGMOut();
		case "PNGImage":
			if (args.length != 2)
				throw new IllegalArgumentException(
						"PNGImage braucht genau einen Parameter!");
			return new PNGImage(args[1]);
		default:
			throw new ClassNotFoundException("Nicht PGMOut oder PNGImage!");
		}
	}
	
	/**
	 * Ausgabe eines Pixelrasters.
	 * 
	 * @param pixel
	 *            Raster
	 * @throws WrongValueException
	 */
	void save(final Raster pixel);
}