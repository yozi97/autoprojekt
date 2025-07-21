package com.zijad.autoprojekt.controller;

import com.zijad.autoprojekt.dto.car.CarRequest;
import com.zijad.autoprojekt.model.Car;
import com.zijad.autoprojekt.service.CarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
@CrossOrigin(origins = "http://localhost:4200")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Car> addCar(@RequestPart("data") CarRequest request,
                                      @RequestPart(value = "image", required = false) MultipartFile image,
                                      Principal principal) throws Exception {
        Car car = carService.addCar(request, image, principal.getName());
        return ResponseEntity.ok(car);
    }

    @PostMapping("/json")
    public ResponseEntity<Car> addCarJson(@RequestBody CarRequest request, Principal principal) throws Exception {
        Car car = carService.addCar(request, null, principal.getName());
        return ResponseEntity.ok(car);
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @GetMapping("/mine")
    public ResponseEntity<List<Car>> getMyCars(Principal principal) {
        return ResponseEntity.ok(carService.getCarsByUser(principal.getName()));
    }



    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @Valid @RequestBody CarRequest request, Principal principal) {
        Car updatedCar = carService.updateCar(id, request, principal.getName());
        return ResponseEntity.ok(updatedCar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Long id, Principal principal) {
        carService.deleteCar(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return carService.getCarById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/api/my/cars")
    public ResponseEntity<List<Car>> getMyCars() {
        List<Car> myCars = carService.getMyCars();
        return ResponseEntity.ok(myCars);
    }

//    @GetMapping("/test")
//    public ResponseEntity<String> test() {
//        return ResponseEntity.ok("Server radi");
//    }


}
