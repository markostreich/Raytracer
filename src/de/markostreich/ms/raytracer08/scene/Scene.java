package de.markostreich.ms.raytracer08.scene;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;
import de.markostreich.ms.raytracer08.geometry.Point;
import de.markostreich.ms.raytracer08.geometry.Ray;
import de.markostreich.ms.raytracer08.scene.primitive.Intersection;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * Interface eine Szene fuer den Raytracer.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public interface Scene {

	/**
	 * Szenenbauer.
	 * 
	 * @param args
	 *            String
	 * @return Objekt der Klasse ScriptedScene
	 * @throws ClassNotFoundException
	 *             nicht looker, sphere, plane, light oder ambient
	 * @throws IOException
	 *             Keine Datei
	 * @throws IllegalArgumentException
	 *             zu viele Argumente nach einem Klassennamen

	 */
	static Scene make(final String... args) throws ClassNotFoundException,
			IOException {
		/*
		 * Beispiel Argumente; "ScriptedScene", "looker [0 0 5] [0 0 0] 2 2",
		 * "sphere [0 0 -5] 1.0", "light [0 5 0]"
		 */

		/* Defaultwerte bei fehlenden Werten. */
		if (args.length == 0 || !"LoadedScene".equals(args[0])
				&& !"ScriptedScene".equals(args[0]))
			return new ScriptedScene("looker [0 0 5] [0 0 0] 2 2",
					"sphere [0 0 -5] 1", "ambient 1");

		if ("LoadedScene".equals(args[0])) {
			if (args.length != 2)
				throw new IllegalArgumentException(
						"LoadedScene-Aufruf mit zuvielen Argumenten!");
			return new LoadedScene(args[1]);
		}
		return new ScriptedScene(Arrays.copyOfRange(args, 1, args.length));
	}

	/**
	 * Schnittpunktfinder.
	 * 
	 * @param ray
	 *            Ray
	 * @return Schnittpunkt Intersection
	 */
	Optional<Intersection> findIntersection(final Ray ray);

	/**
	 * Getter fuer Looker (Kamera und Viewport).
	 * 
	 * @return Looker Looker
	 */
	Looker getLooker();

	/**
	 * Getter fuer die Lichtquelle.
	 * 
	 * @return Light Point
	 */
	Optional<Point> getLight();
}
