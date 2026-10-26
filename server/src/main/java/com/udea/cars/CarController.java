package com.udea.cars;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarController {

    private static final Set<String> NOT_COOL = Set.of("AMC Gremlin", "Triumph Stag", "Ford Pinto", "Yugo GV");

    private final CarRepository repository;

    public CarController(CarRepository repository) {
        this.repository = repository;
    }

    // La raíz abre el HAL Explorer de Spring Data REST
    @GetMapping("/")
    public ResponseEntity<Void> root() {
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/api")).build();
    }

    @GetMapping("/cool-cars")
    public Collection<Car> coolCars() {
        return repository.findAll().stream()
                .filter(this::isCool)
                .toList();
    }

    @GetMapping("/cars")
    public List<Car> allCars() {
        return repository.findAll();
    }

    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> getCar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/cars")
    public ResponseEntity<Car> createCar(@Valid @RequestBody Car car) {
        car.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(car));
    }

    @PutMapping("/cars/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @Valid @RequestBody Car car) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        car.setId(id);
        return ResponseEntity.ok(repository.save(car));
    }

    private boolean isCool(Car car) {
        return !NOT_COOL.contains(car.getName());
    }
}
