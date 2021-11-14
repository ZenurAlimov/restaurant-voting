package com.github.ZenurAlimov.web.dish;

import com.github.ZenurAlimov.model.Dish;
import com.github.ZenurAlimov.repository.DishRepository;
import com.github.ZenurAlimov.repository.MenuRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.github.ZenurAlimov.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.ZenurAlimov.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminDishController {

    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/menu/{menuId}/dishes";

    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Dish> get(@PathVariable int restaurantId,
                                    @PathVariable int menuId,
                                    @PathVariable int id) {
        log.info("get dish {} for menu {} and restaurant {}", id, menuId, restaurantId);
        menuRepository.checkBelong(menuId, restaurantId);
        dishRepository.checkBelong(id, menuId);
        return ResponseEntity.of(dishRepository.get(id, menuId));
    }

    @GetMapping
    public List<Dish> getAll(@PathVariable int restaurantId,
                             @PathVariable int menuId) {
        log.info("get All dishes for menu {} and restaurant {}", menuId, restaurantId);
        menuRepository.checkBelong(menuId, restaurantId);
        return dishRepository.getAll(menuId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId,
                       @PathVariable int menuId,
                       @PathVariable int id) {
        log.info("delete dish {} for menu {} and restaurant {}", id, menuId, restaurantId);
        menuRepository.checkBelong(menuId, restaurantId);
        dishRepository.checkBelong(id, menuId);
        dishRepository.delete(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish,
                                                   @PathVariable int restaurantId,
                                                   @PathVariable int menuId) {
        log.info("add dish to menu {} and restaurant {}", menuId, restaurantId);
        checkNew(dish);
        menuRepository.checkBelong(menuId, restaurantId);
        dish.setMenu(menuRepository.get(menuId, restaurantId).orElse(null));
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, menuId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish,
                       @PathVariable int restaurantId,
                       @PathVariable int menuId,
                       @PathVariable int id) {
        log.info("update dish for menu {} and restaurant {}", menuId, restaurantId);
        assureIdConsistent(dish, id);
        menuRepository.checkBelong(menuId, restaurantId);
        dishRepository.checkBelong(id, menuId);
        dish.setMenu(menuRepository.get(menuId, restaurantId).orElse(null));
        dishRepository.save(dish);
    }
}
