package com.mangoapps.parking_lot.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "car_id")
	private Car car;

	@Column(name = "occupied")
	private int occupied; 

	@ManyToMany
	@JoinTable(name = "ticket_spot",joinColumns = @JoinColumn(name = "ticket_id"),inverseJoinColumns = @JoinColumn(name = "spot_id"))
	private List<ParkingSpot> occupiedSpots = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public int getOccupied() {
		return occupied;
	}

	public void setOccupied(int occupied) {
		this.occupied = occupied;
	}

	public List<ParkingSpot> getOccupiedSpots() {
		return occupiedSpots;
	}

	public void setOccupiedSpots(List<ParkingSpot> occupiedSpots) {
		this.occupiedSpots = occupiedSpots;
	}
}

