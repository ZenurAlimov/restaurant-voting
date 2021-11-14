package com.github.ZenurAlimov.repository;

import com.github.ZenurAlimov.error.IllegalRequestDataException;
import com.github.ZenurAlimov.model.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends BaseRepository<Menu> {

    @Query("SELECT m FROM Menu m WHERE m.id=?1 AND m.restaurant.id=?2")
    Optional<Menu> get(int id, int restaurantId);

    @Query("SELECT m FROM Menu m WHERE m.restaurant.id=:restaurantId AND m.date BETWEEN :from AND :to ORDER BY m.date DESC")
    List<Menu> getBetween(int restaurantId, LocalDate from, LocalDate to);

    default Menu checkBelong(int id, int restaurantId) {
        return get(id, restaurantId).orElseThrow(
                () -> new IllegalRequestDataException("Menu id=" + id + " doesn't belong to Restaurant id=" + restaurantId));
    }
}
