package com.github.zenuralimov.web.restaurant;

import com.github.zenuralimov.error.IllegalRequestDataException;
import com.github.zenuralimov.model.Restaurant;
import com.github.zenuralimov.repository.RestaurantRepository;
import com.github.zenuralimov.to.RestaurantTo;
import com.github.zenuralimov.util.RestaurantUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.github.zenuralimov.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.zenuralimov.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = "restaurants")
public class AdminRestaurantController {

    static final String REST_URL = "/api/admin/restaurants";

    private final RestaurantRepository repository;

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {

        log.info("get restaurant {}", id);
        return repository.findById(id).map(RestaurantUtil::createTo).orElseThrow(
                () -> new IllegalRequestDataException("Entity with id=" + id + " not found"));
    }

    @GetMapping
    public List<RestaurantTo> getAll() {
        log.info("get all restaurants");
        return RestaurantUtil.getTos(repository.findAll(Sort.by(Sort.Direction.ASC, "name")));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant {}", id);
        repository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<RestaurantTo> createWithLocation(@Valid @RequestBody RestaurantTo restaurantTo) {
        log.info("create restaurant");
        checkNew(restaurantTo);
        Restaurant restaurant = RestaurantUtil.rollBackTo(restaurantTo);
        Restaurant created = repository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(RestaurantUtil.createTo(created));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@Valid @RequestBody RestaurantTo restaurantTo, @PathVariable int id) {
        log.info("update restaurant {}", id);
        assureIdConsistent(restaurantTo, id);
        Restaurant restaurant = RestaurantUtil.rollBackTo(restaurantTo);
        repository.save(restaurant);
    }
}
