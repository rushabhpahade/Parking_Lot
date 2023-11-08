package com.mangoapps.parking_lot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.mangoapps.parking_lot.model.Car;
import com.mangoapps.parking_lot.repository.CarRepository;

@SpringBootTest
public class CarServiceTests {
    @InjectMocks
    private CarService carService;

    @Mock
    private CarRepository carRepository;

    @Test
    public void testGetCarByRegistrationNumber() {
        String registrationNumber = "ABC123";
        Car car = new Car();
        car.setRegistrationNumber(registrationNumber);

        List<Car> cars = new ArrayList<>();
        cars.add(car);

        Mockito.when(carRepository.findByRegistrationNumber(registrationNumber)).thenReturn(car);

        Car result = carService.getCarByRegistrationNumber(registrationNumber);

        
        assertEquals(registrationNumber, result.getRegistrationNumber());
        // To check if test fails for Wrong registration Number..
        //assertEquals(registrationNumber, "Mh32x4639");
    }

}
