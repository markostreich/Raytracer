package de.markostreich.ms.raytracer08;


import de.markostreich.ms.raytracer08.image.Image;
import de.markostreich.ms.raytracer08.raster.Raster;
import de.markostreich.ms.raytracer08.scene.Scene;
import de.markostreich.ms.raytracer08.tracer.Raytracer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Hauptprogramm des Raytracers. Schluessel-Wert-Paare koennen sowohl in der
 * Kommandozeile eingegeben, als auch in System-Properties und
 * Umgebungsvariablen gesucht werden. Werden an keiner dieser Stellen Werte
 * gefunden, werden Defaultwerte gestartet.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public class Ratrace {

	/**
	 * Hauptprogramm.
	 * 
	 * @param args
	 *            String
	 * @throws ClassNotFoundException
	 *             Bei Aufrufen von Scenenprimitiven, die nicht looker, sphere,
	 *             plane, light oder ambient sind. Anderes Raster als
	 *             ArrayRaster angefordert. Anderes Image als PGMOut oder
	 *             PNGImage angefordert.
	 * @throws IOException
	 *             Angegebene Datei ist nicht vorhanden.
	 */
	public static void main(final String... args)
			throws ClassNotFoundException, IOException {
		final long start = System.currentTimeMillis();
		/*
		 * gather sammelt die Strings. Je Kommandozeilenargument eine Zeile. Je
		 * Stringteil eine Spalte.
		 */
		final String[][] gather = new String[args.length][];

		for (int increment = 0; increment < args.length; increment++) {
			final List<String> list = makeList(Optional.ofNullable(args[increment]));
			gather[increment] = new String[list.size()];
			list.toArray(gather[increment]);
		}
//		System.out.println(System.currentTimeMillis()-start);
		/* Initialisieren der Objekte des Raytracers. */
		final Image image = makeImage(gather);
		final Raster raster = makeRaster(gather);
		final Scene scene = makeScene(gather);
		final Raytracer raytracer = new Raytracer(scene);

		/* Bildausgabe. */
		image.save(raster.render(raytracer));
		
		System.out.println(System.currentTimeMillis()-start);
	}

	/**
	 * makeList. Aus einem String wird eine Liste von Strings erzeugt. "=" und
	 * "," sind Trennzeichen. Gaensefueßchen " werden ignoriert. "#" ist Marker
	 * fuer Kommentare.
	 * 
	 * @param stri
	 *            Optional<String>
	 * @return Liste von Strings aus dem Originalstring.
	 */
	private static List<String> makeList(final Optional<String> stri) {
		String str = stri.get();
		int strPos = 0;

		final List<String> result = new ArrayList<>();

		while (strPos < str.length() && str.charAt(strPos) != '#') {
			if (str.charAt(strPos) == '=' || str.charAt(strPos) == ',') {
				result.add(str.substring(0, strPos).trim());
				str = str.substring(strPos + 1, str.length());
				strPos = -1;
			}
			strPos++;
		}
		if (strPos < str.length() && str.charAt(strPos) == '#') {
			result.add(str.substring(0, strPos).trim());
		} else
			result.add(str.trim());
		return result;
	}

	/**
	 * Imagemaker.
	 * 
	 * @param gather
	 *            String[][] Strings der Kommandozeile.
	 * @return Objekt der Klasse Image
	 * @throws ClassNotFoundException
	 *             Anderes Image als PGMOut oder PNGImage angefordert.
	 */
	private static Image makeImage(final String[]... gather)
			throws ClassNotFoundException {
		final String image = "image";
		final Optional<Integer> incr = Stream
				.iterate(0, incre -> incre + 1)
				.limit(gather.length)
				.filter(incre -> gather[incre] != null
						&& image.equals(gather[incre][0])).findAny();
		if (incr.isPresent())
			return Image.make(Arrays.copyOfRange(gather[incr.get()], 1,
					gather[incr.get()].length));
		Optional<String> value = Optional.ofNullable(System
				.getProperty("Dimage"));
		if (!value.isPresent())
			value = Optional.ofNullable(System.getenv(image));
		if (value.isPresent()) {
			final List<String> stringList = makeList(value);
			final String[] valueStrings = new String[stringList.size()];
			stringList.toArray(valueStrings);
			return Image.make(valueStrings);
		}
		return Image.make();
	}

	/**
	 * Rastermaker.
	 * 
	 * @param gather
	 *            String[][] Strings der Kommandozeile.
	 * @return Objekt der Klasse Raster
	 * @throws ClassNotFoundException
	 *             Anderes Raster als ArrayRaster angefordert.
	 */
	private static Raster makeRaster(final String[]... gather)
			throws ClassNotFoundException {
		final String raster = "raster";
		final Optional<Integer> incr = Stream
				.iterate(0, incre -> incre + 1)
				.limit(gather.length)
				.filter(incre -> gather[incre] != null
						&& raster.equals(gather[incre][0])).findAny();
		if (incr.isPresent())
			return Raster.make(Arrays.copyOfRange(gather[incr.get()], 1,
					gather[incr.get()].length));
		Optional<String> value = Optional.ofNullable(System
				.getProperty("Draster"));
		if (!value.isPresent())
			value = Optional.ofNullable(System.getenv(raster));
		if (value.isPresent()) {
			final List<String> stringList = makeList(value);
			final String[] valueStrings = new String[stringList.size()];
			stringList.toArray(valueStrings);
			return Raster.make(valueStrings);
		}
		return Raster.make();
	}

	/**
	 * Scenemaker.
	 * 
	 * @param gather
	 *            String[][] Strings der kommandozeile.
	 * @return Objekt der Klasse Scene
	 * @throws ClassNotFoundException
	 *             Bei Aufrufen von Scenenprimitiven, die nicht looker, sphere,
	 *             plane, light oder ambient sind.
	 * @throws IOException
	 *             Angegebene Datei ist nicht vorhanden.
	 */
	private static Scene makeScene(final String[]... gather)
			throws ClassNotFoundException, IOException {
		final String scene = "scene";
		final Optional<Integer> incr = Stream
				.iterate(0, incre -> incre + 1)
				.limit(gather.length)
				.filter(incre -> gather[incre] != null
						&& scene.equals(gather[incre][0])).findAny();
		if (incr.isPresent())
			return Scene.make(Arrays.copyOfRange(gather[incr.get()], 1,
					gather[incr.get()].length));
		Optional<String> value = Optional.ofNullable(System
				.getProperty("Dscene"));
		if (!value.isPresent())
			value = Optional.ofNullable(System.getenv(scene));
		if (value.isPresent()) {
			final List<String> stringList = makeList(value);
			final String[] valueStrings = new String[stringList.size()];
			stringList.toArray(valueStrings);
			return Scene.make(valueStrings);
		}
		return Scene.make();
	}
}
