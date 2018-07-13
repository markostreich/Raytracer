package de.markostreich.ms.raytracer08.scene;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;
import de.markostreich.ms.raytracer08.geometry.Point;
import de.markostreich.ms.raytracer08.geometry.Ray;
import de.markostreich.ms.raytracer08.geometry.Vector;

/**
 * Looker. Die Klasse Looker fasst die Kamera und den Viewport zusammen. - Also
 * den Standpunkt, die Blickrichtung und den Sichtbereich.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public class Looker {

	/** <h2>Standpunkt bzw. Kamerapunkt.</h2> */
	private final Point camera;

	/** <h2>Viewportpunkt. Ort des definierten Sichtbereichs.</h2> */
	private final Point viewPortCenter;

	/** Blickrichtung. Vektor vom Kamerapunkt zum Viewportpunkt. */
	private final Vector cameraViewPortCenter;

	/**
	 * Viewportvektor(rechts). Ausdehnung des Sichtbereichs (von
	 * Sichtbereichsmitte nach rechts).
	 */
	private final Vector viewPortRight;

	/**
	 * Viewportvektor(oben). Ausdehnung des Sichtbereichs (von
	 * Sichtbereichsmitte nach oben).
	 */
	private final Vector viewPortUp;

	/**
	 * Konstruktor Looker.
	 * 
	 * @param camera
	 *            Point
	 * @param viewPortCenter
	 *            Point
	 * @param width
	 *            Double
	 * @param height
	 *            Double
	 */
	public Looker(final Point camera, final Point viewPortCenter,
			final double width, final double height) {
		assert camera != null: "null camera is impossible";
		assert viewPortCenter != null: "null viewPortCenter is impossible";
		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException(
					"Breite und Hoehe duerfen nicht kleiner oder gleich 0 sein");

		this.camera = camera;
		this.viewPortCenter = viewPortCenter;

		// Ermittlung des Blickvektors. (Vektor zwischen Kamera und
		// Viewport-Zentrum(Bildbereichs-Mitte))
		this.cameraViewPortCenter = this.viewPortCenter.makeVector().sub(this.camera.makeVector());

		/*
		 * Berechnung von viewPortRight. Karthesisches Produkt mit einem Vektor
		 * in y-Richtung. Laengen-Skalierung auf Viewportbreite.
		 */
		viewPortRight = cameraViewPortCenter.crossProduct(new Vector(0, 1, 0))
				.setLength(width / 2);

		/*
		 * Berechnung von viewPortUp.
		 * Karthesisches Produkt mit cameraViewPortCenter-Vektor.
		 */
		viewPortUp = viewPortRight.crossProduct(cameraViewPortCenter).setLength(
				height / 2);

	}

	/**
	 * Strahlerzeuger.
	 * 
	 * @param viewportX
	 *            Double
	 * @param viewportY
	 *            Double
	 * @return Stahl
	 */
	public Ray getPrimaryRay(final double viewportX, final double viewportY) {
		if (viewportX < -1 || viewportX > 1 || viewportY < -1 || viewportY > 1)
			throw new IllegalArgumentException();

		return new Ray(camera, cameraViewPortCenter.add(viewPortRight
				.scale(viewportX).add(viewPortUp.scale(viewportY))),1);
	}

	/*
	 * Getter und Setter
	 */
	public Point getCamera() {
		return camera;
	}

	public Vector getCameraViewPortCenter() {
		return cameraViewPortCenter;
	}

	public Point getViewPortCenter() {
		return viewPortCenter;
	}

	public Vector getViewPortRight() {
		return viewPortRight;
	}

	public Vector getViewPortUp() {
		return viewPortUp;
	}

	@Override
	public String toString() {
		return "Looker [camera=" + camera + ", viewPortCenter="
				+ viewPortCenter + ", cameraViewPortCenter="
				+ cameraViewPortCenter + ", viewPortRight=" + viewPortRight
				+ ", viewPortUp=" + viewPortUp + "]";
	}
}
