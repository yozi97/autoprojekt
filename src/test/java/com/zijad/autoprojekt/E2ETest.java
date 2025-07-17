package com.zijad.autoprojekt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zijad.autoprojekt.dto.auth.LoginRequest;
import com.zijad.autoprojekt.dto.auth.RegisterRequest;
import com.zijad.autoprojekt.dto.car.CarRequest;
import com.zijad.autoprojekt.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class E2ETest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;


    @Test
    void testFullFlow_registerLoginAddCarAndFetch() throws Exception {

        String email = "e2euser@example.com";
        String password = "test123";

        var registerBody = new RegisterRequest();
        registerBody.setEmail(email);
        registerBody.setPassword(password);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerBody)))
                .andExpect(status().isOk());

        var loginBody = new LoginRequest();
        loginBody.setEmail(email);
        loginBody.setPassword(password);

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginBody)))
                .andExpect(status().isOk())
                .andReturn();

        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .get("token").asText();

        var carRequest = new CarRequest();
        carRequest.setBrand("Audi");
        carRequest.setModel("A4");
        carRequest.setPrice(12000.0);
        carRequest.setYear(2020);
        carRequest.setFuelType("Diesel");
        carRequest.setDescription("Test opis");


        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(carRequest)))
                .andExpect(status().isOk());

        MvcResult carsResult = mockMvc.perform(get("/api/cars/user")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = carsResult.getResponse().getContentAsString();

        assertThat(responseBody).contains("Audi");
        assertThat(responseBody).contains("A4");
    }
}
