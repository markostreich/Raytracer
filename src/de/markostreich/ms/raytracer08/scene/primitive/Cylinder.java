package de.markostreich.ms.raytracer08.scene.primitive;

import de.markostreich.ms.raytracer08.geometry.Point;
import de.markostreich.ms.raytracer08.geometry.Ray;
import de.markostreich.ms.raytracer08.geometry.Vector;

import java.util.ArrayList;
import java.util.List;

public class Cylinder extends PrimitiveObject {
	private final Point centerPoint;
	private final double height;
	private final double[] yDirection = new double[2];
	private final double radius;
	private final double radiusSquared;
	private final Circle upperCircle;
	private final Circle lowerCirlce;

	public Cylinder(final Point centerPoint, final double height,
			final double radius) {
		this.centerPoint = centerPoint;
		this.height = height;
		this.yDirection[0] = height / 2 + this.centerPoint.getY();
		this.yDirection[1] = this.centerPoint.getY() - height / 2;
		this.radius = radius;
		this.radiusSquared = radius * radius;
		this.upperCircle = new Circle(new Point(centerPoint.getX(),
				yDirection[0], centerPoint.getZ()), new Vector(0, 1, 0),
				radius);
		this.lowerCirlce = new Circle(new Point(centerPoint.getX(),
				yDirection[1], centerPoint.getZ()), new Vector(0, -1, 0),
				radius);
	}

	public Point getCenterPoint() {
		return centerPoint;
	}

	/** Rundungsfehler. */
	private static final double TOLERANCE = 1e-12;
	/** Unmoeglich that == null. */
	private static final String ASSERT_NULL_IMPOSSILBE = "null ray is impossible";

	@Override
	public List<Intersection> intersections(final Ray ray) {
		// System.out.println(ray.getVector().getX());
		final double a = ray.getVector().getX() * ray.getVector().getX()
				+ ray.getVector().getZ() * ray.getVector().getZ();
		// System.out.println(a);
		final double b = ray.getPoint().getX() * ray.getVector().getX()
				+ ray.getPoint().getZ() * ray.getVector().getZ()
				- (centerPoint.getX() * ray.getVector().getX())
				- (centerPoint.getZ() * ray.getVector().getZ());
		final double c = ray.getPoint().getX() * ray.getPoint().getX()
				+ (ray.getPoint().getZ() * ray.getPoint().getZ())
				+ (centerPoint.getX() * centerPoint.getX())
				+ (centerPoint.getZ() * centerPoint.getZ()) - 2
				* (centerPoint.getX() * ray.getPoint().getX()) - 2
				* (centerPoint.getZ() * ray.getPoint().getZ()) - radiusSquared;
		double d = (b * b) - (a * c);
		// System.out.println(d);
		if (d >= 0) {
			final double droot = Math.sqrt(d);
			final double near = (-b - droot) / a;
			final double far = (-b + droot) / a;
			return buildList(ray, near, far);
		}
		// System.out.println("reached");
		return new ArrayList<Intersection>();
	}

	/**
	 * Liste bauen.
	 * 
	 * @param ray
	 *            Ray
	 * @param near
	 *            double
	 * @param far
	 *            double
	 * @return Liste der Schnittpunkte
	 */
	private List<Intersection> buildList(final Ray ray, final double near,
			final double far) {
		assert ray != null : ASSERT_NULL_IMPOSSILBE;
		final List<Intersection> result = new ArrayList<>();
		// negative Loesung = jenseits des RayPoints = nicht mehr auf dem
		// Ray

		if (Math.abs(near) < TOLERANCE || near > 0.0) {
			final Point point = ray.makePoint(near);
			double yDirection = point.getY();
			if (yDirection >= this.yDirection[1]
					&& yDirection <= this.yDirection[0])
				result.add(new Intersection(point, this, near, true));
		}
		// negative Loesung = jenseits des RayPoints = nicht mehr auf dem
		// Ray

		if (Math.abs(far) < TOLERANCE || far > 0.0) {
			final Point point = ray.makePoint(far);
			double yDirection = point.getY();
			if (yDirection >= this.yDirection[1]
					&& yDirection <= this.yDirection[0]) {
				result.add(new Intersection(point, this, far, false));
			}
		}
		if (result.isEmpty()) {
			List<Intersection> upper = upperCircle.intersections(ray);

			if (!upper.isEmpty()){
//				System.out.println(upper.get(0));
				result.add(new Intersection(
						upper.get(0).getIntersectionPoint(), this, upper.get(0)
								.getDistance(), true));
			}
			else {
				List<Intersection> lower = lowerCirlce.intersections(ray);
				if (!lower.isEmpty())
					result.add(new Intersection(lower.get(0)
							.getIntersectionPoint(), this, lower.get(0)
							.getDistance(), true));
			}
		}
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		assert obj != null : "null obj is impossible";
		if (this.getClass() != obj.getClass())
			return false;
		final Cylinder other = (Cylinder) obj;
		return other.getCenterPoint().equals(getCenterPoint())
				&& Math.abs(this.radius - other.radius) <= getTolerance()
				&& Math.abs(this.height - other.height) <= getTolerance();
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("object not hashable");
	}

	@Override
	public String toString() {
		return "Cylinder [centerPoint=" + centerPoint + ", height=" + height
				+ ", radius=" + radius + "]";
	}

	@Override
	public Vector getNormal(final Point point) {
//		if (Math.abs(point.getY() - yDirection[0]) < getTolerance())
//			return upperCircle.getNormal(point);
//		if (point.getY() <= yDirection[1])
//			return lowerCirlce.getNormal(point);
		return new Vector(point.getX() - centerPoint.getX(), 0, point.getZ()
				- centerPoint.getZ());
	}

}
