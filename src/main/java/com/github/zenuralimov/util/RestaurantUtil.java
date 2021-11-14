package com.github.zenuralimov.util;

import com.github.zenuralimov.model.Restaurant;
import com.github.zenuralimov.to.RestaurantTo;

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
