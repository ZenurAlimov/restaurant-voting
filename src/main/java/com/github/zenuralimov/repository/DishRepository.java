package com.github.zenuralimov.repository;

import com.github.zenuralimov.error.IllegalRequestDataException;
import com.github.zenuralimov.model.Dish;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.id=?1 AND d.restaurant.id=?2")
    Optional<Dish> get(int id, int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=?1 AND d.date=?2 ORDER BY d.price DESC")
    List<Dish> getAllByDate(int restaurantId, LocalDate date);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id=?1 ORDER BY d.date DESC, d.price DESC")
    List<Dish> getAll(int restaurantId);

    default Dish checkBelong(int id, int restaurantId) {
        return get(id, restaurantId).orElseThrow(
                () -> new IllegalRequestDataException("Dish id=" + id + " doesn't belong to Restaurant id=" + restaurantId));
    }
}