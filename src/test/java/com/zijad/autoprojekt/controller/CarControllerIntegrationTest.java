package com.zijad.autoprojekt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zijad.autoprojekt.dto.auth.AuthResponse;
import com.zijad.autoprojekt.dto.auth.LoginRequest;
import com.zijad.autoprojekt.dto.auth.RegisterRequest;
import com.zijad.autoprojekt.dto.car.CarRequest;
import com.zijad.autoprojekt.model.Car;
import com.zijad.autoprojekt.service.CarService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CarService carService;


    @Test
    void shouldReturnAllCars() throws Exception {
        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldAddCarWithoutImage() throws Exception {

        String email = "testusertest@example.com";
        String password = "password123";

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(responseBody, AuthResponse.class);
        String token = authResponse.getToken();

        CarRequest carRequest = new CarRequest();
        carRequest.setBrand("Audi");
        carRequest.setModel("A4");
        carRequest.setPrice(12000.0);
        carRequest.setYear(2020);
        carRequest.setFuelType("Diesel");
        carRequest.setDescription("Test opis");


        mockMvc.perform(multipart("/api/cars")
                .file(new MockMultipartFile("data", "", "application/json", objectMapper.writeValueAsString(carRequest).getBytes()))
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddCarWithImage() throws Exception {

        String email = "testuser" + System.currentTimeMillis() + "@example.com";
        String password = "password124";

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(email);
        registerRequest.setPassword(password);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        AuthResponse authResponse = objectMapper.readValue(responseBody, AuthResponse.class);
        String token = authResponse.getToken();

        CarRequest carRequest = new CarRequest();
        carRequest.setBrand("BMW");
        carRequest.setModel("X5");
        carRequest.setYear(2022);
        carRequest.setPrice(45000.0);
        carRequest.setFuelType("Gasoline");
        carRequest.setMileage(15000);
        carRequest.setDescription("Test auto sa slikom");

        MockMultipartFile jsonPart = new MockMultipartFile(
                "data",
                "data.json",
                "application/json",
                objectMapper.writeValueAsBytes(carRequest)
        );

        MockMultipartFile imagePart = new MockMultipartFile(
                "image",
                "car.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image.content".getBytes()
        );

        mockMvc.perform(multipart("/api/cars")
                .file(jsonPart)
                .file(imagePart)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnCarById() throws Exception {

        CarRequest carRequest = new CarRequest();
        carRequest.setBrand("Toyota");
        carRequest.setModel("Corolla");
        carRequest.setYear(2020);
        carRequest.setPrice(15000.0);
        carRequest.setFuelType("Gasoline");
        carRequest.setMileage(50000);
        carRequest.setDescription("Test auto");

        Car car = carService.addCar(carRequest, null, "testusertest@example.com");

        mockMvc.perform(get("/api/cars/" + car.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(car.getId()));
    }
}
