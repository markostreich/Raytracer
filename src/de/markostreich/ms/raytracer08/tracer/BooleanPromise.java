package de.markostreich.ms.raytracer08.tracer;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * BooleanPromise. Liefert Schattenwert eines Oberflächenpunktes
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 *
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
@SuppressWarnings("PMD.BooleanGetMethodName")
class BooleanPromise {

	/** Supplier. */
	private final Supplier<Boolean> shadowedSupplier;
	/** Schattenwert. */
	private Optional<Boolean> isShadowed;

	/**
	 * Konstruktor.
	 * 
	 * @param shadowedSupplier
	 *            Supplier<Boolean>
	 */
	BooleanPromise(final Supplier<Boolean> shadowedSupplier) {
		assert shadowedSupplier != null: "null shadowedSupplier is impossible";
		this.shadowedSupplier = shadowedSupplier;
		this.isShadowed = Stream.of(shadowedSupplier.get()).findAny();
	}

	/**
	 * Getter des Schattenwertes.
	 * 
	 * @return Schattenwert
	 */
	boolean get() {
		if (!isShadowed.isPresent())
			isShadowed = Stream.of(shadowedSupplier.get()).findAny();
		return isShadowed.get();
	}
}
