package com.github.zenuralimov.util;

import com.github.zenuralimov.model.Dish;
import com.github.zenuralimov.to.DishTo;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class DishUtil {
    public static List<DishTo> getTos(Collection<Dish> dishes) {
        return dishes.stream()
                .map(DishUtil::createTo)
                .toList();
    }

    public static DishTo createTo(Dish dish) {
        return new DishTo(dish.id(), dish.getName(), dish.getPrice());
    }

    public static Dish toEntity(DishTo dishTo) {
        return new Dish(dishTo.getId(), dishTo.getName(), dishTo.getPrice());
    }
}