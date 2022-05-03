package com.github.zenuralimov.web.dish;

import com.github.zenuralimov.error.IllegalRequestDataException;
import com.github.zenuralimov.model.Dish;
import com.github.zenuralimov.repository.DishRepository;
import com.github.zenuralimov.repository.RestaurantRepository;
import com.github.zenuralimov.to.DishTo;
import com.github.zenuralimov.util.CommonMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.github.zenuralimov.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.zenuralimov.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = "restaurants")
public class AdminDishController {

    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/dishes";

    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;
    private final CommonMapper commonMapper;

    @GetMapping("/{id}")
    public Dish get(@PathVariable int id,
                    @PathVariable int restaurantId) {
        log.info("get dish {} for restaurant {}", id, restaurantId);
        return dishRepository.checkBelong(id, restaurantId);
    }

    @GetMapping("/by-date")
    public List<DishTo> getAllByRestaurantAndDate(@PathVariable int restaurantId,
                             @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        log.info("get All dishes for restaurant {} by date {}", restaurantId, date);
        return commonMapper.getDishTos(dishRepository.getAllByDate(restaurantId, date == null ? LocalDate.now() : date));
    }

    @GetMapping
    public List<Dish> getAllByRestaurant(@PathVariable int restaurantId) {
        log.info("get All dishes for restaurant {}", restaurantId);
        return dishRepository.getAll(restaurantId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int id,
                       @PathVariable int restaurantId) {
        log.info("delete dish {} for restaurant {}", id, restaurantId);
        Dish dish = dishRepository.checkBelong(id, restaurantId);
        if (!dish.getDate().equals(LocalDate.now())) {
            throw new IllegalRequestDataException("Can not delete food for the past days");
        }
        dishRepository.deleteExisted(id);
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody DishTo dishTo,
                                                   @PathVariable int restaurantId) {
        log.info("add dish {} to restaurant {}", dishTo, restaurantId);
        checkNew(dishTo);
        Dish dish = commonMapper.toEntity(dishTo);
        dish.setRestaurant(restaurantRepository.getById(restaurantId));
        dish.setDate(LocalDate.now());
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Transactional
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@Valid @RequestBody DishTo dishTo,
                       @PathVariable int id,
                       @PathVariable int restaurantId) {
        log.info("update dish {} for restaurant {}", id, restaurantId);
        assureIdConsistent(dishTo, id);
        dishRepository.checkBelong(id, restaurantId);
        Dish dish = commonMapper.toEntity(dishTo);
        dish.setRestaurant(restaurantRepository.getById(restaurantId));
        dish.setDate(LocalDate.now());
        dishRepository.save(dish);
    }
}