
package io;

import java.io.*;
import java.util.*;

public class CityLoader {
    /**
     * Loads cities from the dataset into the cityCoordinates map and graph, and returns the city list.
     */
    public static List<String> loadCitiesFromDataset(String filename, Map<String, Coordinates> cityCoordinates, Graph graph) throws IOException {
        List<String> cities = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String cityName = parts[0].trim();
                    double latitude = Double.parseDouble(parts[1].trim());
                    double longitude = Double.parseDouble(parts[2].trim());
                    cities.add(cityName);
                    cityCoordinates.put(cityName, new Coordinates(latitude, longitude));
                    graph.addVertex(cityName);
                }
            }
        }
        return cities;
    }
}
