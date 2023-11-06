package com.mangoapps.parking_lot.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
			parkingSpotRepository.save(spot);
		}

		return ResponseEntity.ok("Parking lot with " + size + " spots created.");
	}

	@PostMapping("/parkCar")
	public ResponseEntity<String> parkCar(@RequestParam String registrationNumber, @RequestParam String color) {
		ParkingSpot availableSpot = parkingSpotRepository.findFirstByOccupiedFalse();
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
		if (availableSpot != null) {
			// Set the spot as occupied
			availableSpot.setOccupied(true);
			parkingSpotRepository.save(availableSpot);

			Car car = new Car();
			car.setRegistrationNumber(registrationNumber);
			car.setColor(color);
			carService.saveCar(car);

			addParkedRegistrationNumber(registrationNumber);
			Ticket ticket = new Ticket();
			ticket.setSpotNumber(availableSpot.getId());
			ticket.setCar(car);
			ticket.setOccupied(true); // Mark the ticket as occupied
	        ticket.setParkingSpot(availableSpot); // Set the parking spot
			ticketService.saveTicket(ticket);

			return ResponseEntity.ok("Car parked in spot " + availableSpot.getId());
		} else {
			return ResponseEntity.ok("Parking is full.");
		}
	}
	// HashSet for parked vehicles using Registration Numbers.
	private Set<String> parkedRegistrationNumbers = new HashSet<>();

	private boolean isRegistrationNumberParked(String registrationNumber) {
		return parkedRegistrationNumbers.contains(registrationNumber);
	}

	private void addParkedRegistrationNumber(String registrationNumber) {
		parkedRegistrationNumbers.add(registrationNumber);
	}
	@PostMapping("/removeCarBySpot")
	public ResponseEntity<String> removeCarBySpot(@RequestParam long spotNumber) {
	    ParkingSpot spot = parkingSpotRepository.findById(spotNumber).orElse(null);

	    if (spot != null && spot.isOccupied()) {
	        // Find the ticket
	        Ticket ticket = ticketService.getTicketBySpotNumber(spotNumber);
	        if (ticket != null) {
	            // Free up the spot
	            spot.setOccupied(false);
	            parkingSpotRepository.save(spot);

	            // Mark the ticket as unoccupied
	            ticket.setOccupied(false);
	            ticketService.saveTicket(ticket);

	            return ResponseEntity.ok("Car removed from spot " + spotNumber);
	        }
	    }

	    return ResponseEntity.ok("Spot " + spotNumber + " is empty or doesn't exist.");
	}

	@GetMapping("/cars/{color}")
	public List<Car> getCarsByColor(@PathVariable String color) {
		return carService.getCarsByColor(color);
	}
	@GetMapping("/allCars")
	public List<Car> getAllCarsInParkingLot() {
		return carService.getAllCars();
	}
	@GetMapping("/parkedCars")
	public List<Map<String, Object>> getParkedCars() {
	    List<Object[]> result = ticketService.getParkedCarsInfo();

	    List<Map<String, Object>> parkedCarInfoList = new ArrayList<>();

	    for (Object[] row : result) {
	        Map<String, Object> carInfo = new HashMap<>();
	        carInfo.put("car", row[0]);
	        carInfo.put("parkingSpotNumber", row[1]);
	        carInfo.put("ticketNumber", row[2]);

	        parkedCarInfoList.add(carInfo);
	    }

	    return parkedCarInfoList;
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
