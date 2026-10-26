package com.udea.cars;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    private String model;

    private String color;

    // YEAR es palabra reservada en H2
    @Column(name = "car_year")
    @Min(1886)
    @Max(2100)
    private Integer year;

    @PositiveOrZero
    private BigDecimal price;
}
