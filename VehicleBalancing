
package io;

import java.util.*;

public class DijkstraAlgorithm {
    private final Graph graph;
    private final Map<String, Double> distances;
    private final Map<String, String> predecessors;
    private final Set<String> visited;
    private final PriorityQueue<Vertex> priorityQueue;

    public DijkstraAlgorithm(Graph graph) {
        this.graph = graph;
        this.distances = new HashMap<>();
        this.predecessors = new HashMap<>();
        this.visited = new HashSet<>();
        this.priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(Vertex::getDistance));
    }

    public List<String> findShortestPath(List<String> locations, String start) {
        distances.clear();
        predecessors.clear();
        visited.clear();

        for (String vertex : graph.getVertices()) {
            distances.put(vertex, Double.POSITIVE_INFINITY);
        }
        distances.put(start, 0.0);

        priorityQueue.add(new Vertex(start, 0.0));

        while (!priorityQueue.isEmpty()) {
            String currentVertex = priorityQueue.poll().getName();
            visited.add(currentVertex);

            if (currentVertex.equals(locations.get(locations.size() - 1))) break;

            for (Map.Entry<String, Double> neighbor : graph.getAdjVertices(currentVertex).entrySet()) {
                if (!visited.contains(neighbor.getKey())) {
                    double newDist = distances.get(currentVertex) + neighbor.getValue();
                    if (newDist < distances.get(neighbor.getKey())) {
                        distances.put(neighbor.getKey(), newDist);
                        predecessors.put(neighbor.getKey(), currentVertex);
                        priorityQueue.add(new Vertex(neighbor.getKey(), newDist));
                    }
                }
            }
        }

        List<String> path = new ArrayList<>();
        String current = locations.get(locations.size() - 1);
        while (current != null) {
            path.add(current);
            current = predecessors.get(current);
        }
        Collections.reverse(path);
        return path;
    }

    public double calculateTotalDistance(List<String> path) {
        double totalDistance = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            String vertex1 = path.get(i);
            String vertex2 = path.get(i + 1);
            totalDistance += graph.getAdjVertices(vertex1).get(vertex2);
        }
        return totalDistance;
    }

    private static class Vertex {
        private final String name;
        private final double distance;

        public Vertex(String name, double distance) {
            this.name = name;
            this.distance = distance;
        }

        public String getName() {
            return name;
        }

        public double getDistance() {
            return distance;
        }
    }
}

// File: src/io/VehicleBalancing.java

package io;

import java.util.*;

public class VehicleBalancing {
    public List<List<String>> balanceOrders(List<String> locations, int numVehicles, List<Double> orderWeights, double[] vehicleCapacities, Graph graph, Map<String, Coordinates> cityCoordinates, String startLocation) {
        List<List<String>> balancedOrders = new ArrayList<>();

        for (int i = 0; i < numVehicles; i++) {
            balancedOrders.add(new ArrayList<>());
        }

        for (int i = 0; i < locations.size(); i++) {
            double currentWeight = orderWeights.get(i);
            int assignedVehicle = getLeastLoadedVehicle(vehicleCapacities, balancedOrders, currentWeight);
            if (assignedVehicle != -1) {
                balancedOrders.get(assignedVehicle).add(locations.get(i));
            }
            // else: handle overflow/error as needed
        }

        return balancedOrders;
    }

    private int getLeastLoadedVehicle(double[] vehicleCapacities, List<List<String>> balancedOrders, double currentWeight) {
        int leastLoaded = -1;
        double minLoad = Double.MAX_VALUE;

        for (int i = 0; i < balancedOrders.size(); i++) {
            double currentLoad = getTotalWeight(balancedOrders.get(i));
            if (currentLoad + currentWeight <= vehicleCapacities[i] && currentLoad < minLoad) {
                leastLoaded = i;
                minLoad = currentLoad;
            }
        }
        return leastLoaded;
    }

    private double getTotalWeight(List<String> orders) {
        return orders.size(); // Assuming 1 per order; adapt as needed
    }
}
