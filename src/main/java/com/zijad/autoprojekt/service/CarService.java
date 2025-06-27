package com.zijad.autoprojekt.service;

import com.zijad.autoprojekt.dto.car.CarRequest;
import com.zijad.autoprojekt.model.Car;
import com.zijad.autoprojekt.model.User;
import com.zijad.autoprojekt.repository.CarRepository;
import com.zijad.autoprojekt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final UserRepository userRepository;

    private final String uploadDir = "uploads/";



    @Autowired
    public CarService(CarRepository carRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car addCar(CarRequest request, MultipartFile image, String username) throws IOException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Car car = new Car();
        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setPrice(request.getPrice());
        car.setFuelType(request.getFuelType());
        car.setMileage(request.getMileage());
        car.setDescription(request.getDescription());
        car.setUser(user);

        if (image != null && !image.isEmpty()) {
            String imageName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            File destination = new File(uploadDir + imageName);
            image.transferTo(destination);
            car.setImageUrl("/" + uploadDir + imageName);
        }
        return carRepository.save(car);
    }

    public Car updateCar(Long id, CarRequest request, String username) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (!car.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Access denied");
        }

        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        car.setPrice(request.getPrice());
        car.setFuelType(request.getFuelType());
        car.setMileage(request.getMileage());
        car.setDescription(request.getDescription());

        return carRepository.save(car);
    }

    public void deleteCar(Long id, String username) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (!car.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Access denied");
        }
        carRepository.delete(car);
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

}

