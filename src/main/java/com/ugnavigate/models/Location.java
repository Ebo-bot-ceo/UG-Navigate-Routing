package com.ugnavigate.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Location {
    private String name;
    private double latitude;
    private double longitude;
    private Map<String, Double> connections;
    private Set<String> landmarks;
    private String category; // e.g., "academic", "administrative", "facility", "landmark"
    private String description;

    public Location(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.connections = new HashMap<>();
        this.landmarks = new java.util.HashSet<>();
        this.category = "general";
        this.description = "";
    }

    public Location(String name, double latitude, double longitude, String category, String description) {
        this(name, latitude, longitude);
        this.category = category;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void addConnection(String destination, double distance) {
        connections.put(destination, distance);
    }

    public Map<String, Double> getConnections() {
        return new HashMap<>(connections);
    }

    public double getDistanceTo(String destination) {
        return connections.getOrDefault(destination, Double.POSITIVE_INFINITY);
    }

    public Set<String> getLandmarks() {
        return new java.util.HashSet<>(landmarks);
    }

    public void addLandmark(String landmark) {
        landmarks.add(landmark);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean hasLandmark(String landmark) {
        return landmarks.contains(landmark);
    }

    public boolean isNearLandmark(String landmark) {
        return landmarks.contains(landmark) || name.toLowerCase().contains(landmark.toLowerCase());
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", category='" + category + '\'' +
                ", landmarks=" + landmarks +
                '}';
    }
}
