package com.github.zenuralimov.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RestaurantTo extends NamedTo {

    List<DishTo> dishes;

    public RestaurantTo(Integer id, String name, List<DishTo> dishes) {
        super(id, name);
        this.dishes = dishes;
    }
}