package com.mangoapps.parking_lot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.mangoapps.parking_lot.model.ParkingSpot;
import com.mangoapps.parking_lot.repository.ParkingSpotRepository;

import jakarta.annotation.PostConstruct;

@Configuration
public class ParkingLotIniConfig {
	@Autowired
	private ParkingSpotRepository parkingSpotRepository;

	//Set the Intial Size of Parking Lot as 5.
	private int defaultSize=5;;

	@PostConstruct
	public void initializeParkingLot() {
		parkingSpotRepository.deleteAll();

		// Create the parking spots
		for (int i = 0; i < defaultSize; i++) {
			ParkingSpot spot = new ParkingSpot();
			spot.setOccupied(false);
			parkingSpotRepository.save(spot);
		}
	}
}
