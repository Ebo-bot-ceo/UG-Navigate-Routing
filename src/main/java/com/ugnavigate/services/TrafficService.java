package com.ugnavigate.services;

public class TrafficService {

    public String assessTrafficConditions(String location) {
        // Logic to assess current traffic conditions based on the location
        // This could involve calling an external API or using a traffic data source
        return "Traffic conditions for " + location + ": Moderate";
    }

    public int adjustRouteForTraffic(int baseTravelTime, String trafficCondition) {
        // Logic to adjust travel time based on traffic conditions
        switch (trafficCondition.toLowerCase()) {
            case "heavy":
                return (int) (baseTravelTime * 1.5); // Increase time by 50%
            case "moderate":
                return (int) (baseTravelTime * 1.2); // Increase time by 20%
            case "light":
            default:
                return baseTravelTime; // No adjustment
        }
    }
}