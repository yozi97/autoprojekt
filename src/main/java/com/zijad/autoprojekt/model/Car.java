package com.zijad.autoprojekt.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Marka je obavezna!")
    private String brand;
    @NotBlank(message = "Model je obavezna!")
    private String model;
    @Min(value = 1900, message = "Godina mora biti najmanje 1900!")
    private Integer year;
    private Double price;
    private String fuelType;
    private Integer mileage = 0;
    private String description;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Car() {}

    public Car(Long id, String brand, String model, int year) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

}
