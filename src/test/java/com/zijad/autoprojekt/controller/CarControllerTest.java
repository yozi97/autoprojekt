package com.zijad.autoprojekt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zijad.autoprojekt.dto.car.CarRequest;
import com.zijad.autoprojekt.model.Car;
import com.zijad.autoprojekt.model.User;
import com.zijad.autoprojekt.repository.CarRepository;
import com.zijad.autoprojekt.repository.UserRepository;
import com.zijad.autoprojekt.security.JwtService;
import com.zijad.autoprojekt.service.CarService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;


@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CarRepository carRepository;

    @MockBean
    private UserRepository userRepository;



    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testGetAllCars() throws Exception{
        Car car1 = new Car();
        car1.setId(1L);
        car1.setBrand("BMW");

        Car car2 = new Car();
        car2.setId(2L);
        car2.setBrand("Audi");

        when(carService.getAllCars()).thenReturn(List.of(car1, car2));

        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].brand").value("BMW"))
                .andExpect(jsonPath("$[1].brand").value("Audi"));
    }

    @Test
    @WithMockUser(username = "testuser@example.com", roles = {"USER"})
    void testAddCarJson() throws Exception {
        CarRequest carRequest = new CarRequest();
        carRequest.setBrand("Toyota");
        carRequest.setModel("Corolla");
        carRequest.setYear(2020);
        carRequest.setPrice(15000.0);
        carRequest.setFuelType("Gasoline");
        carRequest.setMileage(30000);
        carRequest.setDescription("Pouzdan gradski automobil");

        User mockUser = new User();
        mockUser.setEmail("testuser@example.com");


        when(userRepository.findByEmail("testuser@example.com"))
                .thenReturn(Optional.of(mockUser));

        Car savedCar = new Car();
        savedCar.setId(1L);
        savedCar.setBrand(carRequest.getBrand());
        savedCar.setModel(carRequest.getModel());
        savedCar.setYear(carRequest.getYear());
        savedCar.setPrice(carRequest.getPrice());
        savedCar.setFuelType(carRequest.getFuelType());
        savedCar.setMileage(carRequest.getMileage());
        savedCar.setDescription(carRequest.getDescription());
        savedCar.setUser(mockUser);
        savedCar.setImageUrl(null); // nema slike u ovom testu

        when(carService.addCar(any(CarRequest.class), isNull(), eq("testuser@example.com")))
                .thenReturn(savedCar);

        mockMvc.perform(post("/api/cars/json")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "brand": "Toyota",
                        "model": "Corolla",
                        "year": 2020,
                        "price": 15000,
                        "fuelType": "Gasoline",
                        "mileage": 30000,
                        "description": "Pouzdan gradski automobil"
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.brand").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Corolla"))
                .andExpect(jsonPath("$.year").value(2020))
                .andExpect(jsonPath("$.price").value(15000.0))
                .andExpect(jsonPath("$.fuelType").value("Gasoline"))
                .andExpect(jsonPath("$.mileage").value(30000))
                .andExpect(jsonPath("$.description").value("Pouzdan gradski automobil"))
                .andExpect(jsonPath("$.imageUrl").doesNotExist());

    }

    @Test
    @WithMockUser(username = "testuser@example.com", roles = {"USER"})
    void testAddCarWithImage() throws Exception {

        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "car.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dummy image content".getBytes()
        );


        MockMultipartFile jsonData = new MockMultipartFile(
                "data",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                """
                        {
                                    "brand": "Honda",
                                    "model": "Civic",
                                    "year": 2019,
                                    "price": 18000,
                                    "fuelType": "Petrol",
                                    "mileage": 25000,
                                    "description": "Pouzdan i ekonomičan auto"
                        }
                        """.getBytes()
        );

        User mockUser = new User();
        mockUser.setEmail("testuser@example.com");

        when(userRepository.findByEmail("testuser@example.com"))
                .thenReturn(Optional.of(mockUser));

        Car savedCar = new Car();
        savedCar.setId(1L);
        savedCar.setBrand("Honda");
        savedCar.setModel("Civic");
        savedCar.setYear(2019);
        savedCar.setPrice(18000.0);
        savedCar.setFuelType("Petrol");
        savedCar.setMileage(25000);
        savedCar.setDescription("Pouzdan i ekonomičan auto");
        savedCar.setImageUrl("/uploads/car.jpg");  // Pretpostavimo uploadDir + imageName
        savedCar.setUser(mockUser);

        // Mock servisa koji dodaje auto
        when(carService.addCar(any(CarRequest.class), any(MultipartFile.class), eq("testuser@example.com")))
                .thenReturn(savedCar);

        // Izvrši multipart POST zahtjev
        mockMvc.perform(multipart("/api/cars")
                        .file(jsonData)
                        .file(imageFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("POST"); // jer multipart() defaultno stavlja GET
                            return request;
                        })
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.brand").value("Honda"))
                .andExpect(jsonPath("$.model").value("Civic"))
                .andExpect(jsonPath("$.year").value(2019))
                .andExpect(jsonPath("$.price").value(18000.0))
                .andExpect(jsonPath("$.fuelType").value("Petrol"))
                .andExpect(jsonPath("$.mileage").value(25000))
                .andExpect(jsonPath("$.description").value("Pouzdan i ekonomičan auto"))
                .andExpect(jsonPath("$.imageUrl").value("/uploads/car.jpg"));
    }


}
