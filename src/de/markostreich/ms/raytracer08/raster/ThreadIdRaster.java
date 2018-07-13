package de.markostreich.ms.raytracer08.raster;

/**
 * ThreadIdRaster.
 * 
 * @author M. Streich, mstreich@hm.de
 * @version 2015-05-21
 */
public class ThreadIdRaster extends ParallelRaster {

	/**
	 * Konstruktor ThreadIdRaster.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param amountThreads
	 *            int
	 * @throws IllegalArgumentException
	 *             width oder height < 1
	 */
	ThreadIdRaster(int width, int height, int amountThreads)
			throws IllegalArgumentException {
		super(width, height, amountThreads);
	}

	@Override
	public void setPixel(final int row, final int column, final int value) {
		if (row < 0 || column < 0 || row > this.getHeight()
				|| column > this.getWidth())
			throw new ArrayIndexOutOfBoundsException(
					"Indexnummen nicht im gueltigen Bereich");
		// System.out.println(Integer.parseInt(Thread.currentThread().getName())
		// + " " + this.getAmountThreads());
		super.setPixel(
				row,
				column,
				(int) ((Double.parseDouble(Thread.currentThread().getName()) / (this
						.getAmountThreads() - 1)) * MAX_BRIGHTNESS));
	}
}