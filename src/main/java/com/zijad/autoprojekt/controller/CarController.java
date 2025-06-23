package com.zijad.autoprojekt.controller;

import com.zijad.autoprojekt.model.Car;
import com.zijad.autoprojekt.service.CarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @PostMapping()
    public ResponseEntity<String> addCar(@Valid @RequestBody Car car) {
        carService.addCar(car);
        return ResponseEntity.ok("Car added");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCar(@PathVariable Long id, @Valid @RequestBody Car car) {
        boolean updated = carService.updateCar(id, car);
        if (updated) {
            return ResponseEntity.ok("Car updated");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable Long id) {
        boolean deleted = carService.deleteCar(id);
        if (deleted) {
            return ResponseEntity.ok("Car deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return carService.getCarById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
