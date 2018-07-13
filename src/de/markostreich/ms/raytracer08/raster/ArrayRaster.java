package de.markostreich.ms.raytracer08.raster;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;
import de.markostreich.ms.raytracer08.tracer.Raytracer;

/**
 * Rastererzeugung Array.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
class ArrayRaster implements Raster {
	/** MAX_BRIGHTNESS. Konstante, die die maximale Helligkeit festlegt. */
	static final int MAX_BRIGHTNESS = 255;
	/** Array von Bildpixeln. */
	private final int[][] intOutPut;

	/**
	 * Konstruktor ArrayRaster.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @throws IllegalArgumentException
	 *             width oder height < 1
	 */
	ArrayRaster(final int width, final int height)
			throws IllegalArgumentException {
		if (width < 1 || height < 1)
			throw new IllegalArgumentException();
		this.intOutPut = new int[height][width];
	}

	/**
	 * Szene rendern.
	 * 
	 * @param raytracer
	 *            Raytracer
	 * @return this hat mit Pixelwerten gefuellten Array
	 */
	public Raster render(final Raytracer raytracer) {
		assert raytracer != null : "null raytracer is impossible";
		// 0.5 ist magic number fuer Checkstyle
		final double aHalf = 0.5;
		final int lengthY = this.intOutPut.length;
		final int lengthX = this.intOutPut[0].length;

		// Trace rays through viewport
		for (int yCoordinate = 0; yCoordinate < lengthY; yCoordinate++) {

			final double viewportY = 2.0 * (yCoordinate + aHalf) / lengthY - 1;

			for (int xCoordinate = 0; xCoordinate < lengthX; xCoordinate++) {

				final double viewportX = 2.0 * (xCoordinate + aHalf) / lengthX
						- 1;
				final int trace = raytracer.tracePrimary(viewportX,
						viewportY)/* * MAX_BRIGHTNESS*/;
//				System.out.println(trace);
				intOutPut[yCoordinate][xCoordinate] = trace;
			}
		}
		return this;
	}

	/* Getter */

	/**
	 * Getter fuer Rasterzahl in die Breite.
	 * 
	 * @return Rasterbreite int
	 */
	public int getWidth() {
		return this.intOutPut[0].length;
	}

	/**
	 * Getter fuer Rasterzahl in die Hoehe.
	 * 
	 * @return Rasterhoehe int
	 */
	public int getHeight() {
		return this.intOutPut.length;
	}

	/**
	 * Pixel nach Arrayindices.
	 * 
	 * @param column
	 *            int
	 * @param column
	 *            int
	 * @exception ArrayIndexOutOfBoundsException
	 *                Indexnummen row und/oder column nicht im gueltigen Bereich
	 */
	@Override
	public int getPixel(final int row, final int column)
			throws IllegalArgumentException {
		if (row < 0 || column < 0 || row > this.getHeight()
				|| column > this.getWidth())
			throw new ArrayIndexOutOfBoundsException(
					"Indexnummen nicht im gueltigen Bereich");
		return this.intOutPut[row][column];
	}

	/**
	 * Setter eines Pxels.
	 * 
	 * @param row
	 *            int
	 * @param column
	 *            int
	 * @param value
	 *            int
	 */
	public void setPixel(final int row, final int column, final int value) {
		if (row < 0 || column < 0 || row > this.getHeight()
				|| column > this.getWidth())
			throw new ArrayIndexOutOfBoundsException(
					"Indexnummen nicht im gueltigen Bereich");
		intOutPut[row][column] = value;
	}
}
