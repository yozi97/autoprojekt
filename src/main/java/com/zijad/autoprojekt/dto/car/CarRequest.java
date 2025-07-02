package com.zijad.autoprojekt.dto.car;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CarRequest {
    private String brand;
    private String model;
    private Integer year;
    private Double price;
    private String fuelType;
    private Integer mileage;
    private String description;

}
