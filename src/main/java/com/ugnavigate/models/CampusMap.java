package com.ugnavigate.models;

import java.util.*;
import java.util.stream.Collectors;

public class CampusMap {
    private Map<String, Location> locations;
    private Map<String, Map<String, Double>> distances;

    public CampusMap() {
        locations = new HashMap<>();
        distances = new HashMap<>();
    }

    public void addLocation(Location location) {
        locations.put(location.getName(), location);
        distances.put(location.getName(), new HashMap<>());
    }

    public void addLocation(String name, Map<String, Double> distances) {
        Location location = new Location(name, 0.0, 0.0);
        locations.put(name, location);
        this.distances.put(name, new HashMap<>(distances));

        // Add reverse connections for bidirectional graph
        for (Map.Entry<String, Double> entry : distances.entrySet()) {
            String toLocation = entry.getKey();
            Double distance = entry.getValue();
            if (!this.distances.containsKey(toLocation)) {
                this.distances.put(toLocation, new HashMap<>());
            }
            this.distances.get(toLocation).put(name, distance);
        }
    }

    public void addConnection(String from, String to, double distance) {
        if (!distances.containsKey(from)) {
            distances.put(from, new HashMap<>());
        }
        if (!distances.containsKey(to)) {
            distances.put(to, new HashMap<>());
        }
        distances.get(from).put(to, distance);
        distances.get(to).put(from, distance); // Bidirectional
    }

    public double getDistance(String from, String to) {
        if (distances.containsKey(from) && distances.get(from).containsKey(to)) {
            return distances.get(from).get(to);
        }
        return Double.POSITIVE_INFINITY;
    }

    public Set<String> getLocations() {
        return new HashSet<>(locations.keySet());
    }

    public Location getLocation(String name) {
        return locations.get(name);
    }

    public List<Location> getLocationsByLandmark(String landmark) {
        return locations.values().stream()
                .filter(location -> location.isNearLandmark(landmark))
                .collect(Collectors.toList());
    }

    public List<Location> getLocationsByCategory(String category) {
        return locations.values().stream()
                .filter(location -> location.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public Set<String> getNeighbors(String location) {
        if (distances.containsKey(location)) {
            return distances.get(location).keySet();
        }
        return new HashSet<>();
    }

    public boolean hasLocation(String name) {
        return locations.containsKey(name);
    }

    public int getLocationCount() {
        return locations.size();
    }

    public List<String> getAllLandmarks() {
        Set<String> allLandmarks = new HashSet<>();
        for (Location location : locations.values()) {
            allLandmarks.addAll(location.getLandmarks());
        }
        return new ArrayList<>(allLandmarks);
    }

    public Map<String, List<Location>> getLocationsByLandmark() {
        Map<String, List<Location>> landmarkMap = new HashMap<>();
        for (Location location : locations.values()) {
            for (String landmark : location.getLandmarks()) {
                landmarkMap.computeIfAbsent(landmark, k -> new ArrayList<>()).add(location);
            }
        }
        return landmarkMap;
    }
}