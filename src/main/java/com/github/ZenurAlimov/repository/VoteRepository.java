package com.github.ZenurAlimov.repository;

import com.github.ZenurAlimov.model.Vote;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    //    https://stackoverflow.com/a/46013654/548473
    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId ORDER BY v.date DESC")
    List<Vote> getAll(int userId);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.user.id=?1 and v.restaurant.id=?2 ORDER BY v.date DESC")
    List<Vote> getByRestaurant(int userId, int restaurantId);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId AND v.date BETWEEN :from AND :to ORDER BY v.date DESC")
    List<Vote> getBetween(LocalDate from, LocalDate to, int userId);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId and v.date=:date")
    Optional<Vote> getByDate(int userId, LocalDate date);
}
