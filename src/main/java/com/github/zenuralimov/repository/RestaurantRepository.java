package com.github.zenuralimov.repository;

import com.github.zenuralimov.model.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    //    https://stackoverflow.com/a/46013654/548473//
    @EntityGraph(attributePaths = {"menus"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r JOIN FETCH r.menus m WHERE m.date=:date ORDER BY r.name")
    List<Restaurant> getAllByDateWithMenu(LocalDate date);

    @EntityGraph(attributePaths = {"menus"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r JOIN FETCH r.menus m WHERE m.date=:date AND r.id=:id")
    Optional<Restaurant> getByIdAndDateWithMenu(int id, LocalDate date);
}
