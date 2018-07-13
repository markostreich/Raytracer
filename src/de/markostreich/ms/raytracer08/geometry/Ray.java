package de.markostreich.ms.raytracer08.geometry;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;

/**
 * <p>
 * Diese Klasse realisiert die Rays eines Raytracers.<br>
 * Ein Ray besteht aus einem Ursprungspunkt und einem Richtungsvector.
 * </p>
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 * 
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public class Ray {

	/** Rundungsfehler. */
	private static final double TOLERANCE = 1e-12;
	/** Maximalwert Gewicht. */
	private static final double MAX_WEIGHT = 0.999;
	/** Ausgangspunkt des Rays. */
	private final Point point;
	/** Richtungsvektor des Rays. */
	private final Vector vector;
	/** Maximalgewicht. */
	private final double maxWeight;

	/**
	 * <h2>Ray von der Kamera auf die Szene</h2>
	 * <p>
	 * Ein Ray besteht aus einem Ursprungspunkt und einem Richtungsvektor.
	 * </p>
	 * 
	 * @param point
	 *            Point.
	 * @param vector
	 *            Vector.
	 * @param maxWeight
	 *            double
	 * @throws IllegalArgumentException
	 *             falscher Parametertyp.
	 */
	public Ray(final Point point, final Vector vector, final double maxWeight)
			throws IllegalArgumentException {
		assert point != null : "null point is impossible";
		assert vector != null : "null vector is impossible";
		if (point.getClass() != Point.class
				|| vector.getClass() != Vector.class)
			throw new IllegalArgumentException();
		this.point = point;
		this.vector = vector.normalize();
		if (maxWeight == 1.0)
			this.maxWeight = MAX_WEIGHT;
		else
			this.maxWeight = maxWeight;
	}

	/**
	 * Punkt eines Strahls. Variable t in Geradengleichung (Ray) einsetzen, um
	 * Punkt zu erhalten.
	 * 
	 * @param scalingIn
	 *            double t >= 0.
	 * @return Punkt auf dem Strahl
	 * @exception IllegalArgumentException
	 *                t < 0.0
	 */
	public Point makePoint(final double scalingIn) {
		final double scaling;
		if (scalingIn < 0.0 && Math.abs(scalingIn) <= TOLERANCE)
			scaling = 0;
		else scaling = scalingIn;
//			if (scaling < 0.0 && Math.abs(scaling) >= TOLERANCE)
//			throw new IllegalArgumentException("scaling < 0.0");
		final Vector rayVectorNormalized = this.getVector().normalize();
		final double xDimension = this.getPoint().getX()
				+ (scaling * rayVectorNormalized.getX());
		final double yDimension = this.getPoint().getY()
				+ (scaling * rayVectorNormalized.getY());
		final double zDimension = this.getPoint().getZ()
				+ (scaling * rayVectorNormalized.getZ());
		return new Point(xDimension, yDimension, zDimension);
	}

	/*
	 * Getter, hashCode, equals, toString
	 */
	public Point getPoint() {
		return point;
	}

	public Vector getVector() {
		return vector;
	}

	public double getMaxWeight() {
		return maxWeight;
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("object not hashable");
	}

	@Override
	public boolean equals(final Object obj) {
		assert obj != null : "null obj is impossible";
//		if (getClass() != obj.getClass())
//			return false;
		final Ray other = (Ray) obj;
		return point.equals(other.point) && vector.equals(other.vector);
	}

	@Override
	public String toString() {
		return "Ray [point=" + getPoint().toString() + ", vector="
				+ getVector().toString() + "]";
	}

}
