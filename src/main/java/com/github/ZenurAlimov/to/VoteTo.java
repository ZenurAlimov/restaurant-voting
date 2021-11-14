package com.github.ZenurAlimov.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VoteTo extends BaseTo {
    LocalDate date;

    int restaurantId;

    String restaurantName;

    public VoteTo(Integer id, LocalDate date, int restaurantId, String restaurantName) {
        super(id);
        this.date = date;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
    }
}
