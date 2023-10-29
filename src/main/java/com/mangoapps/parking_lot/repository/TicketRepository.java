package com.mangoapps.parking_lot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mangoapps.parking_lot.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
	List<Ticket> findByCar_Color(String color);
	List<Ticket> findByCar_RegistrationNumber(String registrationNumber);

	@Query("SELECT t.spotNumber FROM Ticket t WHERE t.car.color = :color")
	List<Integer> findTicketNumbersByCarColor(String color);

	@Query("SELECT ticket.car.registrationNumber FROM Ticket ticket WHERE ticket.car.color = :color")
	List<String> findRegistrationNumbersByCarColor(@Param("color") String color);

	@Query("SELECT t FROM Ticket t WHERE t.spotNumber = :spotNumber")
	Ticket findBySpotNumber(@Param("spotNumber") Long spotNumber);
}
