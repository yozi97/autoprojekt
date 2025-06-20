package com.zijad.autoprojekt.service;

import com.zijad.autoprojekt.model.Car;
import com.zijad.autoprojekt.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car addCar(Car car) {
        return carRepository.save(car);
    }

    public boolean updateCar(Long id, Car car) {
        return carRepository.findById(id)
                .map(existingCar -> {
                    existingCar.setBrand(car.getBrand());
                    existingCar.setModel(car.getModel());
                    existingCar.setYear(car.getYear());
                    carRepository.save(existingCar);
                    return true;
                }).orElse(false);
    }

    public boolean deleteCar(Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

