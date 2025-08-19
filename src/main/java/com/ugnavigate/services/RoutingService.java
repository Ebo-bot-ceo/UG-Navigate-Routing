package com.ugnavigate.services;

import com.ugnavigate.algorithms.RoutingAlgorithm;
import com.ugnavigate.models.CampusMap;
import com.ugnavigate.models.Location;
import com.ugnavigate.models.Route;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Objects;

public class RoutingService {
    private RoutingAlgorithm routingAlgorithm;
    private CampusMap campusMap;
    private TrafficService trafficService;

    public RoutingService(CampusMap campusMap, TrafficService trafficService) {
        this.campusMap = campusMap;
        this.trafficService = trafficService;
        this.routingAlgorithm = new RoutingAlgorithm(campusMap, trafficService);
    }

    /**
     * Find the shortest route using Dijkstra's algorithm
     */
    public Route findShortestRoute(String startLocation, String endLocation) {
        List<String> path = routingAlgorithm.findShortestPath(startLocation, endLocation);
        if (path.isEmpty()) {
            return null;
        }

        double totalDistance = calculateTotalDistance(path);
        long estimatedTime = routingAlgorithm.calculateArrivalTimeForRoute(path);
        List<String> landmarks = extractLandmarks(path);

        Route route = new Route(path, totalDistance, estimatedTime, "Dijkstra's Algorithm", landmarks);
        route.setDescription("Shortest distance route using Dijkstra's algorithm");
        return route;
    }

    /**
     * Find the optimal route using A* algorithm
     */
    public Route findOptimalRoute(String startLocation, String endLocation) {
        List<String> path = routingAlgorithm.findOptimalPath(startLocation, endLocation);
        if (path.isEmpty()) {
            return null;
        }

        double totalDistance = calculateTotalDistance(path);
        long estimatedTime = routingAlgorithm.calculateArrivalTimeForRoute(path);
        List<String> landmarks = extractLandmarks(path);

        Route route = new Route(path, totalDistance, estimatedTime, "A* Algorithm", landmarks);
        route.setDescription("Optimal route using A* search algorithm");
        return route;
    }

    /**
     * Find routes that pass through specific landmarks
     */
    public List<Route> findRoutesWithLandmarks(String startLocation, String endLocation, String landmark) {
        List<List<String>> paths = routingAlgorithm.findRoutesWithLandmarks(startLocation, endLocation, landmark);
        List<Route> routes = new ArrayList<>();

        for (List<String> path : paths) {
            if (!path.isEmpty()) {
                double totalDistance = calculateTotalDistance(path);
                long estimatedTime = routingAlgorithm.calculateArrivalTimeForRoute(path);
                List<String> landmarks = extractLandmarks(path);

                Route route = new Route(path, totalDistance, estimatedTime, "Landmark-based", landmarks);
                route.setDescription("Route passing through " + landmark);
                routes.add(route);
            }
        }

        return routes;
    }

    /**
     * Find top N routes sorted by distance
     */
    public List<Route> findTopRoutes(String startLocation, String endLocation, int count) {
        List<List<String>> paths = routingAlgorithm.findTopRoutes(startLocation, endLocation, count);
        List<Route> routes = new ArrayList<>();

        for (List<String> path : paths) {
            if (!path.isEmpty()) {
                double totalDistance = calculateTotalDistance(path);
                long estimatedTime = routingAlgorithm.calculateArrivalTimeForRoute(path);
                List<String> landmarks = extractLandmarks(path);

                String algorithm = determineAlgorithm(path, startLocation, endLocation);
                Route route = new Route(path, totalDistance, estimatedTime, algorithm, landmarks);
                route.setDescription("Alternative route option");
                routes.add(route);
            }
        }

        return routes;
    }

    /**
     * Find routes by category (academic, administrative, facility, etc.)
     */
    public List<Route> findRoutesByCategory(String startLocation, String endLocation, String category) {
        List<Location> categoryLocations = campusMap.getLocationsByCategory(category);
        List<Route> routes = new ArrayList<>();

        for (Location location : categoryLocations) {
            List<String> pathToLocation = routingAlgorithm.findShortestPath(startLocation, location.getName());
            List<String> pathFromLocation = routingAlgorithm.findShortestPath(location.getName(), endLocation);

            if (!pathToLocation.isEmpty() && !pathFromLocation.isEmpty()) {
                // Combine paths
                List<String> combinedPath = new ArrayList<>(pathToLocation);
                if (!pathFromLocation.isEmpty() && !pathFromLocation.get(0).equals(location.getName())) {
                    combinedPath.addAll(pathFromLocation);
                } else if (pathFromLocation.size() > 1) {
                    combinedPath.addAll(pathFromLocation.subList(1, pathFromLocation.size()));
                }

                double totalDistance = calculateTotalDistance(combinedPath);
                long estimatedTime = routingAlgorithm.calculateArrivalTimeForRoute(combinedPath);
                List<String> landmarks = extractLandmarks(combinedPath);

                Route route = new Route(combinedPath, totalDistance, estimatedTime, "Category-based", landmarks);
                route.setDescription("Route passing through " + category + " location: " + location.getName());
                routes.add(route);
            }
        }

        // Sort by distance and return top 3
        routes.sort(Comparator.comparingDouble(Route::getTotalDistance));
        return routes.subList(0, Math.min(3, routes.size()));
    }

    /**
     * Search for routes based on user input (landmarks, categories, etc.)
     */
    public List<Route> searchRoutes(String startLocation, String endLocation, String searchTerm) {
        List<Route> routes = new ArrayList<>();

        // Check if search term is a landmark
        List<Route> landmarkRoutes = findRoutesWithLandmarks(startLocation, endLocation, searchTerm);
        routes.addAll(landmarkRoutes);

        // Check if search term is a category
        List<Route> categoryRoutes = findRoutesByCategory(startLocation, endLocation, searchTerm);
        routes.addAll(categoryRoutes);

        // Check if search term matches any location name
        List<Location> matchingLocations = campusMap.getLocations().stream()
                .map(campusMap::getLocation)
                .filter(Objects::nonNull)
                .filter(location -> location.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());

        for (Location location : matchingLocations) {
            List<String> pathToLocation = routingAlgorithm.findShortestPath(startLocation, location.getName());
            List<String> pathFromLocation = routingAlgorithm.findShortestPath(location.getName(), endLocation);

            if (!pathToLocation.isEmpty() && !pathFromLocation.isEmpty()) {
                List<String> combinedPath = new ArrayList<>(pathToLocation);
                if (!pathFromLocation.isEmpty() && !pathFromLocation.get(0).equals(location.getName())) {
                    combinedPath.addAll(pathFromLocation);
                } else if (pathFromLocation.size() > 1) {
                    combinedPath.addAll(pathFromLocation.subList(1, pathFromLocation.size()));
                }

                double totalDistance = calculateTotalDistance(combinedPath);
                long estimatedTime = routingAlgorithm.calculateArrivalTimeForRoute(combinedPath);
                List<String> landmarks = extractLandmarks(combinedPath);

                Route route = new Route(combinedPath, totalDistance, estimatedTime, "Search-based", landmarks);
                route.setDescription("Route passing through " + location.getName());
                routes.add(route);
            }
        }

        // Remove duplicates and sort by distance
        routes = removeDuplicateRoutes(routes);
        routes.sort(Comparator.comparingDouble(Route::getTotalDistance));

        return routes.subList(0, Math.min(5, routes.size()));
    }

    /**
     * Get all available landmarks
     */
    public List<String> getAllLandmarks() {
        return campusMap.getAllLandmarks();
    }

    /**
     * Get all available categories
     */
    public List<String> getAllCategories() {
        return campusMap.getLocations().stream()
                .map(campusMap::getLocation)
                .filter(Objects::nonNull)
                .map(Location::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Get all available locations
     */
    public List<String> getAllLocations() {
        return new ArrayList<>(campusMap.getLocations());
    }

    // Helper methods
    private double calculateTotalDistance(List<String> path) {
        if (path.size() < 2)
            return 0.0;

        double totalDistance = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            totalDistance += campusMap.getDistance(path.get(i), path.get(i + 1));
        }
        return totalDistance;
    }

    private List<String> extractLandmarks(List<String> path) {
        List<String> landmarks = new ArrayList<>();
        for (String locationName : path) {
            Location location = campusMap.getLocation(locationName);
            if (location != null && !location.getLandmarks().isEmpty()) {
                landmarks.addAll(location.getLandmarks());
            }
        }
        return landmarks.stream().distinct().collect(Collectors.toList());
    }

    private String determineAlgorithm(List<String> path, String start, String end) {
        List<String> shortestPath = routingAlgorithm.findShortestPath(start, end);
        List<String> optimalPath = routingAlgorithm.findOptimalPath(start, end);

        if (path.equals(shortestPath)) {
            return "Dijkstra's Algorithm";
        } else if (path.equals(optimalPath)) {
            return "A* Algorithm";
        } else {
            return "Alternative Route";
        }
    }

    private List<Route> removeDuplicateRoutes(List<Route> routes) {
        Set<String> seen = new HashSet<>();
        List<Route> uniqueRoutes = new ArrayList<>();

        for (Route route : routes) {
            String routeString = String.join("->", route.getPath());
            if (!seen.contains(routeString)) {
                seen.add(routeString);
                uniqueRoutes.add(route);
            }
        }

        return uniqueRoutes;
    }
}