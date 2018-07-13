package de.markostreich.ms.raytracer08.raster;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;
import de.markostreich.ms.raytracer08.tracer.Raytracer;

import java.util.ArrayList;
import java.util.List;

/**
 * Supersampled.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 *
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public class Supersampled implements Raster {

	/** Vier. */
	private static final int AMOUNT_SAMPLED_ELEMENTS = 4;
	/** Drei. */
	private static final int THREE = 3;
	/** Haelfte. */
	private static final double A_HALF = 0.5;
	/** Ein Viertel. */
	private static final double A_QUARTER = 0.25;
	/** Verkettetet Raster. */
	private final Raster raster;
	/** Eigenes Raster. */
	private final int[][] newRaster;
	/** Anzahl verwendeter Threads. */
	private final int amountThreads = Runtime.getRuntime()
			.availableProcessors();;
	/** Array von Berabeitungsstaenden. */
	private volatile boolean[] rowsDone;

	/**
	 * Konstruktor. Verkettetet Raster auf die halbe Groesse reduziert.
	 * 
	 * @param raster
	 *            Raster
	 */
	public Supersampled(final Raster raster) {
		assert raster != null : "null raster is impossible";
		this.raster = raster;
		this.newRaster = new int[raster.getHeight() == 1 ? 1 : raster
				.getHeight() / 2][raster.getWidth() == 1 ? 1 : raster
				.getWidth() / 2];
		this.rowsDone = new boolean[newRaster.length];
	}

	/**
	 * Getter fuer Rasterzahl in die Breite.
	 * 
	 * @return Rasterbreite int
	 */
	@Override
	public int getWidth() {

		return this.newRaster[0].length;

	}

	/**
	 * Getter fuer Rasterzahl in die Hoehe.
	 * 
	 * @return Rasterhoehe int
	 */
	@Override
	public int getHeight() {
		return this.newRaster.length;
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
		return this.newRaster[row][column];
	}

	/**
	 * Szene rendern. Urspruengliches Raster auf 1/4 der Groesse reduziert.
	 * Durchschnitt von 4 Elementen bildet ein neues Element.
	 * 
	 * @param raytracer
	 *            Raytracer
	 * @return this hat mit Pixelwerten gefuellten Array
	 */
	@Override
	public Raster render(final Raytracer raytracer) {
		assert raytracer != null : "null raytracer is impossible";
		final Raster tmpRaster = raster.render(raytracer);
		// Laenge oder Breite koennte ungerade sein.
		final int newWidth = raster.getWidth() % 2 == 0 ? raster.getWidth()
				: raster.getWidth() - 1;
		final int newHeight = raster.getHeight() % 2 == 0 ? raster.getHeight()
				: raster.getHeight() - 1;
		final List<Thread> threadList = new ArrayList<>();
		for (int increment = 0; increment < amountThreads; increment++)
			threadList.add(new Thread() {
				public void run() {
					for (int xCoordinate = 0; xCoordinate < newWidth; xCoordinate += 2)
						for (int yCoordinate = 0; yCoordinate < newHeight; yCoordinate += 2) {
							final int sum = tmpRaster.getPixel(yCoordinate,
									xCoordinate)
									+ tmpRaster.getPixel(1 + yCoordinate,
											xCoordinate)
									+ tmpRaster.getPixel(yCoordinate,
											1 + xCoordinate)
									+ tmpRaster.getPixel(1 + yCoordinate,
											1 + xCoordinate);
							final double average = sum
									/ AMOUNT_SAMPLED_ELEMENTS;
							final int round = sum % AMOUNT_SAMPLED_ELEMENTS;
							final double averageRounded;
							switch (round) {
							case 1:
								averageRounded = average - A_QUARTER;
								break;
							case 2:
								averageRounded = average + A_HALF;
								break;
							case THREE:
								averageRounded = average + A_QUARTER;
								break;
							default:
								averageRounded = average;
								break;
							}
							newRaster[yCoordinate / 2][xCoordinate / 2] = (int) averageRounded;
						}
				}
			});
		for (final Thread thread : threadList) {
			thread.start();

		}
		for (final Thread thread : threadList)
			try {
				thread.join();
			} catch (InterruptedException exception) {
				exception.printStackTrace();
			}

		return this;
	}

	/**
	 * Bearbeitungsstadium einer Reihe.
	 * 
	 * @param lineNumber
	 *            int
	 * @return Bearbeitungsstadium
	 */
	synchronized boolean checkRowStatus(final int lineNumber) {
		if (!rowsDone[lineNumber]) {
			rowsDone[lineNumber] = true;
			rowsDone = rowsDone;
			return false;
		}
		return true;
	}
}