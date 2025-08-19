package com.ugnavigate.algorithms;

import com.ugnavigate.models.CampusMap;
import com.ugnavigate.models.Location;
import com.ugnavigate.services.TrafficService;

import java.util.*;

public class RoutingAlgorithm {
    private CampusMap campusMap;
    private TrafficService trafficService;

    public RoutingAlgorithm(CampusMap campusMap, TrafficService trafficService) {
        this.campusMap = campusMap;
        this.trafficService = trafficService;
    }

    // Dijkstra's Algorithm Implementation
    public List<String> findShortestPath(String startLocation, String endLocation) {
        if (startLocation == null || endLocation == null ||
                !campusMap.hasLocation(startLocation) || !campusMap.hasLocation(endLocation)) {
            return new ArrayList<>();
        }

        if (startLocation.equals(endLocation)) {
            return Arrays.asList(startLocation);
        }

        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(
                Comparator.comparingDouble(distances::get));
        Set<String> visited = new HashSet<>();

        // Initialize distances
        for (String location : campusMap.getLocations()) {
            distances.put(location, Double.POSITIVE_INFINITY);
        }
        distances.put(startLocation, 0.0);
        queue.offer(startLocation);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            if (current.equals(endLocation)) {
                break;
            }

            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            // Check all neighbors
            for (String neighbor : campusMap.getNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    double distance = campusMap.getDistance(current, neighbor);
                    if (distance < Double.POSITIVE_INFINITY) {
                        double newDistance = distances.get(current) + distance;
                        if (newDistance < distances.get(neighbor)) {
                            distances.put(neighbor, newDistance);
                            previous.put(neighbor, current);
                            queue.offer(neighbor);
                        }
                    }
                }
            }
        }

        // Reconstruct path
        return reconstructPath(previous, startLocation, endLocation);
    }

    // A* Search Algorithm Implementation
    public List<String> findOptimalPath(String startLocation, String endLocation) {
        if (startLocation == null || endLocation == null ||
                !campusMap.hasLocation(startLocation) || !campusMap.hasLocation(endLocation)) {
            return new ArrayList<>();
        }

        if (startLocation.equals(endLocation)) {
            return Arrays.asList(startLocation);
        }

        Map<String, Double> gScore = new HashMap<>();
        Map<String, Double> fScore = new HashMap<>();
        Map<String, String> cameFrom = new HashMap<>();
        PriorityQueue<String> openSet = new PriorityQueue<>(
                Comparator.comparingDouble(fScore::get));
        Set<String> closedSet = new HashSet<>();

        // Initialize scores
        for (String location : campusMap.getLocations()) {
            gScore.put(location, Double.POSITIVE_INFINITY);
            fScore.put(location, Double.POSITIVE_INFINITY);
        }

        gScore.put(startLocation, 0.0);
        fScore.put(startLocation, heuristic(startLocation, endLocation));
        openSet.offer(startLocation);

        while (!openSet.isEmpty()) {
            String current = openSet.poll();

            if (current.equals(endLocation)) {
                return reconstructPath(cameFrom, startLocation, endLocation);
            }

            closedSet.add(current);

            for (String neighbor : campusMap.getNeighbors(current)) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                double tentativeGScore = gScore.get(current) + campusMap.getDistance(current, neighbor);

                if (tentativeGScore < gScore.get(neighbor)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + heuristic(neighbor, endLocation));

                    if (!openSet.contains(neighbor)) {
                        openSet.offer(neighbor);
                    }
                }
            }
        }

        return new ArrayList<>();
    }

    // Floyd-Warshall Algorithm Implementation
    public Map<String, Map<String, Double>> findAllShortestPaths() {
        Map<String, Map<String, Double>> distances = new HashMap<>();
        Map<String, Map<String, String>> next = new HashMap<>();

        // Initialize distances and next matrix
        for (String i : campusMap.getLocations()) {
            distances.put(i, new HashMap<>());
            next.put(i, new HashMap<>());
            for (String j : campusMap.getLocations()) {
                if (i.equals(j)) {
                    distances.get(i).put(j, 0.0);
                } else {
                    double distance = campusMap.getDistance(i, j);
                    distances.get(i).put(j, distance);
                    if (distance < Double.POSITIVE_INFINITY) {
                        next.get(i).put(j, j);
                    }
                }
            }
        }

        // Floyd-Warshall algorithm
        for (String k : campusMap.getLocations()) {
            for (String i : campusMap.getLocations()) {
                for (String j : campusMap.getLocations()) {
                    double ik = distances.get(i).get(k);
                    double kj = distances.get(k).get(j);
                    double ij = distances.get(i).get(j);

                    if (ik + kj < ij) {
                        distances.get(i).put(j, ik + kj);
                        next.get(i).put(j, next.get(i).get(k));
                    }
                }
            }
        }

        return distances;
    }

    // Find multiple routes based on landmarks
    public List<List<String>> findRoutesWithLandmarks(String startLocation, String endLocation, String landmark) {
        List<List<String>> routes = new ArrayList<>();

        // Find locations near the landmark
        List<Location> landmarkLocations = campusMap.getLocationsByLandmark(landmark);

        if (landmarkLocations.isEmpty()) {
            // If no landmark found, return shortest path
            List<String> shortestPath = findShortestPath(startLocation, endLocation);
            if (!shortestPath.isEmpty()) {
                routes.add(shortestPath);
            }
            return routes;
        }

        // Find routes through each landmark location
        for (Location landmarkLocation : landmarkLocations) {
            String landmarkName = landmarkLocation.getName();

            // Path from start to landmark
            List<String> pathToLandmark = findShortestPath(startLocation, landmarkName);
            if (pathToLandmark.isEmpty())
                continue;

            // Path from landmark to end
            List<String> pathFromLandmark = findShortestPath(landmarkName, endLocation);
            if (pathFromLandmark.isEmpty())
                continue;

            // Combine paths (avoid duplicate landmark)
            List<String> combinedPath = new ArrayList<>(pathToLandmark);
            if (!pathFromLandmark.isEmpty() && !pathFromLandmark.get(0).equals(landmarkName)) {
                combinedPath.addAll(pathFromLandmark);
            } else if (pathFromLandmark.size() > 1) {
                combinedPath.addAll(pathFromLandmark.subList(1, pathFromLandmark.size()));
            }

            routes.add(combinedPath);
        }

        // Sort routes by total distance
        routes.sort(Comparator.comparingDouble(this::calculateTotalDistance));

        return routes;
    }

    // Find top 3 routes
    public List<List<String>> findTopRoutes(String startLocation, String endLocation, int count) {
        List<List<String>> allRoutes = new ArrayList<>();

        // Add shortest path
        List<String> shortestPath = findShortestPath(startLocation, endLocation);
        if (!shortestPath.isEmpty()) {
            allRoutes.add(shortestPath);
        }

        // Add optimal path (A*)
        List<String> optimalPath = findOptimalPath(startLocation, endLocation);
        if (!optimalPath.isEmpty() && !optimalPath.equals(shortestPath)) {
            allRoutes.add(optimalPath);
        }

        // Add routes through different landmarks
        List<String> landmarks = campusMap.getAllLandmarks();
        for (String landmark : landmarks) {
            List<List<String>> landmarkRoutes = findRoutesWithLandmarks(startLocation, endLocation, landmark);
            allRoutes.addAll(landmarkRoutes);
        }

        // Remove duplicates and sort by distance
        allRoutes = removeDuplicateRoutes(allRoutes);
        allRoutes.sort(Comparator.comparingDouble(this::calculateTotalDistance));

        return allRoutes.subList(0, Math.min(count, allRoutes.size()));
    }

    public long calculateArrivalTime(String startLocation, String endLocation) {
        List<String> path = findShortestPath(startLocation, endLocation);
        if (path.isEmpty()) {
            return 0;
        }

        double totalDistance = calculateTotalDistance(path);

        // Assume average speed of 5 km/h for walking
        double averageSpeed = 5.0; // km/h
        long baseTime = Math.round((totalDistance / averageSpeed) * 60); // Convert to minutes

        // Adjust for traffic conditions
        String trafficCondition = trafficService.assessTrafficConditions(startLocation);
        return trafficService.adjustRouteForTraffic((int) baseTime, trafficCondition);
    }

    public long calculateArrivalTimeForRoute(List<String> route) {
        if (route.isEmpty()) {
            return 0;
        }

        double totalDistance = calculateTotalDistance(route);

        // Assume average speed of 5 km/h for walking
        double averageSpeed = 5.0; // km/h
        long baseTime = Math.round((totalDistance / averageSpeed) * 60); // Convert to minutes

        // Adjust for traffic conditions
        String trafficCondition = trafficService.assessTrafficConditions(route.get(0));
        return trafficService.adjustRouteForTraffic((int) baseTime, trafficCondition);
    }

    // Helper methods
    private double heuristic(String from, String to) {
        Location fromLoc = campusMap.getLocation(from);
        Location toLoc = campusMap.getLocation(to);

        if (fromLoc != null && toLoc != null) {
            // Calculate Euclidean distance as heuristic
            double latDiff = fromLoc.getLatitude() - toLoc.getLatitude();
            double lonDiff = fromLoc.getLongitude() - toLoc.getLongitude();
            return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);
        }

        return 0.0;
    }

    private List<String> reconstructPath(Map<String, String> previous, String start, String end) {
        List<String> path = new ArrayList<>();
        String current = end;

        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }

        return path.isEmpty() || !path.get(0).equals(start) ? new ArrayList<>() : path;
    }

    private double calculateTotalDistance(List<String> path) {
        if (path.size() < 2)
            return 0.0;

        double totalDistance = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            totalDistance += campusMap.getDistance(path.get(i), path.get(i + 1));
        }
        return totalDistance;
    }

    private List<List<String>> removeDuplicateRoutes(List<List<String>> routes) {
        Set<String> seen = new HashSet<>();
        List<List<String>> uniqueRoutes = new ArrayList<>();

        for (List<String> route : routes) {
            String routeString = String.join("->", route);
            if (!seen.contains(routeString)) {
                seen.add(routeString);
                uniqueRoutes.add(route);
            }
        }

        return uniqueRoutes;
    }
}