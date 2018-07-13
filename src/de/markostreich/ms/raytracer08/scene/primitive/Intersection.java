package de.markostreich.ms.raytracer08.scene.primitive;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;
import de.markostreich.ms.raytracer08.geometry.Point;

/**
 * Klasse von Schnittpunkten.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public final class Intersection implements Comparable<Intersection> {

	/** Rundungsfehler. */
	private static final double TOLERANCE = 1e-12;
	/** Schnittpunkt. */
	private final Point intersectionPoint;
	/** Geschnittene Form. */
	private final Primitive primitive;
	/** Entfernung Kamera Schnittpunkt. */
	private final double distance;
	/** Eintretender Strahl? */
	private final boolean rayEntrance;

	/**
	 * Konstruktor Schnittpunkt.
	 * 
	 * @param intersectionPoint
	 *            Point
	 * @param primitive
	 *            Primitive
	 * @param distance
	 *            Double
	 * @param rayEntrance
	 *            Boolean
	 * @exception IllegalArgumentException
	 *                distance ist < 0
	 */

	public Intersection(final Point intersectionPoint,
						final Primitive primitive, final double distance,
						final boolean rayEntrance){
		{
			assert intersectionPoint != null : "null intersectionPoint is impossible";
			assert primitive != null : "null primitive is impossible";
//		if (distance < 0 && Math.abs(distance) > TOLERANCE)
			//throw new IllegalArgumentException();
			this.intersectionPoint = intersectionPoint;
			this.primitive = primitive;
			this.distance = distance;
			this.rayEntrance = rayEntrance;
		}
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("object not hashable");
	}

	@Override
	public boolean equals(final Object obj) {
		assert obj != null : "null obj is impossible";
//		if (obj.getClass() == this.getClass()) {
			final Intersection other = (Intersection) obj;
			return Math.abs(other.getDistance() - this.getDistance()) < TOLERANCE
					&& other.getIntersectionPoint().equals(
							this.getIntersectionPoint())
					&& other.getPrimitive().equals(this.getPrimitive());
//		}
//		return false;
	}

	@Override
	public String toString() {
		return "Intersection [intersectionPoint=" + intersectionPoint
				+ ", primitive=" + primitive + ", distance=" + distance
				+ ", rayEntrance=" + rayEntrance +"]";
	}

	/* Getter */
	public Point getIntersectionPoint() {
		return intersectionPoint;
	}

	public Primitive getPrimitive() {
		return primitive;
	}

	public double getDistance() {
		return distance;
	}

	public boolean isRayEntrance() {
		return rayEntrance;
	}

	@Override
	public int compareTo(final Intersection that) {
		assert that != null : "null that is impossible";
		return Double.compare(this.getDistance(), that.getDistance());
	}

}
