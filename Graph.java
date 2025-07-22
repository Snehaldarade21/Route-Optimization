
package io;

import java.util.*;

public class Graph {
    private final Map<String, Map<String, Double>> adjVertices = new HashMap<>();

    public void addVertex(String vertex) {
        adjVertices.putIfAbsent(vertex, new HashMap<>());
    }

    public void addEdge(String vertex1, String vertex2, double weight) {
        adjVertices.get(vertex1).put(vertex2, weight);
        adjVertices.get(vertex2).put(vertex1, weight);
    }

    public Map<String, Double> getAdjVertices(String vertex) {
        return adjVertices.get(vertex);
    }

    public Set<String> getVertices() {
        return adjVertices.keySet();
    }
}

// File: src/io/Coordinates.java

package io;

public class Coordinates {
    double latitude, longitude;

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
