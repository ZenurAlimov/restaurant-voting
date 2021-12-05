package com.github.zenuralimov.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DishTo extends NamedTo {

    Integer price;

    public DishTo(Integer id, String name, Integer price) {
        super(id, name);
        this.price = price;
    }
}
