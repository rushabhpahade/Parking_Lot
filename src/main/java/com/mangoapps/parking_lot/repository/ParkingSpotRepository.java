package com.mangoapps.parking_lot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mangoapps.parking_lot.model.ParkingSpot;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
	ParkingSpot findFirstByOccupiedFalse();
	void deleteAll();    
}