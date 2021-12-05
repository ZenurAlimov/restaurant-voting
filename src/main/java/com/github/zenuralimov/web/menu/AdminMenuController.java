package com.github.zenuralimov.web.menu;

import com.github.zenuralimov.error.IllegalRequestDataException;
import com.github.zenuralimov.model.Menu;
import com.github.zenuralimov.repository.MenuRepository;
import com.github.zenuralimov.repository.RestaurantRepository;
import com.github.zenuralimov.to.MenuTo;
import com.github.zenuralimov.util.MenuUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static com.github.zenuralimov.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.zenuralimov.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@CacheConfig(cacheNames = "restaurants")
public class AdminMenuController {

    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/menus";

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    @GetMapping("/{id}")
    public MenuTo get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get menu {} for restaurant {}", id, restaurantId);
        Menu menu = menuRepository.checkBelong(id, restaurantId);
        return MenuUtil.createTo(menu);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete menu {} for restaurant {}", id, restaurantId);
        menuRepository.checkBelong(id, restaurantId);
        menuRepository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<MenuTo> createWithLocation(@Valid @RequestBody MenuTo menuTo, @PathVariable int restaurantId) {
        log.info("add menu to restaurant {}", restaurantId);
        checkNew(menuTo);
        Menu menu = MenuUtil.rollBackTo(menuTo);
        menu.setRestaurant(restaurantRepository.getById(restaurantId));
        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(MenuUtil.createTo(created));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@Valid @RequestBody MenuTo menuTo, @PathVariable int restaurantId, @PathVariable int id) {
        log.info("update menu {} for restaurant {}", id, restaurantId);
        assureIdConsistent(menuTo, id);
        menuRepository.checkBelong(id, restaurantId);
        Menu menu = MenuUtil.rollBackTo(menuTo);
        menu.setRestaurant(restaurantRepository.getById(restaurantId));
        menuRepository.save(menu);
    }
}
