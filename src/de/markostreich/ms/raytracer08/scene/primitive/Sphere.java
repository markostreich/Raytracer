//CHECKSTYLE:OFF MagicNumber
package de.markostreich.ms.raytracer08.scene.primitive;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;
import de.markostreich.ms.raytracer08.geometry.Point;
import de.markostreich.ms.raytracer08.geometry.Ray;
import de.markostreich.ms.raytracer08.geometry.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Eine Kugel in einem 3D-Koordinatensystem bestehend aus Mittelpunkt und
 * Radius.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
@SuppressWarnings("PMD.ShortVariable")
public class Sphere extends PrimitiveObject {

	/** Unmoeglich that == null. */
	private static final String ASSERT_NULL_IMPOSSILBE = "null ray is impossible";
	/** Mittelpunkt. */
	private final Point centerPoint;
	/** Radius. */
	private final double radius;
	private final double radiusSquared;

	/**
	 * <h2>Sphere Konstruktor.</h2>
	 * 
	 * @param centerPoint
	 *            Point
	 * @param radius
	 *            double
	 */
	public Sphere(final Point centerPoint, final double radius) {
		assert centerPoint != null : "null centerPoint is impossible";
		this.centerPoint = centerPoint;
		this.radius = radius;
		this.radiusSquared = radius * radius;
	}

	private double getRadiusSquared() {
		return radiusSquared;
	}

	/**
	 * Liste von Schnittpunkten eines Strahls mit der Kugel.
	 * 
	 * @param ray
	 *            Ray
	 * @return Liste der Schnittpunkte (Intersection). Leer, ein oder zwei
	 *         Elemente.
	 */
	public List<Intersection> intersections(final Ray ray) {
		assert ray != null : ASSERT_NULL_IMPOSSILBE;
		/* Geometrische Loesung */
		final Vector rayCenterVector = getCenterPoint().makeVector().sub(
				ray.getPoint().makeVector());
		/* Ursprung in der Kugel, wenn lengthSquared < radiusSqared */
		final double lengthSquared = rayCenterVector.scalar(rayCenterVector);
		final boolean entersSphere = !(lengthSquared < radiusSquared);
		final double closestApproachAlongRay = rayCenterVector.scalar(ray
				.getVector());
		/* Ursprung außerhalb der Kugel und Mittelpunkt hinter Ursprung? */
		if (lengthSquared >= 0 && closestApproachAlongRay < 0)
			return new ArrayList<Intersection>();
		final double halfChordDistanceSquared = radiusSquared
				- lengthSquared
				+ (closestApproachAlongRay * closestApproachAlongRay);
		/* Strahl geht an der Kugel vorbei? */
		if (halfChordDistanceSquared < 0)
			return new ArrayList<Intersection>();
		final double sqRoot = Math.sqrt(halfChordDistanceSquared);
		final double near = closestApproachAlongRay - sqRoot;
		final double far = closestApproachAlongRay + sqRoot;

		final List<Intersection> result = new ArrayList<>();
		// negative Loesung = jenseits des RayPoints = nicht mehr auf dem
		// Ray

		if (Math.abs(near) < getTolerance() || near > 0.0)
			result.add(new Intersection(ray.makePoint(Math.abs(near)), this,
					near, entersSphere));

		// negative Loesung = jenseits des Ranear > TOLERANCEyPoints = nicht mehr auf dem
		// Ray

		if (Math.abs(far) < getTolerance() || far > 0.0) {
			//final boolean entersSphere = near > TOLERANCE;
			result.add(new Intersection(ray.makePoint(Math.abs(far)), this,
					far, false));
		}
		return result;
	}

	public Point getCenterPoint() {
		return centerPoint;
	}

	public double getRadius() {
		return radius;
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("object not hashable");
	}

	@Override
	public boolean equals(final Object obj) {
		assert obj != null : "null obj is impossible";
		if (this.getClass() != obj.getClass())
			return false;
		final Sphere other = (Sphere) obj;
		return other.getCenterPoint().equals(getCenterPoint())
				&& Math.abs(other.getRadius() - getRadius()) < getTolerance();
	}

	@Override
	public String toString() {
		return "Sphere [centerPoint=" + getCenterPoint() + ", radius="
				+ getRadius() + "]";
	}

	/**
	 * Normalenverctor an einem Oberflaechenpunkt.
	 * 
	 * @param point
	 *            Point
	 * @return Normalenvector Vector
	 */
	@Override
	public Vector getNormal(final Point point) {
		assert point != null : "null point is impossible";
		return point.makeVector().sub(getCenterPoint().makeVector());
	}
}
// CHECKSTYLE:ON MagicNumber