package com.github.zenuralimov.repository;

import com.github.zenuralimov.error.IllegalRequestDataException;
import com.github.zenuralimov.model.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {

    @Query("SELECT m FROM Menu m WHERE m.id=?1 AND m.restaurant.id=?2")
    Optional<Menu> get(int id, int restaurantId);

    default Menu checkBelong(int id, int restaurantId) {
        return get(id, restaurantId).orElseThrow(
                () -> new IllegalRequestDataException("Menu id=" + id + " doesn't belong to Restaurant id=" + restaurantId));
    }
}
