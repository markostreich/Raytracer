package de.markostreich.ms.raytracer08.scene.primitive;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;
import de.markostreich.ms.raytracer08.geometry.Point;
import de.markostreich.ms.raytracer08.geometry.Ray;
import de.markostreich.ms.raytracer08.geometry.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Eine Ebene in einem 3D-Koordinatensystem bestehend aus Mittelpunkt und
 * Vektor.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public class Plane extends PrimitiveObject {
	/** Ebenenpunkt. */
	private final Point point;
	/** Ebenenvektor. */
	private final Vector vector;

	/**
	 * <h2>Plane Konstruktor.</h2>
	 * 
	 * @param point
	 *            Point
	 * @param vector
	 *            Vector
	 * @throws IllegalArgumentException
	 *             falscher Argumenttyp
	 */
	public Plane(final Point point, final Vector vector) {
		assert point != null : "null point is impossible";
		assert vector != null : "null vector is impossible";
		this.point = point;
		this.vector = vector.normalize();
	}

	/**
	 * <h2>Schnittpunkt Strahl Ebene.</h2>
	 * 
	 * @param ray
	 *            Ray
	 * @return Liste der Schnittpunkte (Intersection). Leer oder ein Element.
	 * @throws IllegalArgumentException
	 *             falscher Parametertyp.
	 * @see http://www.siggraph.org/education/materials/HyperGraph/raytrace/
	 *      rayplane_intersection.htm
	 */
	@Override
	public List<Intersection> intersections(final Ray ray) {
		assert ray != null : "null ray is impossible";

		final List<Intersection> result = new ArrayList<>();

		/*
		 * Ebene: a*x + b*x + c*x + distanceOrigin = 0 normierter Ebenenvektor:
		 * planeNormVector = (a, b, c) Ebenenpunkt: planePoint distanceOrigin =
		 * -(planeNormVector scalar planePoint) Strahl: ray(root)=rayPoint +
		 * root*rayVector, root>0
		 * 
		 * loese: root = -(planeNormVector scalar rayPoint +
		 * distanceOrigin)/(planeNormVector scalar rayVector) bzw. root = num /
		 * denom denom = planeNormVector scalar rayVector num =
		 * -(planeNormVector scalar rayPoint + distanceOrigin)
		 */

		final double denominator = this.getVector().scalar(ray.getVector());
		if (denominator != 0) {
			final Vector rayPoint = ray.getPoint().makeVector();
			final double distanceOrigin = -(this.getVector().scalar(this
					.getPoint().makeVector()));
			final double num = -(this.getVector().scalar(rayPoint) + distanceOrigin);
			final double root = num / denominator;
			if (root >= 0) // TODO root=0???
				result.add(new Intersection(ray.makePoint(root), this, root,
						true));
		}
		return result;
	}

	/* Getter, hashCode, equals, toString */

	public Point getPoint() {
		return point;
	}

	public Vector getVector() {
		return vector;
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("object not hashable");
	}

	@Override
	public boolean equals(final Object obj) {
		assert obj != null : "null obj is impossible";
		if (obj.getClass() == getClass()) {
			final Plane other = (Plane) obj;
			return other.getPoint().equals(getPoint())
					&& other.getVector().equals(getVector());
		}
		return false;
	}

	@Override
	public String toString() {
		return "Plane [point=" + point + ", vector=" + vector + "]";
	}

	/**
	 * Normalenverctor an einem Oberflaechenpunkt.
	 * 
	 * @param point
	 *            Point
	 * @return Normalenvector Vector
	 */
	@Override
	public Vector getNormal(final Point planePoint) {
		assert planePoint != null : "null planePoint is impossible";
		return vector;
	}
}
