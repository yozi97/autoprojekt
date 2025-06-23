package com.zijad.autoprojekt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zijad.autoprojekt.model.Car;
import com.zijad.autoprojekt.service.CarService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;


@WebMvcTest(CarController.class)
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllCars() throws Exception {
        Mockito.when(carService.getAllCars()).thenReturn(Arrays.asList(
                new Car(1L, "BMW", "X5", 2020),
                new Car(2L, "Audi", "A4", 2019)
        ));

        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].brand").value("BMW"));
    }

    @Test
    void testAddCar() throws Exception {
        Car car = new Car(null, "VW", "Golf", 2022);
        Car savedCar = new Car(1L, "VW", "Golf", 2022);

        Mockito.when(carService.addCar(any(Car.class))).thenReturn(savedCar);

        mockMvc.perform(post("/api/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.brand").value("VW"));
    }

    @Test
    void testDeleteCarSuccess() throws Exception {
        Mockito.when(carService.deleteCar(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/cars/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteCarNotFound() throws Exception {
        Mockito.when(carService.deleteCar(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/cars/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateCarSuccess() throws Exception {
        Car car = new Car(null, "Renault", "Clio", 2021);
        Mockito.when(carService.updateCar(eq(1L), any(Car.class))).thenReturn(true);

        mockMvc.perform(put("/api/cars/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateCarNotFound() throws Exception {
        Car car = new Car(null, "Toyota", "Yaris", 2020);
        Mockito.when(carService.updateCar(eq(999L), any(Car.class))).thenReturn(false);

        mockMvc.perform(put("/api/cars/999")
                        .with(httpBasic("zijad", "mySecretPass"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(car)))
                .andExpect(status().isNotFound());
    }


}
