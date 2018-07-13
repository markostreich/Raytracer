package de.markostreich.ms.raytracer08.scene;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Szene aus Datei laden.
 * 
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 *
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
public class LoadedScene extends ScriptedScene {
	
	/**
	 * Konstruktor Szene aus Datei laden.
	 * @param filename String
	 * @throws IOException Keine Datei
	 * @throws ClassNotFoundException nicht looker, sphere, plane, light oder ambient
	 */
	public LoadedScene(final String filename) throws IOException, ClassNotFoundException {
		super(Files.lines(Paths.get(filename)).toArray(String[]::new));
	}
}
