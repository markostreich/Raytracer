package de.markostreich.ms.raytracer08.geometry;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;

/**
 * Abstract fuer Punkte und Vectoren im R3 mit einigen Berechnungen.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public
abstract class Cartesian3D {

	/** TOLERANCE. Die Rundungstoleranz. */
	private static final double TOLERANCE = 1e-12;
	/** X-Koordinate. */
	private final double xDimension;
	/** Y-Koordinate. */
	private final double yDimension;
	/** Z-Koordinate. */
	private final double zDimension;

	/**
	 * Konstruktor.
	 * 
	 * @param x
	 *            Double
	 * @param y
	 *            Double
	 * @param z
	 *            Double
	 */
	Cartesian3D(final double x, final double y, final double z) {
		this.xDimension = x;
		this.yDimension = y;
		this.zDimension = z;
	}

	/**
	 * Distanz zweier Koordinatenpunkte.
	 * 
	 * @param other
	 *            Cartesian3D
	 * @return Entfernung double
	 */
	public double distance(final Cartesian3D other) {
		assert other != null : "null other is impossible";
		return Math.sqrt(Math.pow(this.getX() - other.getX(), 2)
				+ Math.pow(this.getY() - other.getY(), 2)
				+ Math.pow(this.getZ() - other.getZ(), 2));
	}

	/**
	 * <h2>Entfernung zum Koordinatenursprung.</h2>
	 * 
	 * @return Entfernung double.
	 */
	public double distanceRoot() {
		return Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2)
				+ Math.pow(getZ(), 2));
	}

	@Override
	public String toString() {
		return "[" + this.getX() + ", " + this.getY() + ", " + this.getZ()
				+ "]";
	}

	/*
	 * Getter
	 */
	public double getX() {
		return xDimension;
	}

	public double getY() {
		return yDimension;
	}

	public double getZ() {
		return zDimension;
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("object not hashable");
	}

	@Override
	public boolean equals(final Object obj) {
		assert obj != null : "null obj is impossible";
//		if (obj.getClass() == this.getClass()) {
			final Cartesian3D other = (Cartesian3D) obj;
			return Math.abs(other.getX() - this.getX()) < TOLERANCE
					&& Math.abs(other.getY() - this.getY()) < TOLERANCE
					&& Math.abs(other.getZ() - this.getZ()) < TOLERANCE;
//		}
//		return false;
	}

}
