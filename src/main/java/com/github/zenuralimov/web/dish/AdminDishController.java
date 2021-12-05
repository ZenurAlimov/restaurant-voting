package com.github.zenuralimov.web.dish;

import com.github.zenuralimov.model.Dish;
import com.github.zenuralimov.model.Menu;
import com.github.zenuralimov.repository.DishRepository;
import com.github.zenuralimov.repository.MenuRepository;
import com.github.zenuralimov.to.DishTo;
import com.github.zenuralimov.util.DishUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.github.zenuralimov.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.zenuralimov.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = "restaurants")
public class AdminDishController {

    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/menus/{menuId}/dishes";

    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;

    @GetMapping("/{id}")
    public Dish get(@PathVariable int restaurantId,
                    @PathVariable int menuId,
                    @PathVariable int id) {
        log.info("get dish {} for menu {} and restaurant {}", id, menuId, restaurantId);
        menuRepository.checkBelong(menuId, restaurantId);
        return dishRepository.checkBelong(id, menuId);
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
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int restaurantId,
                       @PathVariable int menuId,
                       @PathVariable int id) {
        log.info("delete dish {} for menu {} and restaurant {}", id, menuId, restaurantId);
        menuRepository.checkBelong(menuId, restaurantId);
        dishRepository.checkBelong(id, menuId);
        dishRepository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<DishTo> createWithLocation(@Valid @RequestBody DishTo dishTo,
                                                     @PathVariable int restaurantId,
                                                     @PathVariable int menuId) {
        log.info("add dish to menu {} and restaurant {}", menuId, restaurantId);
        checkNew(dishTo);
        Menu menu = menuRepository.checkBelong(menuId, restaurantId);
        Dish dish = DishUtil.rollBackTo(dishTo);
        dish.setMenu(menu);
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, menuId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(DishUtil.createTo(created));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@Valid @RequestBody DishTo dishTo,
                       @PathVariable int restaurantId,
                       @PathVariable int menuId,
                       @PathVariable int id) {
        log.info("update dish for menu {} and restaurant {}", menuId, restaurantId);
        assureIdConsistent(dishTo, id);
        dishRepository.checkBelong(id, menuId);
        Menu menu = menuRepository.checkBelong(menuId, restaurantId);
        Dish dish = DishUtil.rollBackTo(dishTo);
        dish.setMenu(menu);
        dishRepository.save(dish);
    }
}
