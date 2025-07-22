# Route-Optimization

Route Optimization Dashboard
A Java Swing application for optimizing vehicle routing between cities, especially suited for logistics and delivery planning across India.

**Features**
Load a dataset of Indian cities (CSV file with city names and coordinates)

Enter number, type, and capacity of vehicles

Assign delivery order weights to destinations

Run route optimization using Dijkstra’s algorithm

Get per-vehicle optimized delivery routes, stepwise paths, distances & estimated times

Google Maps links for visualizing optimal routes

Modern, easy-to-use Java Swing dashboard


**Setup**
**Clone the repository:**
bash
git clone https://github.com/YOUR_USERNAME/RouteOptimizationDashboard.git
cd RouteOptimizationDashboard
(Optional) Open in your favorite IDE (IntelliJ IDEA, VS Code, Eclipse, NetBeans, etc.)

**Compile & Run:**
From command line (in the project root):

bash
javac -d out src/io/*.java
java -cp out io.DAA_GUI
Or use your IDE's build/run option.

Folder Structure
text
RouteOptimizationDashboard/
  ├── src/io/
  │     ├── DAA_GUI.java
  │     ├── CityLoader.java
  │     ├── Graph.java
  │     ├── Coordinates.java
  │     ├── DijkstraAlgorithm.java
  │     └── VehicleBalancing.java
  ├── .gitignore
  ├── README.md
  └── indian_cities.csv
Usage

**Fill in:**

Number of Vehicles

Vehicle Types (comma-separated, e.g. Truck,Van)

Vehicle Capacities (comma-separated, e.g. 1000,500)

Start Location (choose from dropdown)

End Locations (list, comma-separated)

Order Weights (comma-separated, in order of locations)

Click Optimize Routes

Results, optimal routes, and Google Maps links will display in the output area.

