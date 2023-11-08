package com.mangoapps.parking_lot.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mangoapps.parking_lot.model.Car;
import com.mangoapps.parking_lot.model.ParkingSpot;
import com.mangoapps.parking_lot.model.Ticket;
import com.mangoapps.parking_lot.repository.CarRepository;
import com.mangoapps.parking_lot.repository.ParkingSpotRepository;
import com.mangoapps.parking_lot.repository.TicketRepository;
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
	@Autowired
	private TicketRepository ticketRepository;
	@Autowired
	private CarRepository carRepository;


	@PostMapping("/createParkingLot")
	public ResponseEntity<String> createParkingLot(@RequestParam int size) {
		parkingSpotRepository.deleteAll();
		parkingSpotRepository.deleteAll();
		ticketRepository.deleteAll();
		carRepository.deleteAll();
		parkedRegistrationNumbers.clear();
		// Create parking Lot of size 
		for (int i = 0; i < size; i++) {
			ParkingSpot spot = new ParkingSpot();
			spot.setOccupied(false);
			spot.setMaintenance(false);
			parkingSpotRepository.save(spot);
		}

		return ResponseEntity.ok("Parking lot with " + size + " spots created.");
	}

	@PostMapping("/parkCar")
	public  ResponseEntity< String> parkCar(@RequestParam String registrationNumber, @RequestParam String color, @RequestParam int size) {
	    List<ParkingSpot> availableSpots = findConsecutiveAvailableSpots(size);
		// Validations for RegistrationNumber. It should be of length 9 or 10 only..
		if (!registrationNumber.matches("^[A-Za-z][A-Za-z0-9]{8,9}$")) {
			return ResponseEntity.badRequest().body("Invalid registration number format");
		}

		// Validation for color. String should only contain alphabets..
		if (!color.matches("^[A-Za-z]+$")) {
			return ResponseEntity.badRequest().body("Invalid color format");
		}
		// Check if Car with Same Registration Number is already Present.
		if (isRegistrationNumberParked(registrationNumber)) {
			return ResponseEntity.badRequest().body("Car with this registration number is already parked.");
		}
		if (!availableSpots.isEmpty()) {
	        allocateConsecutiveSpots(availableSpots, registrationNumber, color, size);
			addParkedRegistrationNumber(registrationNumber);
	        return ResponseEntity.ok("Car parked in spots: " + getSpotNumbers(availableSpots));
	    } else {
	        return ResponseEntity.ok("No parking spots available for your vehicle");
	    }
	}
	private List<ParkingSpot> findConsecutiveAvailableSpots(int requiredSize) {
	    List<ParkingSpot> availableSpots = new ArrayList<>();
	    List<ParkingSpot> allSpots = parkingSpotRepository.findByOccupiedFalse();
	    int consecutiveCount = 0;

	    for (ParkingSpot spot : allSpots) {
	        if (consecutiveCount == requiredSize) {
	            break;
	        }

	        if (!spot.isOccupied() && !spot.isMaintenance()) {
	            availableSpots.add(spot);
	            consecutiveCount++;
	        } else {
	            availableSpots.clear();
	            consecutiveCount = 0;
	        }
	    }

	    return consecutiveCount == requiredSize ? availableSpots : Collections.emptyList();
	}
	private void allocateConsecutiveSpots(List<ParkingSpot> availableSpots, String registrationNumber, String color, int size) {
		availableSpots.forEach(spot -> {
	        spot.setOccupied(true);
	        parkingSpotRepository.save(spot);
	    });

	    Car car = new Car();
	    car.setRegistrationNumber(registrationNumber);
	    car.setColor(color);
	    car.setSize(size);
	    carService.saveCar(car);

	    Ticket ticket = new Ticket();
	    ticket.setCar(car);
	    ticket.getOccupiedSpots().addAll(availableSpots);
	    ticketService.saveTicket(ticket);
	
	}

	private String getSpotNumbers(List<ParkingSpot> spots) {
	    return spots.stream()
	            .map(spot -> spot.getId().toString())
	            .collect(Collectors.joining(", "));
	}
	// HashSet for parked vehicles using Registration Numbers.
	private Set<String> parkedRegistrationNumbers = new HashSet<>();

	private boolean isRegistrationNumberParked(String registrationNumber) {
		return parkedRegistrationNumbers.contains(registrationNumber);
	}

	private void addParkedRegistrationNumber(String registrationNumber) {
		parkedRegistrationNumbers.add(registrationNumber);
	}

	@PostMapping("/removeCarByRegistration")
	public ResponseEntity<String> removeCarByRegistration(@RequestParam String registrationNumber) {
	    Car car = carService.getCarByRegistrationNumber(registrationNumber);
	    
	    if (car != null) {
	        Ticket ticket = ticketService.getTicketByCar(car);
	        
	        if (ticket != null) {
	            List<ParkingSpot> spots = ticket.getOccupiedSpots();

	            // Delete the ticket first
	            ticketService.deleteTicket(ticket);

	            // Free up the spot
	            for(ParkingSpot spot:spots) {
	            spot.setOccupied(false);
	            parkingSpotRepository.save(spot);
	            }
	            // Delete the car
	            carService.deleteCar(car);
	            parkedRegistrationNumbers.remove(registrationNumber);
	            if(spots.size()==1)
	            	return ResponseEntity.ok("Car removed from spot " + spots.get(0).getId());
	            else
	            	return ResponseEntity.ok("Car removed from spot " + spots.get(0).getId() +"-"+ spots.get(spots.size()-1).getId());
	            
	        }
	    }

	    return ResponseEntity.ok("Car with registration number " + registrationNumber + " is not found in the parking lot.");
	}

	@GetMapping("/cars/{color}")
	public List<Car> getCarsByColor(@PathVariable String color) {
		return carService.getCarsByColor(color);
	}
	@GetMapping("/allCars")
	public List<Car> getAllCarsInParkingLot() {
		return carService.getAllCars();
	}
	@GetMapping("/registrationNumbersByColor")
	public List<String> getRegistrationNumbersByColor(@RequestParam String color) {
		List<String> registrationNumbers = ticketService.getRegistrationNumbersByColor(color);
		return registrationNumbers;
	}
	
	@GetMapping("/ticketNumbersByColor")
	public List<Integer> getTicketNumbersByColor(@RequestParam String color) {
		List<Integer> ticketNumbers = ticketService.getTicketNumbersByColor(color);
		return ticketNumbers;
	}
	@GetMapping("/ticket/{registrationNumber}")
	public Car getCarByRegistrationNumber(@PathVariable String registrationNumber) {
		return carService.getCarByRegistrationNumber(registrationNumber);
	}

	@GetMapping("/tickets/{color}")
	public List<Ticket> getTicketsByColor(@PathVariable String color) {
		return ticketService.getTicketsByColor(color);
	}
	
	@PutMapping("/markForMaintenance/{spotId}")
	public ResponseEntity<String> markForMaintenance(@PathVariable Long spotId) {
	    ParkingSpot spot = parkingSpotRepository.findByIdAndOccupiedFalse(spotId);
	    if (spot != null) {
	        spot.setMaintenance(true);
	        parkingSpotRepository.save(spot);
	        return ResponseEntity.ok("Spot marked for maintenance.");
	    } else {
	        return ResponseEntity.ok("Spot is already occupied and cannot be marked for maintenance.");
	    }
	}
	@PutMapping("/completeMaintenance/{spotId}")
	public ResponseEntity<String> completeMaintenance(@PathVariable Long spotId) {
	    Optional<ParkingSpot> spotOptional = parkingSpotRepository.findById(spotId);
	    if (spotOptional.isPresent()) {
	        ParkingSpot spot = spotOptional.get();
	        if (!spot.isOccupied()) {
	            spot.setMaintenance(false);
	            parkingSpotRepository.save(spot);
	            return ResponseEntity.ok("Maintenance completed for the spot.");
	        } else {
	            return ResponseEntity.ok("Cannot complete maintenance for an occupied spot.");
	        }
	    } else {
	        return ResponseEntity.ok("Spot not found.");
	    }
	}
}
