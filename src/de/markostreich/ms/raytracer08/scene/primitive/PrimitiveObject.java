package de.markostreich.ms.raytracer08.scene.primitive;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;

/**
 * ABC mit Objektvariable surface und dem Getter fuer Primitives.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public abstract class PrimitiveObject implements Primitive {

	/** Rundungsfehler. */
	private static final double TOLERANCE = 1e-12;
	/** Oberflaeche eines ScenenObjektes. */
	private final Surface surface = new Surface();

	/* Getter */
	public Surface getSurface() {
		return surface;
	}

	public static double getTolerance() {
		return TOLERANCE;
	}
}
