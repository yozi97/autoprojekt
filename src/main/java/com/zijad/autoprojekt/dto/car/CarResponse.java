package com.zijad.autoprojekt.dto.car;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarResponse {

    private Long id;
    private String brand;
    private String model;
    private int year;
    private double price;
    private String fuelType;
    private int mileage;
    private String description;
    private String imageUrl;
}
