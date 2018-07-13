/* (C) 2016, M. Streich, mstreich@hm.edu
 * Oracle Corporation Java 1.8.0_91, Ubuntu 15.10 64 Bit
 * Intel Celeron 2957U 1.4 GHz x 2, 3.8 GiB RAM
 **/
package de.markostreich.ms.raytracer08.tracer;

import de.markostreich.ms.raytracer08.scene.Scene;
import de.markostreich.ms.raytracer08.scene.primitive.Intersection;
import de.markostreich.ms.raytracer08.scene.primitive.Surface;

import java.util.Optional;

/**
 * @author M. Streich, mstreich@hm.edu
 * @version 29.08.16.
 */
public class Colour implements LightModel {
    /**
     * Berechnung des Beitrages eines Lichtmodells zu einem Pixel.
     *
     * @param scene        Scene
     * @param intersection Optional<Intersection>
     * @return Beitrag des Lichtmodells zu einem Pixel
     */
    @Override
    public double calculate(Scene scene, Optional<Intersection> intersection) {
        if (intersection.isPresent())
            return intersection.get().getPrimitive().getSurface()
            .get(Surface.Property.Colour);
        else return 0.0;
    }
}
