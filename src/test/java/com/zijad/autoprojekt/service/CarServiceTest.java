package com.zijad.autoprojekt.service;

import com.zijad.autoprojekt.model.Car;
import com.zijad.autoprojekt.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CarServiceTest {

    private CarRepository carRepository;
    private CarService carService;

    @BeforeEach
    void setUp() {
        carRepository = mock(CarRepository.class);
        carService = new CarService(carRepository);
    }

    @Test
    void testGetAllCarsReturnsList() {

        Car car1 = new Car();
        car1.setBrand("Audi");
        Car car2 = new Car();
        car2.setBrand("BMW");

        List<Car> mockCars = Arrays.asList(car1, car2);

        when(carRepository.findAll()).thenReturn(mockCars);

        List<Car> result = carService.getAllCars();

        assertEquals(2, result.size());
        assertEquals("Audi", result.get(0).getBrand());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void testAddCar() {
        Car car = new Car(null, "Audi", "A4", 2020);
        Car savedCar = new Car(1L, "BMW", "320", 2020);

        when(carRepository.save(car)).thenReturn(savedCar);

        Car result = carService.addCar(car);

        assertEquals(savedCar, result);
        verify(carRepository, times(1)).save(car);
    }

    @Test
    void testDeleteCarExists() {
        Long id = 1L;
        when(carRepository.existsById(id)).thenReturn(true);

        boolean deleted = carService.deleteCar(id);

        assertTrue(deleted);
        verify(carRepository).deleteById(id);
    }

    @Test
    void testDeleteCarNotExists() {
        Long id = 1L;
        when(carRepository.existsById(id)).thenReturn(false);

        boolean deleted = carService.deleteCar(id);

        assertFalse(deleted);
        verify(carRepository, never()).deleteById(id);
    }

    @Test
    void testUpdateCarSuccess() {
        Long id = 1L;
        Car existingCar = new Car(id, "Opel", "Astra", 2010);
        Car updatedCar = new Car(null, "Opel", "Corsa", 2012);

        when(carRepository.findById(id)).thenReturn(Optional.of(existingCar));

        boolean updated = carService.updateCar(id, updatedCar);

        assertTrue(updated);
        assertEquals("Corsa", existingCar.getModel());
        assertEquals(2012, existingCar.getYear());
        verify(carRepository).save(existingCar);
    }

    @Test
    void testUpdateCarNotFound() {
        Long id = 1L;
        Car car = new Car(null, "Renault", "Clio", 2015);

        when(carRepository.findById(id)).thenReturn(Optional.empty());

        boolean updated = carService.updateCar(id, car);

        assertFalse(updated);
        verify(carRepository, never()).save(any());
    }
}
