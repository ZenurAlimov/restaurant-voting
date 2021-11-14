package com.github.ZenurAlimov.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Dish extends NamedEntity {

    private Integer price;
}
