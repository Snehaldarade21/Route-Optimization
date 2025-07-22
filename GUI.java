// File: src/io/DAA_GUI.java

package io;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.*;

public class DAA_GUI extends JFrame {
    private static final double VEHICLE_SPEED = 60.0;

    private JButton optimizeButton, loadButton;
    private JComboBox<String> startLocationDropdown;
    private JTextArea endLocationsArea, outputArea;
    private JTextField vehicleTypesField, vehicleCapacitiesField, numVehiclesField, orderWeightsField;
    private Graph graph;
    private Map<String, Coordinates> cityCoordinates;
    private List<String> allLocations;

    public DAA_GUI() {
        setTitle("Route Optimization Dashboard");
        setSize(1200, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        initializeComponents(mainPanel);
        add(mainPanel);
    }

    private void initializeComponents(JPanel panel) {
        JPanel sidebar = new JPanel(new GridLayout(5, 1, 10, 10));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(new Color(45, 52, 54));

        JPanel displayPanel = new JPanel(new BorderLayout());

        loadButton = new JButton("Load Dataset");
        loadButton.setBackground(new Color(99, 110, 114));
        loadButton.setForeground(Color.WHITE);

        optimizeButton = new JButton("Optimize Routes");
        optimizeButton.setBackground(new Color(99, 110, 114));
        optimizeButton.setForeground(Color.WHITE);

        sidebar.add(loadButton);
        sidebar.add(optimizeButton);

        JPanel inputPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        vehicleTypesField = new JTextField();
        vehicleCapacitiesField = new JTextField();
        numVehiclesField = new JTextField();
        startLocationDropdown = new JComboBox<>();
        endLocationsArea = new JTextArea(3, 20);
        orderWeightsField = new JTextField();

        startLocationDropdown.setPreferredSize(new Dimension(200, 30));

        inputPanel.add(new JLabel("Number of Vehicles:"));
        inputPanel.add(numVehiclesField);
        inputPanel.add(new JLabel("Vehicle Types:"));
        inputPanel.add(vehicleTypesField);
        inputPanel.add(new JLabel("Vehicle Capacities:"));
        inputPanel.add(vehicleCapacitiesField);
        inputPanel.add(new JLabel("Start Location:"));
        inputPanel.add(startLocationDropdown);
        inputPanel.add(new JLabel("End Locations (comma-separated):"));
        inputPanel.add(new JScrollPane(endLocationsArea));
        inputPanel.add(new JLabel("Order Weights (comma-separated):"));
        inputPanel.add(orderWeightsField);

        outputArea = new JTextArea(30, 50);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(BorderFactory.createTitledBorder("Optimization Results"));

        displayPanel.add(inputPanel, BorderLayout.NORTH);
        displayPanel.add(outputScroll, BorderLayout.CENTER);

        panel.add(sidebar, BorderLayout.WEST);
        panel.add(displayPanel, BorderLayout.CENTER);

        loadButton.addActionListener(this::loadCities);
        optimizeButton.addActionListener(this::optimizeRoute);
    }

    private void loadCities(ActionEvent e) {
        try {
            graph = new Graph();
            cityCoordinates = new HashMap<>();
            allLocations = CityLoader.loadCitiesFromDataset("C:\\Users\\admin\\Downloads\\indian_cities.csv", cityCoordinates, graph);

            startLocationDropdown.removeAllItems();
            for (String location : allLocations) {
                startLocationDropdown.addItem(location);
            }

            JOptionPane.showMessageDialog(this, "Cities dataset loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading cities dataset.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void optimizeRoute(ActionEvent e) {
        try {
            int numVehicles = Integer.parseInt(numVehiclesField.getText().trim());
            String[] vehicleTypes = vehicleTypesField.getText().split(",");
            double[] vehicleCapacities = Arrays.stream(vehicleCapacitiesField.getText().split(",")).mapToDouble(Double::parseDouble).toArray();
            String startLocation = (String) startLocationDropdown.getSelectedItem();
            String[] endLocations = endLocationsArea.getText().split(",");
            List<Double> orderWeights = parseOrderWeights(orderWeightsField.getText().trim());

            VehicleBalancing vehicleBalancing = new VehicleBalancing();
            List<List<String>> balancedOrders = vehicleBalancing.balanceOrders(Arrays.asList(endLocations), numVehicles, orderWeights, vehicleCapacities, graph, cityCoordinates, startLocation);

            DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
            StringBuilder output = new StringBuilder("Final Optimized Routes Summary:\n");

            for (int i = 0; i < balancedOrders.size(); i++) {
                List<String> vehicleRoute = dijkstra.findShortestPath(balancedOrders.get(i), startLocation);
                double routeDistance = dijkstra.calculateTotalDistance(vehicleRoute);
                double totalWeight = calculateTotalWeight(balancedOrders.get(i), orderWeights);
                double estimatedTime = routeDistance / VEHICLE_SPEED;

                output.append("\nVehicle ").append(i + 1).append(" handles the following orders: ").append(balancedOrders.get(i)).append("\n");
                output.append("Order paths for Vehicle ").append(i + 1).append(":\n");

                for (String order : balancedOrders.get(i)) {
                    String path = dijkstra.findShortestPath(Arrays.asList(startLocation, order), startLocation).toString();
                    double distance = dijkstra.calculateTotalDistance(Arrays.asList(startLocation, order));
                    output.append("Path to ").append(order).append(": ").append(path).append(" (Distance: ").append(distance).append(" km)\n");
                }

                output.append("Total distance for Vehicle ").append(i + 1).append(": ").append(routeDistance).append(" km\n");
                output.append("Estimated total time for Vehicle ").append(i + 1).append(": ").append(estimatedTime).append(" hours\n");
                output.append("Google Maps Link to visualize the path for Vehicle ").append(i + 1).append(": ").append(getGoogleMapsLink(vehicleRoute)).append("\n");
            }

            outputArea.setText(output.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error optimizing route: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getGoogleMapsLink(List<String> route) {
        StringBuilder link = new StringBuilder("https://www.google.com/maps/dir/");
        for (String city : route) {
            Coordinates coords = cityCoordinates.get(city);
            link.append(coords.latitude).append(",").append(coords.longitude).append("/");
        }
        return link.toString();
    }

    private List<Double> parseOrderWeights(String orderWeights) {
        List<Double> weights = new ArrayList<>();
        for (String weight : orderWeights.split(",")) {
            weights.add(Double.parseDouble(weight.trim()));
        }
        return weights;
    }

    private double calculateTotalWeight(List<String> orders, List<Double> weights) {
        double totalWeight = 0.0;
        for (String order : orders) {
            int index = Arrays.asList(endLocationsArea.getText().split(",")).indexOf(order);
            if (index >= 0 && index < weights.size()) {
                totalWeight += weights.get(index);
            }
        }
        return totalWeight;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DAA_GUI frame = new DAA_GUI();
            frame.setVisible(true);
        });
    }
}
