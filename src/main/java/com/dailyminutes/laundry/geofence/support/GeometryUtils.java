/**
 * @author Somendra Datta <sodatta@gmail.com>
 * @version 28/08/25
 */
package com.dailyminutes.laundry.geofence.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * Utility for parsing Tookan polygon strings and testing serviceability.
 * <p>
 * polygonCoordinates is stored as a String like:
 * "[[[72.901607,19.117849],[72.909593,19.120563],...]]"
 */
public class GeometryUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Parse polygon string (JSON array) into list of [lon, lat] pairs.
     * Returns the "outer ring" (first element).
     *
     * @param polygonCoordinates the polygon coordinates
     * @return the list
     */
    public static List<List<Double>> parseOuterRingFromString(String polygonCoordinates) {
        try {
            // type is List<List<List<Double>>> because it's [[[lon,lat],...]]
            List<List<List<Double>>> parsed = mapper.readValue(
                    polygonCoordinates,
                    new TypeReference<>() {}
            );
            return parsed.get(0); // outer ring
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid polygonCoordinates: " + polygonCoordinates, e);
        }
    }

    /**
     * Ray-casting algorithm for point in polygon.
     *
     * @param x    longitude
     * @param y    latitude
     * @param ring list of [lon, lat] pairs
     * @return the boolean
     */
    public static boolean pointInRing(double x, double y, List<List<Double>> ring) {
        boolean inside = false;
        for (int i = 0, j = ring.size() - 1; i < ring.size(); j = i++) {
            double xi = ring.get(i).get(0), yi = ring.get(i).get(1);
            double xj = ring.get(j).get(0), yj = ring.get(j).get(1);

            boolean intersect = ((yi > y) != (yj > y)) &&
                    (x < (xj - xi) * (y - yi) / (yj - yi + 0.0) + xi);
            if (intersect) inside = !inside;
        }
        return inside;
    }
}
