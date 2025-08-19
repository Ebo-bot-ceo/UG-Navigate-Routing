package com.ugnavigate.algorithms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.ugnavigate.models.CampusMap;
import com.ugnavigate.services.TrafficService;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class RoutingAlgorithmTest {

    private RoutingAlgorithm routingAlgorithm;
    private CampusMap campusMap;

    @BeforeEach
    void setUp() {
        campusMap = new CampusMap();
        TrafficService trafficService = new TrafficService();
        routingAlgorithm = new RoutingAlgorithm(campusMap, trafficService);

        // Set up test data
        Map<String, Double> locationA = new HashMap<>();
        locationA.put("B", 10.0);
        locationA.put("C", 15.0);
        campusMap.addLocation("A", locationA);

        Map<String, Double> locationB = new HashMap<>();
        locationB.put("A", 10.0);
        locationB.put("C", 5.0);
        locationB.put("D", 12.0);
        campusMap.addLocation("B", locationB);

        Map<String, Double> locationC = new HashMap<>();
        locationC.put("A", 15.0);
        locationC.put("B", 5.0);
        locationC.put("D", 8.0);
        campusMap.addLocation("C", locationC);

        Map<String, Double> locationD = new HashMap<>();
        locationD.put("B", 12.0);
        locationD.put("C", 8.0);
        campusMap.addLocation("D", locationD);
    }

    @Test
    void testFindShortestPathReturnsEmptyListForUnknownLocations() {
        List<String> expected = Collections.emptyList();
        List<String> actual = routingAlgorithm.findShortestPath("Unknown", "Unknown");
        assertEquals(expected, actual);
    }

    @Test
    void testFindShortestPathReturnsList() {
        List<String> actual = routingAlgorithm.findShortestPath("A", "B");
        assertNotNull(actual);
        assertTrue(actual instanceof List);
    }

    @Test
    void testCalculateArrivalTimeReturnsZeroForUnknownLocations() {
        long actual = routingAlgorithm.calculateArrivalTime("Unknown", "Unknown");
        assertEquals(0, actual);
    }

    @Test
    void testCalculateArrivalTimeReturnsLong() {
        long actual = routingAlgorithm.calculateArrivalTime("A", "B");
        assertTrue(actual >= 0);
    }

    @Test
    void testFindShortestPathWithSameStartAndEnd() {
        List<String> actual = routingAlgorithm.findShortestPath("A", "A");
        assertNotNull(actual);
        assertEquals(Arrays.asList("A"), actual);
    }

    @Test
    void testCalculateArrivalTimeWithSameStartAndEnd() {
        long actual = routingAlgorithm.calculateArrivalTime("A", "A");
        assertTrue(actual >= 0);
    }

    @Test
    void testFindShortestPath() {
        List<String> expectedPath = Arrays.asList("A", "B");
        List<String> actualPath = routingAlgorithm.findShortestPath("A", "B");
        assertEquals(expectedPath, actualPath);
    }

    @Test
    void testFindShortestPathWithIntermediateLocation() {
        List<String> expectedPath = Arrays.asList("A", "B", "D");
        List<String> actualPath = routingAlgorithm.findShortestPath("A", "D");
        assertEquals(expectedPath, actualPath);
    }

    @Test
    void testCalculateArrivalTime() {
        long actualArrivalTime = routingAlgorithm.calculateArrivalTime("A", "B");
        assertTrue(actualArrivalTime > 0);
    }
}