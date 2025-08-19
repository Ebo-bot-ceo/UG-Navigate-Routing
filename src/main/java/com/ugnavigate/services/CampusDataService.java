package com.ugnavigate.services;

import com.ugnavigate.models.CampusMap;
import com.ugnavigate.models.Location;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class CampusDataService {
    private CampusMap campusMap;

    public CampusDataService() {
        this.campusMap = new CampusMap();
    }

    @PostConstruct
    public void initializeCampusData() {
        populateLocations();
        populateConnections();
    }

    private void populateLocations() {
        // Academic Buildings
        Location computerScience = new Location("Computer Science Department", 5.6500, -0.1867, "academic",
                "Department of Computer Science");
        computerScience.addLandmark("Computer Science");
        computerScience.addLandmark("DCIT");
        computerScience.addLandmark("Computer Lab");

        Location mathematics = new Location("Mathematics Department", 5.6510, -0.1870, "academic",
                "Department of Mathematics");
        mathematics.addLandmark("Mathematics");
        mathematics.addLandmark("Math Lab");

        Location physics = new Location("Physics Department", 5.6520, -0.1875, "academic", "Department of Physics");
        physics.addLandmark("Physics");
        physics.addLandmark("Physics Lab");

        Location chemistry = new Location("Chemistry Department", 5.6530, -0.1880, "academic",
                "Department of Chemistry");
        chemistry.addLandmark("Chemistry");
        chemistry.addLandmark("Chemistry Lab");

        Location biology = new Location("Biology Department", 5.6540, -0.1885, "academic", "Department of Biology");
        biology.addLandmark("Biology");
        biology.addLandmark("Biology Lab");

        // Administrative Buildings
        Location registry = new Location("Registry", 5.6550, -0.1890, "administrative", "Main Registry Office");
        registry.addLandmark("Registry");
        registry.addLandmark("Administration");

        Location senate = new Location("Senate Building", 5.6560, -0.1895, "administrative", "Senate Building");
        senate.addLandmark("Senate");
        senate.addLandmark("Administration");

        Location finance = new Location("Finance Office", 5.6570, -0.1900, "administrative",
                "Finance and Accounts Office");
        finance.addLandmark("Finance");
        finance.addLandmark("Accounts");

        // Facilities
        Location library = new Location("Balme Library", 5.6580, -0.1905, "facility", "Main University Library");
        library.addLandmark("Library");
        library.addLandmark("Balme");
        library.addLandmark("Study Area");

        Location cafeteria = new Location("Cafeteria", 5.6590, -0.1910, "facility", "Main Cafeteria");
        cafeteria.addLandmark("Cafeteria");
        cafeteria.addLandmark("Food");
        cafeteria.addLandmark("Restaurant");

        Location bank = new Location("University Bank", 5.6600, -0.1915, "facility", "University Bank Branch");
        bank.addLandmark("Bank");
        bank.addLandmark("ATM");
        bank.addLandmark("Financial Services");

        Location healthCenter = new Location("Health Center", 5.6610, -0.1920, "facility", "University Health Center");
        healthCenter.addLandmark("Health Center");
        healthCenter.addLandmark("Medical");
        healthCenter.addLandmark("Clinic");

        Location sportsCenter = new Location("Sports Center", 5.6620, -0.1925, "facility", "University Sports Center");
        sportsCenter.addLandmark("Sports Center");
        sportsCenter.addLandmark("Gym");
        sportsCenter.addLandmark("Fitness");

        // Landmarks
        Location independenceSquare = new Location("Independence Square", 5.6630, -0.1930, "landmark",
                "Independence Square");
        independenceSquare.addLandmark("Independence Square");
        independenceSquare.addLandmark("Square");
        independenceSquare.addLandmark("Monument");

        Location greatHall = new Location("Great Hall", 5.6640, -0.1935, "landmark", "Great Hall");
        greatHall.addLandmark("Great Hall");
        greatHall.addLandmark("Auditorium");
        greatHall.addLandmark("Events");

        Location botanicalGarden = new Location("Botanical Garden", 5.6650, -0.1940, "landmark",
                "University Botanical Garden");
        botanicalGarden.addLandmark("Botanical Garden");
        botanicalGarden.addLandmark("Garden");
        botanicalGarden.addLandmark("Nature");

        // Add all locations to campus map
        campusMap.addLocation(computerScience);
        campusMap.addLocation(mathematics);
        campusMap.addLocation(physics);
        campusMap.addLocation(chemistry);
        campusMap.addLocation(biology);
        campusMap.addLocation(registry);
        campusMap.addLocation(senate);
        campusMap.addLocation(finance);
        campusMap.addLocation(library);
        campusMap.addLocation(cafeteria);
        campusMap.addLocation(bank);
        campusMap.addLocation(healthCenter);
        campusMap.addLocation(sportsCenter);
        campusMap.addLocation(independenceSquare);
        campusMap.addLocation(greatHall);
        campusMap.addLocation(botanicalGarden);
    }

    private void populateConnections() {
        // Academic buildings connections
        campusMap.addConnection("Computer Science Department", "Mathematics Department", 0.2);
        campusMap.addConnection("Computer Science Department", "Physics Department", 0.3);
        campusMap.addConnection("Mathematics Department", "Physics Department", 0.2);
        campusMap.addConnection("Physics Department", "Chemistry Department", 0.25);
        campusMap.addConnection("Chemistry Department", "Biology Department", 0.3);

        // Administrative buildings connections
        campusMap.addConnection("Registry", "Senate Building", 0.15);
        campusMap.addConnection("Senate Building", "Finance Office", 0.2);
        campusMap.addConnection("Registry", "Finance Office", 0.25);

        // Academic to Administrative connections
        campusMap.addConnection("Computer Science Department", "Registry", 0.4);
        campusMap.addConnection("Mathematics Department", "Registry", 0.35);
        campusMap.addConnection("Physics Department", "Senate Building", 0.3);
        campusMap.addConnection("Chemistry Department", "Finance Office", 0.4);

        // Facility connections
        campusMap.addConnection("Balme Library", "Computer Science Department", 0.3);
        campusMap.addConnection("Balme Library", "Mathematics Department", 0.25);
        campusMap.addConnection("Cafeteria", "Computer Science Department", 0.35);
        campusMap.addConnection("Cafeteria", "Balme Library", 0.2);
        campusMap.addConnection("University Bank", "Finance Office", 0.15);
        campusMap.addConnection("University Bank", "Registry", 0.3);
        campusMap.addConnection("Health Center", "Biology Department", 0.25);
        campusMap.addConnection("Health Center", "Sports Center", 0.4);
        campusMap.addConnection("Sports Center", "Botanical Garden", 0.3);

        // Landmark connections
        campusMap.addConnection("Independence Square", "Great Hall", 0.2);
        campusMap.addConnection("Great Hall", "Senate Building", 0.25);
        campusMap.addConnection("Botanical Garden", "Biology Department", 0.2);
        campusMap.addConnection("Botanical Garden", "Health Center", 0.3);

        // Cross-campus connections
        campusMap.addConnection("Computer Science Department", "Balme Library", 0.3);
        campusMap.addConnection("Computer Science Department", "Cafeteria", 0.35);
        campusMap.addConnection("Mathematics Department", "Balme Library", 0.25);
        campusMap.addConnection("Physics Department", "Great Hall", 0.4);
        campusMap.addConnection("Chemistry Department", "Health Center", 0.35);
        campusMap.addConnection("Biology Department", "Botanical Garden", 0.2);
        campusMap.addConnection("Registry", "Great Hall", 0.3);
        campusMap.addConnection("Senate Building", "Independence Square", 0.25);
        campusMap.addConnection("Finance Office", "University Bank", 0.15);
        campusMap.addConnection("Cafeteria", "University Bank", 0.4);
        campusMap.addConnection("Balme Library", "Great Hall", 0.35);
        campusMap.addConnection("Sports Center", "Cafeteria", 0.5);
        campusMap.addConnection("Health Center", "Cafeteria", 0.3);
        campusMap.addConnection("Independence Square", "Botanical Garden", 0.6);
    }

    public CampusMap getCampusMap() {
        return campusMap;
    }

    public void setCampusMap(CampusMap campusMap) {
        this.campusMap = campusMap;
    }
}