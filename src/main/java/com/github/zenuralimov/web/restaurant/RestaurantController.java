package com.github.zenuralimov.web.restaurant;

import com.github.zenuralimov.error.IllegalRequestDataException;
import com.github.zenuralimov.model.Restaurant;
import com.github.zenuralimov.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RestaurantController {

    static final String REST_URL = "/api/restaurants";

    private final RestaurantRepository repository;

    @GetMapping("/with-menu")
    @Cacheable("restaurants")
    public List<Restaurant> getAllWithMenuToday() {
        log.info("get all restaurants with menu today");
        return repository.getAllByDateWithMenu(LocalDate.now());
    }

    @GetMapping("/{id}/with-menu")
    public Restaurant getByIdWithMenuToday(@PathVariable int id) {
        log.info("get restaurant {} with menu today", id);
        return repository.getByIdAndDateWithMenu(id, LocalDate.now()).orElseThrow(
        () -> new IllegalRequestDataException("Entity with id=" + id + " not found"));
    }
}
