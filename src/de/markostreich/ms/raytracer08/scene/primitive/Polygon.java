package de.markostreich.ms.raytracer08.scene.primitive;

import de.markostreich.ms.raytracer08.geometry.Point;
import de.markostreich.ms.raytracer08.geometry.Ray;
import de.markostreich.ms.raytracer08.geometry.Vector;

import java.util.ArrayList;
import java.util.List;

public class Polygon extends PrimitiveObject {

	private final Primitive primitive;
	private final List<Point> points;
	//private final List<Point2D> points2D;
	//private final byte discardDimension;
	private final int amountPoints;

	public Polygon(final Primitive primitive, final List<Point> listPointsPolygon) {
		this.primitive = primitive;
		this.points = listPointsPolygon;

		

		

		//this.points2D = points2D;

		//this.discardDimension = discardDimension;
		this.amountPoints = listPointsPolygon.size();
	}

	private static Point2D discardADimension(final Point point,
			final byte discardDimension) {
		switch (discardDimension) {
		case 0:
			return new Point2D(point.getY(), point.getZ());
		case 1:
			return new Point2D(point.getX(), point.getZ());
		default:
			return new Point2D(point.getX(), point.getY());
		}
	}

	private static class Point2D {
		private final double u;
		private final double v;

		private Point2D(final double u, final double v) {
			this.u = u;
			this.v = v;
		}

		private double getU() {
			return u;
		}

		private double getV() {
			return v;
		}
	}

	@Override
	public List<Intersection> intersections(final Ray ray) {
		final List<Intersection> result = primitive.intersections(ray);
		if (result.isEmpty())
			return result;
		final Point intersectionPoint = result.get(0).getIntersectionPoint();
		if (onPolygon(intersectionPoint))
			return result;
		return new ArrayList<Intersection>();
	}

	private static byte getSign(final double coordinate) {
		return (byte) (coordinate < 0 ? -1 : 1);
	}

	private boolean onPolygon(final Point intersectionPoint) {
		
		final Vector planeNormal = primitive.getNormal(intersectionPoint);
		
		byte discardDimension = 0;
		if (Math.abs(planeNormal.getX()) < Math.abs(planeNormal.getY()))
			discardDimension = 1;
		if (Math.abs(planeNormal.getX()) < Math.abs(planeNormal.getZ())
				&& Math.abs(planeNormal.getY()) < Math.abs(planeNormal.getZ()))
			discardDimension = 2;
		
		final List<Point2D> points2D = new ArrayList<>();
		for (final Point pointP : points)
			points2D.add(discardADimension(pointP, discardDimension));
		
		final Point2D origin = discardADimension(intersectionPoint, discardDimension);

		final List<Point2D> listPoints = new ArrayList<>();
		for (final Point2D pointE : points2D) {
			listPoints.add(new Point2D(pointE.getU() - origin.getU(), pointE
					.getV() - origin.getV()));
		}
		
		int numberOfCrossings = 0;
		byte signHolder = getSign(listPoints.get(0).getV());
		byte nextSignHolder;

		for (int a = 0; a < amountPoints; a++) {
			int b = (a + 1) % amountPoints;
			nextSignHolder = getSign(listPoints.get(b).getV());
			if (signHolder != nextSignHolder) {
				if (listPoints.get(a).getU() > 0
						&& listPoints.get(b).getU() > 0)
					numberOfCrossings++;
				else {
					if (listPoints.get(a).getU() > 0
							|| listPoints.get(b).getU() > 0)
						if (listPoints.get(a).getU()
								- listPoints.get(a).getV()
								* (listPoints.get(b).getU() - listPoints.get(a)
										.getU())
								/ (listPoints.get(b).getV() - listPoints.get(a)
										.getV()) > 0)
							numberOfCrossings++;
					//System.out.println("Reached");
				}
				signHolder = nextSignHolder;
			}
		}
		//if (numberOfCrossings > 0)
			//System.out.println(numberOfCrossings);
		if (numberOfCrossings % 2 != 0){
			//System.out.println(numberOfCrossings);
			return true;
		}
		return false;
		
	}

	@Override
	public Vector getNormal(Point point) {
		return primitive.getNormal(point);
	}

	@Override
	public Surface getSurface() {
		return primitive.getSurface();
	}
}
