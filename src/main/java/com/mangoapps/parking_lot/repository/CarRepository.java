package com.mangoapps.parking_lot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mangoapps.parking_lot.model.Car;
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByColor(String color);
    List<Car> findByRegistrationNumber(String registrationNumber);
}

