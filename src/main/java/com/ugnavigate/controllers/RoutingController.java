package com.ugnavigate.controllers;

import com.ugnavigate.models.Route;
import com.ugnavigate.services.RoutingService;
import com.ugnavigate.services.CampusDataService;
import com.ugnavigate.services.TrafficService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/routing")
@CrossOrigin(origins = "*")
public class RoutingController {

    @Autowired
    private RoutingService routingService;

    @Autowired
    private CampusDataService campusDataService;

    @Autowired
    private TrafficService trafficService;

    @GetMapping("/locations")
    public ResponseEntity<List<String>> getAllLocations() {
        List<String> locations = routingService.getAllLocations();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/landmarks")
    public ResponseEntity<List<String>> getAllLandmarks() {
        List<String> landmarks = routingService.getAllLandmarks();
        return ResponseEntity.ok(landmarks);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = routingService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/shortest")
    public ResponseEntity<Route> getShortestRoute(
            @RequestParam String start,
            @RequestParam String end) {
        Route route = routingService.findShortestRoute(start, end);
        if (route != null) {
            return ResponseEntity.ok(route);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/optimal")
    public ResponseEntity<Route> getOptimalRoute(
            @RequestParam String start,
            @RequestParam String end) {
        Route route = routingService.findOptimalRoute(start, end);
        if (route != null) {
            return ResponseEntity.ok(route);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/landmarks/{landmark}")
    public ResponseEntity<List<Route>> getRoutesWithLandmarks(
            @PathVariable String landmark,
            @RequestParam String start,
            @RequestParam String end) {
        List<Route> routes = routingService.findRoutesWithLandmarks(start, end, landmark);
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/top")
    public ResponseEntity<List<Route>> getTopRoutes(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(defaultValue = "3") int count) {
        List<Route> routes = routingService.findTopRoutes(start, end, count);
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Route>> getRoutesByCategory(
            @PathVariable String category,
            @RequestParam String start,
            @RequestParam String end) {
        List<Route> routes = routingService.findRoutesByCategory(start, end, category);
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Route>> searchRoutes(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam String term) {
        List<Route> routes = routingService.searchRoutes(start, end, term);
        return ResponseEntity.ok(routes);
    }

    @GetMapping("/traffic")
    public ResponseEntity<Map<String, String>> getTrafficConditions(
            @RequestParam String location) {
        String conditions = trafficService.assessTrafficConditions(location);
        Map<String, String> response = new HashMap<>();
        response.put("location", location);
        response.put("conditions", conditions);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/route")
    public ResponseEntity<Route> createRoute(
            @RequestBody Map<String, String> request) {
        String start = request.get("start");
        String end = request.get("end");
        String algorithm = request.getOrDefault("algorithm", "shortest");

        Route route = null;
        switch (algorithm.toLowerCase()) {
            case "optimal":
                route = routingService.findOptimalRoute(start, end);
                break;
            case "shortest":
            default:
                route = routingService.findShortestRoute(start, end);
                break;
        }

        if (route != null) {
            return ResponseEntity.ok(route);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "UG Navigate Routing Service");
        health.put("version", "1.0.0");
        health.put("locations", routingService.getAllLocations().size());
        health.put("landmarks", routingService.getAllLandmarks().size());
        health.put("categories", routingService.getAllCategories().size());
        return ResponseEntity.ok(health);
    }
}