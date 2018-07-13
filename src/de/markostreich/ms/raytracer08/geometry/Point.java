package de.markostreich.ms.raytracer08.geometry;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;

/**
 * <p>
 * Diese Klasse realisiert die im Raytracer verwendeten Punkte.<br>
 * Die Punkte, die durch diese Klasse definiert werden koennen, haben genau drei
 * Dimensionen.
 * </p>
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 * 
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public class Point extends Cartesian3D {

	/**
	 * <h2>Point</h2>
	 * <p>
	 * Die drei dem Konstruktor uebergebenen Variablen definieren den Point.<br>
	 * Sie repraesentieren die Punktkoordinaten und sind vom Typ Double.
	 * </p>
	 * 
	 * @param x
	 *            Double
	 * @param y
	 *            Double
	 * @param z
	 *            Double
	 */
	public Point(final double x, final double y, final double z) {
		super(x, y, z);
	}

	/**
	 * <h2>Vector aus einem Punkt</h2>
	 * <p>
	 * Ein Punkt definiert den Vector.<br>
	 * Seine Koordinaten beschreiben in ihrer jeweiligen Dimension den Abstand
	 * zum Koordinatenursprung und sind vom Typ Double.
	 * </p>
	 * 
	 * @param none
	 * @return Vektor mit Richtung auf diesen Punkt

	 */
	public Vector makeVector() {
		return new Vector(this.getX(), this.getY(), this.getZ());
	}

}
