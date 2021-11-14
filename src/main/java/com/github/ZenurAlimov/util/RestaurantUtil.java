package com.github.ZenurAlimov.util;

import com.github.ZenurAlimov.model.Restaurant;
import com.github.ZenurAlimov.to.RestaurantTo;

import java.util.Collection;
import java.util.List;

public class RestaurantUtil {
    public static List<RestaurantTo> getTos(Collection<Restaurant> restaurants) {
        return restaurants.stream()
                .map(RestaurantUtil::createTo)
                .toList();
    }

    public static RestaurantTo createTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.id(), restaurant.getName());
    }
}
