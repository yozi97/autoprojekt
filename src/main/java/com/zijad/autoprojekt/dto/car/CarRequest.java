package com.zijad.autoprojekt.dto.car;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CarRequest {
    private String brand;
    private String model;
    private int year;
    private double price;
    private String fuelType;
    private int mileage;
    private String description;

}
