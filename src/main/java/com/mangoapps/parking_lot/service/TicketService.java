package com.mangoapps.parking_lot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mangoapps.parking_lot.model.Car;
import com.mangoapps.parking_lot.model.ParkingSpot;
import com.mangoapps.parking_lot.model.Ticket;
import com.mangoapps.parking_lot.repository.ParkingSpotRepository;
import com.mangoapps.parking_lot.repository.TicketRepository;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    public List<Ticket> getTicketsByColor(String color) {
        return ticketRepository.findByCar_Color(color);
    }
    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }
    public List<String> getRegistrationNumbersByColor(String color) {
        return ticketRepository.findRegistrationNumbersByCarColor(color);
    }

    public List<Ticket> getTicketsByRegistrationNumber(String registrationNumber) {
        return ticketRepository.findByCar_RegistrationNumber(registrationNumber);
    }

    public List<Integer> getTicketNumbersByColor(String color) {
        return ticketRepository.findTicketNumbersByCarColor(color);
    }
    public void deleteTicket(Ticket ticket) {
        ticketRepository.delete(ticket);
    }

    public Ticket getTicketByCar(Car car) {
        return ticketRepository.getTicketWithOccupiedSpotsByCar(car);
    }
    
    public List<ParkingSpot> getParkingSpots(Ticket ticket) {
        return parkingSpotRepository.findByOccupiedContaining(ticket);
    }
}

