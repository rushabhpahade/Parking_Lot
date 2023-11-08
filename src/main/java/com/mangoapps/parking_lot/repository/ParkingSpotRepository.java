package com.mangoapps.parking_lot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mangoapps.parking_lot.model.ParkingSpot;
import com.mangoapps.parking_lot.model.Ticket;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
	ParkingSpot findFirstByOccupiedFalse();
	List<ParkingSpot> findByOccupiedFalse();
	void deleteAll();   
    List<ParkingSpot> findByOccupiedContaining(Ticket ticket);
    List<ParkingSpot> findByMaintenance(boolean maintenance);
    ParkingSpot findByIdAndOccupiedFalse(Long id);
    ParkingSpot findById(long id);
}