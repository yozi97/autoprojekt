package com.zijad.autoprojekt.service;

import com.zijad.autoprojekt.dto.car.CarRequest;
import com.zijad.autoprojekt.model.Car;
import com.zijad.autoprojekt.model.User;
import com.zijad.autoprojekt.repository.CarRepository;
import com.zijad.autoprojekt.repository.UserRepository;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;
    @InjectMocks
    private CarService carService;
    @Mock
    private UserRepository userRepository;


    @Test
    public void testGetAllCars() {

        Car car1 = new Car();
        car1.setBrand("BMW");
        car1.setModel("X5");

        Car car2 = new Car();
        car2.setBrand("Audi");
        car2.setModel("A4");

        List<Car> mockCarList = Arrays.asList(car1, car2);

        when(carRepository.findAll()).thenReturn(mockCarList);

        List<Car> result = carService.getAllCars();

        assertEquals(2,result.size());
        assertEquals("BMW", result.get(0).getBrand());
        assertEquals("Audi", result.get(1).getBrand());

        verify(carRepository, times(1)).findAll();
    }

    @Test
    public void testAddCar_withImage_success() throws IOException {

        CarRequest request = new CarRequest();
        request.setBrand("BMW");
        request.setModel("X5");
        request.setYear(2020);
        request.setPrice(25000.0);
        request.setFuelType("Diesel");
        request.setMileage(100000);
        request.setDescription("Odlican auto");

        MultipartFile mockImage = mock(MultipartFile.class);
        when(mockImage.isEmpty()).thenReturn(false);
        when(mockImage.getOriginalFilename()).thenReturn("car.jpg");

        User mockUser = new User();
        mockUser.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(mockUser));

        doNothing().when(mockImage).transferTo(any(File.class));

        Car savedCar = new Car();
        savedCar.setId(1L);
        when(carRepository.save(any(Car.class))).thenReturn(savedCar);

        Car result = carService.addCar(request, mockImage, "test@example.com");

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    public void testAddCar_userNotFound_throwsException() {

        CarRequest request = new CarRequest();
        request.setBrand("BMW");

        MultipartFile mockImage = mock(MultipartFile.class);

        when(userRepository.findByEmail("nonexistent@example.com"))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(
                UsernameNotFoundException.class,
                () -> carService.addCar(request, mockImage, "nonexistent@example.com")
        );

        assertEquals("User not found", exception.getMessage());

        verify(carRepository, never()).save(any(Car.class));
    }

}
