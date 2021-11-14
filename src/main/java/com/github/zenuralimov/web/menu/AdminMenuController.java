package com.github.zenuralimov.web.menu;

import com.github.zenuralimov.model.Menu;
import com.github.zenuralimov.repository.MenuRepository;
import com.github.zenuralimov.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.github.zenuralimov.util.DateUtil.dayOrMax;
import static com.github.zenuralimov.util.DateUtil.dayOrMin;
import static com.github.zenuralimov.util.validation.ValidationUtil.assureIdConsistent;
import static com.github.zenuralimov.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminMenuController {

    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/menu";

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Menu> get(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("get menu {} for restaurant {}", id, restaurantId);
        menuRepository.checkBelong(id, restaurantId);
        return ResponseEntity.of(menuRepository.get(id, restaurantId));
    }

    @GetMapping("/filter")
    public List<Menu> getBetween(@PathVariable int restaurantId,
                                 @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                 @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        log.info("get All menu for restaurant {} between dates({} - {})", restaurantId, from, to);
        return menuRepository.getBetween(restaurantId, dayOrMin(from), dayOrMax(to));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int restaurantId, @PathVariable int id) {
        log.info("delete menu {} for restaurant {}", id, restaurantId);
        menuRepository.checkBelong(id, restaurantId);
        menuRepository.delete(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> createWithLocation(@Valid @RequestBody Menu menu, @PathVariable int restaurantId) {
        log.info("add menu to restaurant {}", restaurantId);
        checkNew(menu);
        menu.setRestaurant(restaurantRepository.getById(restaurantId));
        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Menu menu, @PathVariable int restaurantId, @PathVariable int id) {
        log.info("update menu {} for restaurant {}", id, restaurantId);
        assureIdConsistent(menu, id);
        menu.setRestaurant(restaurantRepository.getById(restaurantId));
        menuRepository.save(menu);
    }
}
