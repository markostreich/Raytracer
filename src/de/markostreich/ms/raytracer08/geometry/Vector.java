package de.markostreich.ms.raytracer08.geometry;


/**
 * <p>
 * Diese Klasse realisiert die im Raytracer verwendeten Vectoren.<br>
 * Die Vectoren, die durch diese Klasse definiert werden koennen, haben genau
 * drei Dimensionen.
 * </p>
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 * 
 */
public class Vector extends Cartesian3D {

	/** Unmoeglich that == null. */
	private static final String ASSERT_NULL_IMPOSSILBE = "null that is impossible";
	
	/**
	 * <h2>Vector</h2>
	 * <p>
	 * Die drei dem Konstruktor uebergebenen Variablen definieren den Vector.<br>
	 * Sie beschreiben in ihrer jeweiligen Dimension den Abstand zum
	 * Koordinatenursprung und sind vom Typ Double.
	 * </p>
	 * 
	 * @param x
	 *            Double
	 * @param y
	 *            Double
	 * @param z
	 *            Double
	 */
	public Vector(final double x, final double y, final double z) {
		super(x, y, z);
	}

	/**
	 * <h2>Vektor-Addition.</h2>
	 * <p>
	 * Addiert einen Vector(that) mit diesem(this)<br>
	 * und liefert den Ergebnis-Vector zurueck.
	 * </p>
	 * 
	 * @param that
	 *            (Vector)
	 * @return new Vector()
	 * @throws IllegalArgumentException
	 *             Addition von Werten verlässt den Wertebereich von Double.
	 */
	public Vector add(final Vector that) throws IllegalArgumentException {
		assert that != null: ASSERT_NULL_IMPOSSILBE;
		return new Vector(this.getX() + that.getX(), this.getY() + that.getY(),
				this.getZ() + that.getZ());
	}

	/**
	 * <h2>Vektor-Subtraktion.</h2>
	 * <p>
	 * Subtrahiert einen Vector (that) von diesem<br>
	 * und liefert das Ergebnis als Vector zurueck.
	 * </p>
	 * 
	 * @param that
	 *            (Vector)
	 * @return neuer Vector()
	 */
	public Vector sub(final Vector that) {
		assert that != null: ASSERT_NULL_IMPOSSILBE;
		return new Vector(this.getX() - that.getX(), this.getY() - that.getY(),
				this.getZ() - that.getZ());
	}

	/**
	 * <h2>Laenge auslesen.</h2>
	 * <p>
	 * Berechnet die Laenge dieses Vectors<br>
	 * und liefert das Ergebnis im Format Double zurueck.
	 * </p>
	 * 
	 * @return Laenge des Vektors (Double)
	 */
	public double getLength() {
		return this.distanceRoot();
	}

	/**
	 * <h2>Laenge setzen.</h2>
	 * <p>
	 * Berechnet die Koordinaten auf Basis dieses Vektors fuer einen neuen
	 * Vektor mit gleicher Richtung.<br>
	 * Dazu wird der Vektor normalisiert und dann um "length" gestreckt /
	 * gestaucht.
	 * </p>
	 * 
	 * @param length
	 *            Vector
	 * @return Vector von Laenge length
	 */
	public Vector setLength(final double length) {
		final Vector normalized = normalize();
		return new Vector(normalized.getX() * length, normalized.getY()
				* length, normalized.getZ() * length);
	}

	/**
	 * <h2>Normierung.</h2>
	 * <p>
	 * Kuerzt diesen Vector auf die Laenge 1<br>
	 * (und liefert als Ergebnis diesen Vector zurueck).
	 * </p>
	 * 
	 * @return normalisierter Vektor
	 */
	public Vector normalize() {
		final double length = this.getLength();
		return new Vector(this.getX() / length, this.getY() / length,
				this.getZ() / length);
	}

	/**
	 * <h2>Kreuzrodukt.</h2>
	 * <p>
	 * Liefert den Normal-Vektor, der durch this und that definiert ist.<br>
	 * Der Normal-Vektor ist senkrecht zu this und ist senkrecht zu that.
	 * </p>
	 * (Rechte-Hand-Regel beachten!)
	 * 
	 * @param that
	 *            (Vector) - Vector, mit dem das <br>
	 *            kartesische Produkt gebildet werden soll.
	 * @return new Vector(x, y, z);
	 * 
	 */
	public Vector crossProduct(final Vector that) {
		assert that != null: ASSERT_NULL_IMPOSSILBE;
		final double xTemp = this.getY() * that.getZ() - this.getZ()
				* that.getY();
		final double yTemp = this.getZ() * that.getX() - this.getX()
				* that.getZ();
		final double zTemp = this.getX() * that.getY() - this.getY()
				* that.getX();

		return new Vector(xTemp, yTemp, zTemp);
	}

	/**
	 * * <h2>Skalarprodukt.</h2>
	 * <p>
	 * Die Methode bildet das Skalarprodukt zweier Vektoren<br>
	 * (diesem und einem uebergebenen).
	 * </p>
	 * 
	 * @param that
	 *            Vector
	 * @return scalar (Double)
	 */
	public double scalar(final Vector that) {
		assert that != null: ASSERT_NULL_IMPOSSILBE;
		return this.getX() * that.getX() + this.getY() * that.getY()
				+ this.getZ() * that.getZ();
	}

	/**
	 * Vector Spiegeln an einem anderen Vektor.
	 * 
	 * @param onThat
	 *            Vector
	 * @return gespiegelter Vector
	 */
	public Vector mirror(final Vector onThat) {
		assert onThat != null: "null onThat is impossible";
		final Vector thisNormalized = this.normalize();
		final Vector onThatNormalized = onThat.normalize();
		return thisNormalized.sub(onThatNormalized.scale(thisNormalized
				.scalar(onThatNormalized) * 2));
	}

	/**
	 * <h2>Vektor skalieren.</h2>
	 * <p>
	 * Der Vektor um "scalar" gestreckt / gestaucht.
	 * </p>
	 * 
	 * @param scalar
	 *            Vector
	 * @return Vector von Laenge length
	 */
	public Vector scale(final double scalar) {
		return new Vector(getX() * scalar, getY() * scalar, getZ() * scalar);
	}

	/**
	 * Kosinus des Winkels zwischen zwei Vectoren.
	 * 
	 * @param that
	 *            Vector
	 * @return Kosinus des Winkels Double
	 */
	public double cosAngle(final Vector that) {
		assert that != null: ASSERT_NULL_IMPOSSILBE;
		final double result = this.scalar(that)
				/ (this.getLength() * that.getLength());
		return result;
	}

	/**
	 * Punkt aus Vektorkoordinaten.
	 * 
	 * @return Punkt aus Vektorkoordinaten
	 */
	public Point makePoint() {
		return new Point(getX(), getY(), getZ());
	}
}
