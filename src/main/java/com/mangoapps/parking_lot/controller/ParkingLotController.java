package com.mangoapps.parking_lot.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mangoapps.parking_lot.model.Car;
import com.mangoapps.parking_lot.model.ParkingSpot;
import com.mangoapps.parking_lot.model.Ticket;
import com.mangoapps.parking_lot.repository.ParkingSpotRepository;
import com.mangoapps.parking_lot.service.CarService;
import com.mangoapps.parking_lot.service.TicketService;

@RestController
@CrossOrigin(origins = "http://localhost:4200") 
@RequestMapping("/parkinglot")
public class ParkingLotController {
    @Autowired
    private CarService carService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private ParkingSpotRepository parkingSpotRepository;
    
    //private List<Ticket> parkingLot = new ArrayList<>();

    @PostMapping("/createParkingLot")
    public ResponseEntity<String> createParkingLot(@RequestParam int size) {
    	 parkingSpotRepository.deleteAll();

         // Create parking Lot
         for (int i = 0; i < size; i++) {
             ParkingSpot spot = new ParkingSpot();
             spot.setOccupied(false);
             parkingSpotRepository.save(spot);
         }

         return ResponseEntity.ok("Parking lot with " + size + " spots created.");
    }
    
    @PostMapping("/parkCar")
    public ResponseEntity< String> parkCar(@RequestParam String registrationNumber, @RequestParam String color) {
    	 ParkingSpot availableSpot = parkingSpotRepository.findFirstByOccupiedFalse();
 	    // Validations for RegistrationNumber. It should be of length 9 or 10 only..
 	    if (!registrationNumber.matches("^[A-Za-z][A-Za-z0-9]{8,9}$")) {
 	        return ResponseEntity.badRequest().body("Invalid registration number format");
 	    }

 	    // Validation for color. String should only contain alphabets..
 	    if (!color.matches("^[A-Za-z]+$")) {
 	        return ResponseEntity.badRequest().body("Invalid color format");
 	    }
         if (availableSpot != null) {
             // Set the spot as occupied
             availableSpot.setOccupied(true);
             parkingSpotRepository.save(availableSpot);

             Car car = new Car();
             car.setRegistrationNumber(registrationNumber);
             car.setColor(color);
             carService.saveCar(car);
             Ticket ticket = new Ticket();
             ticket.setSpotNumber(availableSpot.getId());
             ticket.setCar(car);
             ticketService.saveTicket(ticket);
             
             return ResponseEntity.ok("Car parked in spot " + availableSpot.getId());
         } else {
             return ResponseEntity.ok("Parking is full.");
         }
    }
    @PostMapping("/removeCarBySpot")
    public ResponseEntity<String> removeCarBySpot(@RequestParam long spotNumber) {
    	ParkingSpot spot = parkingSpotRepository.findById(spotNumber).orElse(null);

        if (spot != null && spot.isOccupied()) {
            // Find the ticket
        	// Logic:- First Delete The Ticket associated with the spot and then delete car since there is a foreign key reference..
            Ticket ticket = ticketService.getTicketBySpotNumber(spotNumber);
            if (ticket != null) {
                // Delete the ticket first
                ticketService.deleteTicket(ticket);

                // Free up the spot
                spot.setOccupied(false);
                parkingSpotRepository.save(spot);

                // Find the associated car
                Car car = ticket.getCar();
                if (car != null) {
                    // Delete the car
                    carService.deleteCar(car);
                }

                return ResponseEntity.ok("Car removed from spot " + spotNumber);
            }
        }

        return ResponseEntity.ok("Spot " + spotNumber + " is empty or doesn't exist.");
    }

    @PostMapping("/parkCarInNearestSpot")
    public ResponseEntity<String> parkCarInNearestSpot(@RequestParam String registrationNumber, @RequestParam String color) {
    	   ParkingSpot availableSpot = parkingSpotRepository.findFirstByOccupiedFalse();
    	   
    	    // Validations for RegistrationNumber. It should be of length 9 or 10 only..
    	    if (!registrationNumber.matches("^[A-Za-z][A-Za-z0-9]{8,9}$")) {
    	        return ResponseEntity.badRequest().body("Invalid registration number format");
    	    }

    	    // Validation for color. String should only contain alphabets..
    	    if (!color.matches("^[A-Za-z]+$")) {
    	        return ResponseEntity.badRequest().body("Invalid color format");
    	    }
    	    if (availableSpot != null) {
    	        availableSpot.setOccupied(true);
    	        parkingSpotRepository.save(availableSpot);

    	        Car car = new Car();
    	        car.setRegistrationNumber(registrationNumber);
    	        car.setColor(color);
    	        carService.saveCar(car);

    	        Ticket ticket = new Ticket();
    	        ticket.setSpotNumber(availableSpot.getId());
    	        ticket.setCar(car);
    	        ticketService.saveTicket(ticket);

    	        return ResponseEntity.ok("Car parked in nearest spot " + availableSpot.getId());
    	    } else {
    	        return ResponseEntity.ok("Parking is full.");
    	    }
    }
    @GetMapping("/cars/{color}")
    public List<Car> getCarsByColor(@PathVariable String color) {
        return carService.getCarsByColor(color);
    }
    @GetMapping("/registrationNumbersByColor")
    public List<String> getRegistrationNumbersByColor(@RequestParam String color) {
        List<String> registrationNumbers = ticketService.getRegistrationNumbersByColor(color);
        return registrationNumbers;
    }
    @GetMapping("/ticketNumberByRegistrationNumber")
    public Long getTicketNumberByRegistrationNumber(@RequestParam String registrationNumber) {
    	   List<Ticket> tickets = ticketService.getTicketsByRegistrationNumber(registrationNumber);

    	    if (!tickets.isEmpty()) {
    	        // Find the latest ticket based on the spot number
    	        Ticket latestTicket = tickets.stream()
    	            .max(Comparator.comparing(Ticket::getSpotNumber))
    	            .orElse(null);

    	        if (latestTicket != null) {
    	            return latestTicket.getSpotNumber();
    	        }
    	    }
    	 // Car not found or no tickets for the registration number
    	    return null; 
    }
    @GetMapping("/ticketNumbersByColor")
    public List<Integer> getTicketNumbersByColor(@RequestParam String color) {
    	 List<Integer> ticketNumbers = ticketService.getTicketNumbersByColor(color);
    	    return ticketNumbers;
    }
    @GetMapping("/ticket/{registrationNumber}")
    public List<Car> getCarByRegistrationNumber(@PathVariable String registrationNumber) {
        return carService.getCarByRegistrationNumber(registrationNumber);
    }

    @GetMapping("/tickets/{color}")
    public List<Ticket> getTicketsByColor(@PathVariable String color) {
        return ticketService.getTicketsByColor(color);
    }
}
