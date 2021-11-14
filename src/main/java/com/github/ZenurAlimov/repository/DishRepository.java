package com.github.ZenurAlimov.repository;

import com.github.ZenurAlimov.error.IllegalRequestDataException;
import com.github.ZenurAlimov.model.Dish;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Query("SELECT d FROM Dish d WHERE d.id=?1 AND d.menu.id=?2")
    Optional<Dish> get(int id, int menuId);

    @Query("SELECT d FROM Dish d WHERE d.menu.id=:menuId")
    List<Dish> getAll(int menuId);

    default Dish checkBelong(int id, int menuId) {
        return get(id, menuId).orElseThrow(
                () -> new IllegalRequestDataException("Dish id=" + id + " doesn't belong to Menu id=" + menuId));
    }
}