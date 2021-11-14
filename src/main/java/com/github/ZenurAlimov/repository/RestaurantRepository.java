package com.github.ZenurAlimov.repository;

import com.github.ZenurAlimov.model.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {
    //    https://stackoverflow.com/a/46013654/548473//
    @EntityGraph(attributePaths = {"menus"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT r FROM Restaurant r JOIN FETCH r.menus m WHERE m.date=:date")
    List<Restaurant> getAllByDate(LocalDate date);

    @Query("SELECT r FROM Restaurant r JOIN FETCH r.menus m WHERE m.date=:date AND r.id=:id")
    Restaurant getByIdWithDate(int id, LocalDate date);
}
