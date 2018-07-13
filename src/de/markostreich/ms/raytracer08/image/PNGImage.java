package de.markostreich.ms.raytracer08.image;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;
import de.markostreich.ms.raytracer08.raster.Raster;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * Diese Klasse realisiert die Bildausgabe des Programms im PNG-Dateiformat.
 *
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public class PNGImage implements Image {
    /**
     * Dateiname.
     */
    private final String filename;

    /**
     * Konstrunktor.
     *
     * @param filename String
     */
    public PNGImage(final String filename) {
        assert filename != null : "null filename is impossible";
        this.filename = filename;
    }

    /**
     * Raster in PNG-Bilddatei.
     *
     * @param pixs Raster
     * @throws IOException kann von IntStream geworfen werden
     */
    private void asPNG(final Raster pixs) throws IOException {
        assert pixs != null : "null pixs is impossible";
        final int width = pixs.getWidth();
        final int height = pixs.getHeight();

        final BufferedImage image = new BufferedImage(width, height,
                TYPE_INT_ARGB);
        IntStream.range(0, height).forEach(
                row -> IntStream.range(0, width).forEach(
                        column -> image.setRGB(column, row, /*byteToARGB(*/pixs
                                .getPixel(height - 1 - row, column))));

        ImageIO.write(image, "png", new File(filename));
    }

    /**
     * PNG lesbaren Wert erstellen.
     *
     * @param brightness int
     * @return Helligkeit.
     */
    private static int byteToARGB(final int brightness) {
        final int bitsInByte = 8;
        final int opaqueBitmask = 0xFF;
        final int red = 0xFFFF0000;
//        int result = ((opaqueBitmask << (bitsInByte) | brightness) << bitsInByte | brightness) << bitsInByte
//                | brightness;
        int result = (0xFF << 24) | (brightness << 16) | (brightness << 8) | brightness;
        result = result | red;
        return result;
    }

    @Override
    public void save(final Raster pixs) {
        assert pixs != null : "null pixs is impossible";
        try {
            asPNG(pixs);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

}
