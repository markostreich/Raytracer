package de.markostreich.ms.raytracer08.scene.primitive;

import de.markostreich.ms.raytracer08.geometry.Point;
import de.markostreich.ms.raytracer08.geometry.Ray;
import de.markostreich.ms.raytracer08.geometry.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Primitive Quadric of a Raytracer
 * 
 * @author Marko Streich, ms@markostreich.de
 * @version 2015-10-23
 */
public class Quadric extends PrimitiveObject {
	private final Point centerPoint;
	// private final double height;
	// private final double[] yDirection = new double[2];
	private final double[] matrix;

	public Quadric(final Point centerPoint,/*
											 * final double height, final double
											 * radius,
											 */final double... matrix) {
		this.centerPoint = centerPoint;
		// this.height = height;
		// this.yDirection[0] = height / 2 + this.centerPoint.getY();
		// this.yDirection[1] = this.centerPoint.getY() - height / 2;
		this.matrix = matrix;
	}

	@Override
	public List<Intersection> intersections(final Ray ray) {
		/* Nullstellenbestimmung: */
		final double xd = ray.getVector().getX();
		final double yd = ray.getVector().getY();
		final double zd = ray.getVector().getZ();
		final double xo = ray.getPoint().getX();
		final double yo = ray.getPoint().getY();
		final double zo = ray.getPoint().getZ();
		final double xq = centerPoint.getX();
		final double yq = centerPoint.getY();
		final double zq = centerPoint.getZ();
		final double Aq = xd * xd * matrix[0] + 2 * xd * yd * matrix[1] + 2
				* xd * zd * matrix[2] + yd * yd * matrix[4] + 2 * yd * zd
				* matrix[5] + zd * zd * matrix[7];
		final double Bq = xd * xo * matrix[0] - xd * xq * matrix[0] + xo * yd
				* matrix[1] - xq * yd * matrix[1] + xd * yo * matrix[1] - xd
				* yq * matrix[1] + xo * zd * matrix[2] - xq * zd * matrix[2]
				+ xd * zo * matrix[2] - xd * zq * matrix[2] + xd * matrix[3]
				+ yd * yo * matrix[4] - yd * yq * matrix[4] + yo * zd
				* matrix[5] - yq * zd * matrix[5] + yd * zo * matrix[5] - yd
				* zq * matrix[5] + yd * matrix[6] + zd * zo * matrix[7] - zd
				* zq * matrix[7] + zd * matrix[8];
		final double Cq = xo * xo * matrix[0] - 2 * xo * xq * matrix[0] + xq
				* xq * matrix[0] + 2 * xo * yo * matrix[1] - 2 * xq * yo
				* matrix[1] - 2 * xo * yq * matrix[1] + 2 * xq * yq * matrix[1]
				+ 2 * xo * zo * matrix[2] - 2 * xq * zo * matrix[2] - 2 * xo
				* zq * matrix[2] + 2 * xq * zq * matrix[2] + 2 * xo * matrix[3]
				- 2 * xq * matrix[3] + yo * yo * matrix[4] - 2 * yo * yq
				* matrix[4] + yq * yq * matrix[4] + 2 * yo * zo * matrix[5] - 2
				* yq * zo * matrix[5] - 2 * yo * zq * matrix[5] + 2 * yq * zq
				* matrix[5] + 2 * yo * matrix[6] - 2 * yq * matrix[6] + zo * zo
				* matrix[7] - 2 * zo * zq * matrix[7] + zq * zq * matrix[7] + 2
				* zo * matrix[8] - 2 * zq * matrix[8] + matrix[9];
		if (Math.abs(Aq) < getTolerance()) {
			final List<Intersection> result = new ArrayList<>();
			final double near = -Cq / Bq;
			final Point point = ray.makePoint(near);
			result.add(new Intersection(point, this, near, true));
		}
		final double d = (Bq * Bq) - (Aq * Cq);
		if (d < 0)
			return new ArrayList<Intersection>();
		final double droot = Math.sqrt(d);
		final double near = (-Bq - droot) / Aq;
		final double far = (-Bq + droot) / Aq;

		final List<Intersection> result = new ArrayList<>();
		// negative Loesung = jenseits des RayPoints = nicht mehr auf dem
		// Ray

		if (Math.abs(near) < getTolerance() || near > 0.0){
			final Point intersectionPoint = ray.makePoint(Math.abs(near));
//			if (Math.abs(intersectionPoint.getZ()) < 20)
			result.add(new Intersection(intersectionPoint, this,
					near, true));
		}
		// negative Loesung = jenseits des Ray
		// near > TOLERANCE yPoints = nicht mehr auf dem Ray

		if (Math.abs(far) < getTolerance() || far > 0.0) {
			final Point intersectionPoint = ray.makePoint(Math.abs(far));
//			if (Math.abs(intersectionPnt.getZ()) < 20)
			result.add(new Intersection(intersectionPoint, this,
					far, false));
		}
		return result;

	}

	@Override
	public Vector getNormal(final Point point) {
		final double xn = 2 * (matrix[0] * point.getX() + matrix[1]
				* point.getY() + matrix[2] * point.getZ() + matrix[3]);
		final double yn = 2 * (matrix[1] * point.getX() + matrix[4]
				* point.getY() + matrix[5] * point.getZ() + matrix[6]);
		final double zn = 2 * (matrix[2] * point.getX() + matrix[5]
				* point.getY() + matrix[7] * point.getZ() + matrix[8]);
		return new Vector(xn, yn, zn);
	}

}
