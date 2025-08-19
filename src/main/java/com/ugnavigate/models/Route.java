package com.ugnavigate.models;

import java.util.List;
import java.util.Objects;

public class Route {
    private List<String> path;
    private double totalDistance;
    private long estimatedTime;
    private List<String> landmarks;
    private String algorithm;
    private String description;

    public Route(List<String> path, double totalDistance, long estimatedTime, String algorithm) {
        this.path = path;
        this.totalDistance = totalDistance;
        this.estimatedTime = estimatedTime;
        this.algorithm = algorithm;
        this.landmarks = new java.util.ArrayList<>();
        this.description = "";
    }

    public Route(List<String> path, double totalDistance, long estimatedTime, String algorithm,
            List<String> landmarks) {
        this(path, totalDistance, estimatedTime, algorithm);
        this.landmarks = landmarks;
    }

    public List<String> getPath() {
        return new java.util.ArrayList<>(path);
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public long getEstimatedTime() {
        return estimatedTime;
    }

    public List<String> getLandmarks() {
        return new java.util.ArrayList<>(landmarks);
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addLandmark(String landmark) {
        if (!landmarks.contains(landmark)) {
            landmarks.add(landmark);
        }
    }

    public int getPathLength() {
        return path.size();
    }

    public String getStartLocation() {
        return path.isEmpty() ? "" : path.get(0);
    }

    public String getEndLocation() {
        return path.isEmpty() ? "" : path.get(path.size() - 1);
    }

    public boolean containsLandmark(String landmark) {
        return landmarks.contains(landmark);
    }

    public String getFormattedPath() {
        return String.join(" → ", path);
    }

    public String getFormattedTime() {
        long hours = estimatedTime / 60;
        long minutes = estimatedTime % 60;
        if (hours > 0) {
            return String.format("%d hr %d min", hours, minutes);
        } else {
            return String.format("%d min", minutes);
        }
    }

    public String getFormattedDistance() {
        if (totalDistance >= 1.0) {
            return String.format("%.2f km", totalDistance);
        } else {
            return String.format("%.0f m", totalDistance * 1000);
        }
    }

    @Override
    public String toString() {
        return String.format("Route{path=%s, distance=%.2f km, time=%d min, algorithm=%s, landmarks=%s}",
                getFormattedPath(), totalDistance, estimatedTime, algorithm, landmarks);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Route route = (Route) obj;
        return Double.compare(route.totalDistance, totalDistance) == 0 &&
                estimatedTime == route.estimatedTime &&
                Objects.equals(path, route.path) &&
                Objects.equals(algorithm, route.algorithm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, totalDistance, estimatedTime, algorithm);
    }
}