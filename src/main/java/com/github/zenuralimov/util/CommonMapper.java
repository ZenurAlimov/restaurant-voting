package com.github.zenuralimov.util;

import com.github.zenuralimov.model.Dish;
import com.github.zenuralimov.model.Restaurant;
import com.github.zenuralimov.model.Vote;
import com.github.zenuralimov.to.DishTo;
import com.github.zenuralimov.to.RestaurantTo;
import com.github.zenuralimov.to.VoteTo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CommonMapper {

    DishTo createTo(Dish dish);
    List<DishTo> getDishTos(Collection<Dish> dishes);
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    Dish toEntity(DishTo dishTo);

    RestaurantTo createTo(Restaurant restaurant);
    List<RestaurantTo> getRestaurantTos(Collection<Restaurant> restaurants);

    @Mapping(target = "restaurantId", expression = "java(vote.getRestaurant().id())")
    VoteTo createTo(Vote vote);
    List<VoteTo> getVoteTos(Collection<Vote> votes);
}
