package de.markostreich.ms.raytracer08.image;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;
import de.markostreich.ms.raytracer08.raster.Raster;

/**
 * Diese Klasse realisiert die Bildausgabe des Programms im PGM-Dateiformat.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 */
@SuppressWarnings("PMD.UseVarargs")
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public class PGMOut implements Image {

	/** MAX_BRIGHTNESS. Konstante, die die maximale Helligkeit festlegt. */
	private static final int MAX_BRIGHTNESS = 255;

	/**
	 * <h2>asString</h2>.
	 * <p>
	 * Diese Methode wandelt ein zweidimensionales Integer-Array in einen String
	 * um.<\p>
	 * 
	 * @param pixelIn
	 *            Raster
	 * @return String der Pixelwerte fuer eine PGM-Bilddatei
	 * @throws WrongValueException
	 */
	public String asString(final Raster pixelIn) {

		assert pixelIn != null: "null pixsIn is impossible";

		final int rowMaximum = pixelIn.getHeight();
		if (rowMaximum == 0)
			throw new IllegalArgumentException();

		final int columnMaximum = pixelIn.getWidth();
		if (columnMaximum == 0)
			throw new IllegalArgumentException();

		final String newLine = "\n";
		final StringBuilder outPut = new StringBuilder("P2");
		outPut.append(newLine).append(columnMaximum).append(' ')
				.append(rowMaximum).append(newLine).append(MAX_BRIGHTNESS)
				.append(newLine);

		for (int row = rowMaximum - 1; row >= 0; row--) {
			for (int column = 0; column < columnMaximum; column++) {
				outPut.append(String.valueOf(pixelIn.getPixel(row, column)));
				outPut.append(' ');
			}
			outPut.append(newLine);
		}

		return outPut.toString();
	}

	@Override
	public void save(final Raster pixs) {
		assert pixs != null: "null pixs is impossible";
		System.out.println(asString(pixs));
	}
}
