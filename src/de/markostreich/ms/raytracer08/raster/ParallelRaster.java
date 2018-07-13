package de.markostreich.ms.raytracer08.raster;

import de.markostreich.ms.raytracer08.tracer.Raytracer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ParallelRaster.
 * 
 * @author M. Streich, mstreich@hm.de
 * @version 2015-05-21
 */
public class ParallelRaster extends ArrayRaster {

	/** Anzahl verwendeter Threads. */
	private final int amountThreads;
	/** Array von Berabeitungsstaenden. */
	private final boolean[] rowsDone;
	/** Menge gerade bearbeiteter Reihen. */
	private volatile Set<Integer> linesInProcess = new HashSet<>();

	/**
	 * Konstruktor ParallelRaster.
	 * 
	 * @param width
	 *            int
	 * @param height
	 *            int
	 * @param amountThreads
	 *            int
	 * @throws IllegalArgumentException
	 *             Indexnummen row und/oder column nicht im gueltigen Bereich
	 */
	ParallelRaster(final int width, final int height, final int amountThreads)
			throws IllegalArgumentException {
		super(width, height);
		if (amountThreads == 0)
			this.amountThreads = Runtime.getRuntime().availableProcessors();
		else
			this.amountThreads = amountThreads;
		rowsDone = new boolean[height];
	}

	/**
	 * Szene rendern.
	 * 
	 * @param raytracer
	 *            Raytracer
	 * @return this hat mit Pixelwerten gefuellten Array
	 */
	@Override
	public Raster render(final Raytracer raytracer) {
		assert raytracer != null : "null raytracer is impossible";
		// 0.5 ist magic number fuer Checkstyle
		final double aHalf = 0.5;
		final int lengthY = this.getHeight();
		final int lengthX = this.getWidth();
		final List<Thread> threadList = new ArrayList<>();
		for (int increment = 0; increment < amountThreads; increment++)
			threadList.add(new Thread(Integer.toString(increment)) {
				public void run() {
					for (int yCoordinate = 0; yCoordinate < lengthY; yCoordinate++) {
						if (!checkRowStatus(yCoordinate)) {
							final double viewportY = 2.0
									* (yCoordinate + aHalf) / lengthY - 1;

							for (int xCoordinate = 0; xCoordinate < lengthX; xCoordinate++) {

								final double viewportX = 2.0
										* (xCoordinate + aHalf) / lengthX - 1;
								final double trace = raytracer.tracePrimary(
										viewportX, viewportY) * MAX_BRIGHTNESS;
								ParallelRaster.this.setPixel(yCoordinate,
										xCoordinate, (int) trace);
							}
						}
						//linesInProcess.remove(yCoordinate);
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
	private synchronized boolean checkRowStatus(final int lineNumber) {
		// if (linesInProcess.contains(lineNumber))
		// return false;

		if (!rowsDone[lineNumber])
			rowsDone[lineNumber] = true;
		return false;
	}

	// linesInProcess.add(lineNumber);
	//
	// return true;
	// }

	public int getAmountThreads() {
		return amountThreads;
	}
}
