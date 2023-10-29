package com.mangoapps.parking_lot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mangoapps.parking_lot.model.Car;
import com.mangoapps.parking_lot.repository.CarRepository;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    public List<Car> getCarsByColor(String color) {
        return carRepository.findByColor(color);
    }

    public List<Car> getCarByRegistrationNumber(String registrationNumber) {
        return carRepository.findByRegistrationNumber(registrationNumber);
    }
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
    public Car saveCar(Car car) {
        return carRepository.save(car);
    }
    public void deleteCar(Car car) {
        carRepository.delete(car);
    }


}
