package de.markostreich.ms.raytracer08.scene;

import de.markostreich.ms.raytracer08.SuppressFBWarnings;
import de.markostreich.ms.raytracer08.geometry.Point;
import de.markostreich.ms.raytracer08.geometry.Ray;
import de.markostreich.ms.raytracer08.geometry.Vector;
import de.markostreich.ms.raytracer08.scene.primitive.*;
import de.markostreich.ms.raytracer08.scene.primitive.Surface.Property;

import java.util.*;

/**
 * Szenenskript.
 *
 * @author M. Huebner, martin.huebner0@hm.edu
 * @author M. Streich, mstreich@hm.de
 * @version 2015-06-14
 */
@SuppressFBWarnings("UCF_USELESS_CONTROL_FLOW")
class ScriptedScene implements Scene {
    /**
     * Looker.
     */
    private Looker looker;
    /**
     * Lichtpunkt.
     */
    private final List<Point> light = new ArrayList<>();

    /**
     * Liste der Objekte in der Szene.
     */
    private final List<Primitive> arrayListPrimitives = new ArrayList<Primitive>();

    /**
     * Konstruktor. Liest mehrere Strings ein und erstellt ein Szenenobjekt,
     * wenn ein Looker enthalten ist und weitere Szenenelemente (Lichtquelle,
     * Kugeln, Ebenen) lesbar enthalten sind.
     *
     * @param sceneObjectsIn String...
     * @throws ClassNotFoundException   nicht looker, sphere, plane, light, ambient, diffuse oder
     *                                  specular
     * @throws IllegalArgumentException zu viele Argumente nach einem Klassennamen oder nicht genau
     *                                  ein Looker.
     */
    ScriptedScene(final String... sceneObjectsIn) throws ClassNotFoundException {
        // final long start = System.currentTimeMillis();
        // Durchlaufen des String-Arrays, um die Objekte und deren Parameter zu
        // identifizieren

        assert sceneObjectsIn != null : "null sceneObjectsIn is impossible";
        int amountLooker = 0;

        for (int currentRowIndex = 0; currentRowIndex < sceneObjectsIn.length; currentRowIndex++) {

            final String rowString = sceneObjectsIn[currentRowIndex];
            final Iterator<String> stringIterator = new IterableString(
                    rowString).iterator();

            amountLooker = parser(stringIterator, amountLooker);

            if (stringIterator.hasNext())
                throw new IllegalArgumentException("Zu viele Argumente!");
        }
        if (amountLooker != 1)
            throw new IllegalArgumentException("Not exactly one Looker");
        // System.out.println("ScriptedScene: "
        // + (System.currentTimeMillis() - start));
    }

    /**
     * Methode zum parsen der Strings im Iterator.
     *
     * @param stringIterator Iterator<String>
     * @param lookerAmount   int
     * @return aktuelle Zahl der Looker
     * @throws ClassNotFoundException nicht looker, sphere, plane, light, ambient, diffuse oder
     *                                specular
     */
    private int parser(final Iterator<String> stringIterator,
                       final int lookerAmount) throws ClassNotFoundException {
        assert stringIterator != null : "null stringIterator is impossible";
        int amountLooker = lookerAmount;
        final String current = stringIterator.next();
        switch (current) {
            case "looker":
                amountLooker++;
                looker = new Looker(new Point(Double.parseDouble(stringIterator
                        .next()), Double.parseDouble(stringIterator.next()),
                        Double.parseDouble(stringIterator.next())), new Point(
                        Double.parseDouble(stringIterator.next()),
                        Double.parseDouble(stringIterator.next()),
                        Double.parseDouble(stringIterator.next())),
                        Double.parseDouble(stringIterator.next()),
                        Double.parseDouble(stringIterator.next()));
                break;

            case "light":
                light.add(new Point(Double.parseDouble(stringIterator.next()),
                        Double.parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next())));
                break;

            case "sphere":
                final Sphere sphere = new Sphere(new Point(
                        Double.parseDouble(stringIterator.next()),
                        Double.parseDouble(stringIterator.next()),
                        Double.parseDouble(stringIterator.next())),
                        Double.parseDouble(stringIterator.next()));
                arrayListPrimitives.add(sphere);
                break;

            case "plane":
                final Plane plane = new Plane(new Point(
                        Double.parseDouble(stringIterator.next()),
                        Double.parseDouble(stringIterator.next()),
                        Double.parseDouble(stringIterator.next())), new Vector(
                        Double.parseDouble(stringIterator.next()),
                        Double.parseDouble(stringIterator.next()),
                        Double.parseDouble(stringIterator.next())));
                arrayListPrimitives.add(plane);
                break;
            case "polygon":
                List<Point> listPointsPolygon = new ArrayList<Point>();
                while (stringIterator.hasNext()) {
                    Point point = new Point(Double.parseDouble(stringIterator
                            .next()), Double.parseDouble(stringIterator.next()),
                            Double.parseDouble(stringIterator.next()));
                    listPointsPolygon.add(point);
                }
                final Polygon polygon = new Polygon(
                        arrayListPrimitives.get(arrayListPrimitives.size() - 1),
                        listPointsPolygon);
                arrayListPrimitives.set(arrayListPrimitives.size() - 1, polygon);
                break;
            case "cylinder":
                arrayListPrimitives.add(new Cylinder(new Point(Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next())), Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next())));
                break;
            case "circle":
                arrayListPrimitives.add(new Circle(new Point(Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next())), new Vector(Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next())), Double
                        .parseDouble(stringIterator.next())));
                break;
            case "quadric":
                arrayListPrimitives.add(new Quadric(new Point(Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next())), Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next())));
                break;
            case "box":
                arrayListPrimitives.add(new Box(new Point(Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next())),new Point(Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next()), Double
                        .parseDouble(stringIterator.next()))));
                break;
            case "":
                break;
            default:
                parseSurface(current, stringIterator);
                break;
        }
        return amountLooker;
    }

    /**
     * Methode zum Parsen von Surfaceelementen.
     *
     * @param current        String
     * @param stringIterator Iterator<String>
     * @throws ClassNotFoundException nicht ambient diffuse oder specular
     */
    private void parseSurface(final String current,
                              final Iterator<String> stringIterator)
            throws ClassNotFoundException {
        assert current != null : "null current is impossible";
        assert stringIterator != null : "null stringIterator is impossible";
        switch (current) {
            case "ambient":
                if (!arrayListPrimitives.isEmpty()) {
                    final Surface surface1 = arrayListPrimitives.get(
                            arrayListPrimitives.size() - 1).getSurface();
                    surface1.set(Property.AmbientRatio,
                            Double.parseDouble(stringIterator.next()));
                }
                break;
            case "diffuse":
                if (!arrayListPrimitives.isEmpty()) {
                    final Surface surface2 = arrayListPrimitives.get(
                            arrayListPrimitives.size() - 1).getSurface();
                    surface2.set(Property.DiffuseRatio,
                            Double.parseDouble(stringIterator.next()));
                }
                break;
            case "specular":
                if (!arrayListPrimitives.isEmpty()) {
                    final Surface surface3 = arrayListPrimitives.get(
                            arrayListPrimitives.size() - 1).getSurface();
                    surface3.set(Property.SpecularRatio,
                            Double.parseDouble(stringIterator.next()));
                    surface3.set(Property.SpecularExponent,
                            Double.parseDouble(stringIterator.next()));
                }
                break;
            case "reflexion":
                if (!arrayListPrimitives.isEmpty()) {
                    arrayListPrimitives
                            .get(arrayListPrimitives.size() - 1)
                            .getSurface()
                            .set(Property.ReflexionRatio,
                                    Double.parseDouble(stringIterator.next()));
                }
                break;
            case "transmission":
                if (!arrayListPrimitives.isEmpty()) {
                    arrayListPrimitives
                            .get(arrayListPrimitives.size() - 1)
                            .getSurface()
                            .set(Property.TransmissionRatio,
                                    Double.parseDouble(stringIterator.next()));
                }
                break;
            case "colour":
                if (!arrayListPrimitives.isEmpty()) {
                    arrayListPrimitives
                            .get(arrayListPrimitives.size() - 1)
                            .getSurface()
                            .set(Property.Colour,
                                    Double.parseDouble(String.valueOf(Integer.parseInt(stringIterator.next(),16))));
                }
                break;
            default:
                throw new ClassNotFoundException(
                        "Nur looker, sphere, plane, light, ambient, diffuse, specular und reflexion erlaubt!");
        }
    }

    @Override
    public Optional<Intersection> findIntersection(final Ray ray) {
        assert ray != null : "null ray is impossible";
        final List<Intersection> list = new ArrayList<>();
        arrayListPrimitives.stream()
                .map(primitive -> primitive.intersections(ray))
                .forEach(intersection -> list.addAll(intersection));
        Collections.sort(list);
        return list.stream().findFirst();
    }

    @Override
    public Looker getLooker() {
        return looker;
    }

    @Override
    public Optional<Point> getLight() {
        return light.stream().findAny();
    }

    /**
     * Klasse erstellt einen iterierbaren String und einen passenden Iterator.
     *
     * @author M. Huebner, martin.huebner0@hm.edu
     * @author M. Streich, mstreich@hm.de
     * @version 2015-05-03
     */
    private static class IterableString implements Iterable<String> {
        /**
         * String auf dem itereiert wird.
         */
        private String strToIterateOver;

        /**
         * Konstruktor loescht Klammern.
         *
         * @param rowString String
         */
        public IterableString(final String rowString) {
            assert rowString != null : "null rowString is impossible";
            if (rowString.isEmpty() || rowString.charAt(0) == '#')
                strToIterateOver = "";
            else
                strToIterateOver = rowString.trim().replace("[", "")
                        .replace("]", "").replace("<", "").replace(">", "")
                        .toLowerCase();
        }

        @Override
        public StringIterator iterator() {
            return new StringIterator();
        }

        /**
         * Klasse erstellt einen Iterator fuer einen String.
         *
         * @author M. Huebner, martin.huebner0@hm.edu
         * @author M. Streich, mstreich@hm.de
         * @version 2015-05-03
         */
        private class StringIterator implements Iterator<String> {

            @Override
            public boolean hasNext() {
                return strToIterateOver.length() > 0;
            }

            @Override
            public String next() {

                String returnString = "";

                if (hasNext()) {

                    int posOfSpace = 0;
                    while (posOfSpace < strToIterateOver.length()
                            && !Character.isWhitespace(strToIterateOver
                            .charAt(posOfSpace))) {
                        posOfSpace++;
                    }

                    if (posOfSpace > 0) {
                        returnString = strToIterateOver
                                .substring(0, posOfSpace);
                        strToIterateOver = strToIterateOver.substring(
                                posOfSpace, strToIterateOver.length());
                    } else {
                        returnString = strToIterateOver;
                        strToIterateOver = "";
                    }
                }

                strToIterateOver = strToIterateOver.trim();
                return returnString;
            }

        }

    }

    @Override
    public String toString() {
        return "ScriptedScene [looker=" + looker + ", light=" + light
                + ", arrayListPrimitives=" + arrayListPrimitives + "]";
    }

}
