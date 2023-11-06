package com.mangoapps.parking_lot.model;

import jakarta.persistence.*;

@Entity
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "car_id")
	private Car car;
	
	@ManyToOne
    @JoinColumn(name = "spot_id")
    private ParkingSpot parkingSpot;

	@Column(name = "occupied")
    private boolean occupied;

	@Column(name = "spot_number", nullable = false)
	private Long spotNumber;

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

	public boolean getOccupied() {
		return occupied;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	public Long getSpotNumber() {
		return spotNumber;
	}

	public void setSpotNumber(Long spotNumber) {
		this.spotNumber = spotNumber;
	}

	public ParkingSpot getParkingSpot() {
		return parkingSpot;
	}

	public void setParkingSpot(ParkingSpot parkingSpot) {
		this.parkingSpot = parkingSpot;
	}
}

