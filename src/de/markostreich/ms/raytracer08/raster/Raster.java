package de.markostreich.ms.raytracer08.raster;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;
import de.markostreich.ms.raytracer08.tracer.Raytracer;

import java.util.Arrays;

/**
 * Interface Raster.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-04-16
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public interface Raster {

	/**
	 * Rasterbauer.
	 * 
	 * @param args
	 *            String
	 * @return Objekt der Klasse ArrayRaster
	 * @throws ClassNotFoundException
	 *             nicht ArrayRaster
	 * @throws NullPointerException
	 *             keine Argumente
	 * @throws IllegalArgumentException
	 *             nicht 3 Argumente
	 */
	static Raster make(final String... args) throws ClassNotFoundException {
		final int defaultRasterSize = 128;
		if (args.length == 0)
			return new ArrayRaster(defaultRasterSize, defaultRasterSize);

		// if (!"ArrayRaster".equals(args[0]) && !"Supersampled".equals(args[0])
		// && !"ParallelRaster".equals(args[0])
		// && !"ThreadIdRaster".equals(args[0]))
		// throw new ClassNotFoundException(
		// "Nicht Supersampled oder ArrayRaster!");
		final int three = 3;
		switch (args[0]) {
		case "ArrayRaster":
			final int[] parsedArgs1 = new int[2];
			for (int incr = 0; incr <= 1; incr++)
				parsedArgs1[incr] = Integer.parseInt(args[incr + 1]);
			return new ArrayRaster(parsedArgs1[0], parsedArgs1[1]);
		case "Supersampled":
			return new Supersampled(Raster.make(Arrays.copyOfRange(args, 1,
					args.length)));
		case "ParallelRaster":
			final int[] parsedArgs2 = new int[three];
			for (int incr = 0; incr <= 2; incr++)
				parsedArgs2[incr] = Integer.parseInt(args[incr + 1]);
			return new ParallelRaster(parsedArgs2[0], parsedArgs2[1],
					parsedArgs2[2]);
		case "ThreadIdRaster":
			final int[] parsedArgs3 = new int[three];
			for (int incr = 0; incr <= 2; incr++)
				parsedArgs3[incr] = Integer.parseInt(args[incr + 1]);
			return new ThreadIdRaster(parsedArgs3[0], parsedArgs3[1],
					parsedArgs3[2]);
		default:
			return new ArrayRaster(defaultRasterSize, defaultRasterSize);
		}
	}

	/**
	 * Getter Breite.
	 * 
	 * @return Breite int
	 */
	int getWidth();

	/**
	 * Getter Hoehe.
	 * 
	 * @return Hoehe int
	 */
	int getHeight();

	/**
	 * Pixel nach Index.
	 * 
	 * @param row
	 *            int
	 * @param column
	 *            int
	 * @return Pixelwert int
	 */
	int getPixel(final int row, final int column);

	/**
	 * Raytracer auf ein Raster rendern.
	 * 
	 * @param raytracer
	 *            Raytracer
	 * @return Raster
	 */
	Raster render(final Raytracer raytracer);
}
