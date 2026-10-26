package com.udea.cars;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CarsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarsApplication.class, args);
    }

    @Bean
    ApplicationRunner init(CarRepository repository) {
        return args -> {
            Stream.of(
                    new Car(null, "Ferrari", "F8 Tributo", "Rojo", 2021, new BigDecimal("280000")),
                    new Car(null, "Jaguar Car", "F-Type", "Verde", 2020, new BigDecimal("70000")),
                    new Car(null, "Porsche", "911 Carrera", "Plata", 2022, new BigDecimal("115000")),
                    new Car(null, "Lamborghini", "Huracan", "Amarillo", 2021, new BigDecimal("260000")),
                    new Car(null, "Bugatti", "Chiron", "Azul", 2019, new BigDecimal("3000000")),
                    new Car(null, "AMC Gremlin", "X", "Naranja", 1972, new BigDecimal("2000")),
                    new Car(null, "Triumph Stag", "Mk II", "Blanco", 1975, new BigDecimal("15000")),
                    new Car(null, "Ford Pinto", "Runabout", "Marron", 1974, new BigDecimal("1800")),
                    new Car(null, "Yugo GV", "GV", "Gris", 1987, new BigDecimal("1500"))
            ).forEach(repository::save);
            repository.findAll().forEach(System.out::println);
        };
    }
}
