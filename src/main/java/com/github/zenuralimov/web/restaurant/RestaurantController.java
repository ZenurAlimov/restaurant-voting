package com.github.zenuralimov.web.restaurant;

import com.github.zenuralimov.error.IllegalRequestDataException;
import com.github.zenuralimov.repository.RestaurantRepository;
import com.github.zenuralimov.to.RestaurantTo;
import com.github.zenuralimov.util.CommonMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Tag(name = "Restaurant Controller")
public class RestaurantController {

    static final String REST_URL = "/api/restaurants";

    private final RestaurantRepository repository;
    private final CommonMapper commonMapper;

    @GetMapping("/with-menu")
    @Cacheable("restaurants")
    public List<RestaurantTo> getAllWithMenuToday() {
        log.info("get all restaurants with menu today");
        return commonMapper.getRestaurantTos(repository.getAllByDateWithMenu(LocalDate.now()));
    }

    @GetMapping("/{id}/with-menu")
    public RestaurantTo getByIdWithMenuToday(@PathVariable int id) {
        log.info("get restaurant {} with menu today", id);
        return repository.getByIdAndDateWithMenu(id, LocalDate.now()).map(commonMapper::createTo).orElseThrow(
        () -> new IllegalRequestDataException("Restaurant with id=" + id + " not found"));
    }
}