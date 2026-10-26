package com.udea.cars;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// Expuesto por Spring Data REST bajo /api/cars (ver application.properties)
@RepositoryRestResource
public interface CarRepository extends JpaRepository<Car, Long> {

}
