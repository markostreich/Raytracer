package de.markostreich.ms.raytracer08.scene.primitive;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;

import java.util.EnumMap;

/**
 * Oberflaecheneigenschaften von Primitives.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public class Surface {

	/** Mapped Properties mit Eigenschaftswerten und einem Setzungsstatus. */
	private final EnumMap<Property, SurfaceValue> values = new EnumMap<>(
			Property.class);

	/**
	 * Private Klasse vereinigt Oberflaechenwerte und Setzungsstatus von
	 * Properties.
	 * 
	 * @author M. Huebner, martin.huebner0@hm.edu
	 * @author M. Streich, mstreich@hm.de
	 * @version 2015-05-19
	 */
	private static final class SurfaceValue {

		/** Eigenschaftswert. */
		private double value;
		/** Eigenschaftsstatus. */
		private boolean set;

		/**
		 * Konstruktor SurfaceValue.
		 * 
		 * @param value
		 *            double
		 */
		SurfaceValue(final double value) {
			this.value = value;
		}
	}

	/**
	 * Enum Property. Minimal-, Maximalwerte von ambient, diffuse, specular und
	 * specularExponent
	 * 
	 * @author M. Huebner, martin.huebner0@hm.edu
	 * @author M. Streich, mstreich@hm.de
	 * @version 2015-05-19
	 */
	public static enum Property {
		/** Minimal-, Maximalwert fuer ambient. */
		AmbientRatio(0.0, 1.0),
		/** Minimal-, Maximalwert fuer diffuse. */
		DiffuseRatio(0.0, 1.0),
		/** Minimal-, Maximalwert fuer specular. */
		SpecularRatio(0.0, 1.0),
		/** Minimal-, Maximalwert fuer specularExponent. */
		SpecularExponent(0.0, 1000.0),
		/** Minimal-, Maximalwert fuer reflexion. */
		ReflexionRatio(0.0, 1.0),
		/** Minmal-, Maximalwert fuer transmission. */
		TransmissionRatio(0.0, 1.0),
		/** Minmal-, Maximalwert fuer colour. */
		Colour(0.0, 16777215.0);

		/** Defaultwert ambient. */
		private static final double AMBIENT_DEFAULT = 0.05;
		/** Defaultwert diffuse. */
		private static final double DIFFUSE_DEFAULT = 0.95;
		/** Defaultwert specular. */
		private static final double SPECULAR_DEFAULT = 0.0;
		/** Defaultwert specularExponent. */
		private static final double SPECEXPONENT_DEFAULT = 30;
		/** Defaultwert reflexion. */
		private static final double REFLEXION_DEFAULT = 0.0;
		/** Defaultwert transmission. */
		private static final double TRANSMISSION_DEFAULT = 0.0;
		/**  Defaultwert colour. */
		private static final double COLOUR_DEFAULT = 0.0;
		/** Minimum. */
		private final double minimum;
		/** Maximum. */
		private final double maximum;

		/**
		 * Konstruktor Property.
		 * 
		 * @param minimum
		 *            double
		 * @param maximum
		 *            double
		 */
		Property(final double minimum, final double maximum) {
			this.minimum = minimum;
			this.maximum = maximum;
		}
	}

	/**
	 * Konstruktor Surface.
	 */
	public Surface() {

		values.put(Property.AmbientRatio, new SurfaceValue(
				Property.AMBIENT_DEFAULT));
		values.put(Property.DiffuseRatio, new SurfaceValue(
				Property.DIFFUSE_DEFAULT));
		values.put(Property.SpecularRatio, new SurfaceValue(
				Property.SPECULAR_DEFAULT));
		values.put(Property.SpecularExponent, new SurfaceValue(
				Property.SPECEXPONENT_DEFAULT));
		values.put(Property.ReflexionRatio, new SurfaceValue(
				Property.REFLEXION_DEFAULT));
		values.put(Property.TransmissionRatio, new SurfaceValue(
				Property.TRANSMISSION_DEFAULT));
		values.put(Property.Colour, new SurfaceValue(
				Property.COLOUR_DEFAULT));
	}

	/**
	 * Getter Propertywert.
	 * 
	 * @param property
	 *            Property
	 * @return Propertywert
	 */
	public double get(final Property property) {
		assert property != null : "null property is impossible";
		return values.get(property).value;
	}

	/**
	 * Setter Property-Wert. Fuer Oberflaecheneigenschaft kann ein Wert in
	 * zulaessigen Bereich gesetzt werden. Zulaessige Werte bestimmt das Enum.
	 * Eine Property hat bei der Initialisierung eines Surface-Objektes einen
	 * default-Wert und kann ein einziges mal gesetzt werden.
	 * 
	 * @param property
	 *            Property
	 * @param newValue
	 *            double
	 * @throws IllegalStateException
	 *             Wert der Property wurde bereits gesetzt.
	 * @throws IllegalArgumentException
	 *             Neuer Wert ist nicht im zul. Bereich der Property.
	 */
	public void set(final Property property, final double newValue) {
		assert property != null : "null property is impossible";
		if (values.get(property).set)
			throw new IllegalStateException(
					"Wert der Property wurde bereits gesetzt.");
		if (property.maximum < newValue || property.minimum > newValue)
			throw new IllegalArgumentException(
					"Neuer Wert ist nicht im zul. Bereich der Property!");
		values.get(property).value = newValue;
		values.get(property).set = true;

	}
}
