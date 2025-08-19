package com.ugnavigate;

import com.ugnavigate.services.CampusDataService;
import com.ugnavigate.services.RoutingService;
import com.ugnavigate.services.TrafficService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        System.out.println("Welcome to UG Navigate: Optimal Routing Solution for University of Ghana Campus");
        System.out.println("Starting the application...");
        SpringApplication.run(App.class, args);
        System.out.println("UG Navigate is now running!");
        System.out.println("Access the API at: http://localhost:8080/api/routing");
        System.out.println("Health check: http://localhost:8080/api/routing/health");
    }

    @Bean
    public TrafficService trafficService() {
        return new TrafficService();
    }

    @Bean
    public RoutingService routingService(CampusDataService campusDataService, TrafficService trafficService) {
        return new RoutingService(campusDataService.getCampusMap(), trafficService);
    }
}