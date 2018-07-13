package de.markostreich.ms.raytracer08.scene.primitive;

import de.markostreich.ms.raytracer08.geometry.Point;
import de.markostreich.ms.raytracer08.geometry.Ray;
import de.markostreich.ms.raytracer08.geometry.Vector;

import java.util.ArrayList;
import java.util.List;

public class Circle extends PrimitiveObject {
	private final Plane plane;
	private final double radius;

	public Circle(final Point point, final Vector vector, final double radius) {
		this.plane = new Plane(point, vector);
		this.radius = radius;
	}

	@Override
	public List<Intersection> intersections(Ray ray) {
		assert ray != null : "null ray is impossible";

		final List<Intersection> result = new ArrayList<>();
		final double denominator = this.plane.getVector().scalar(
				ray.getVector());
		if (denominator != 0) {
			final Vector rayPoint = ray.getPoint().makeVector();
			final double distanceOrigin = -(this.plane.getVector()
					.scalar(this.plane.getPoint().makeVector()));
			final double num = -(this.plane.getVector().scalar(rayPoint) + distanceOrigin);
			final double root = num / denominator;
			if (root >= 0) { // TODO root=0???
				final Point intersectionPoint = ray.makePoint(root);
				if (intersectionPoint.distance(plane.getPoint()) <= radius)
					result.add(new Intersection(intersectionPoint, this,
							root, true));
			}
		}
		return result;
	}

	@Override
	public Vector getNormal(Point point) {
		return plane.getNormal(point);
	}

	@Override
	public String toString() {
		return "Circle [plane=" + plane + ", radius=" + radius + "]";
	}

}
