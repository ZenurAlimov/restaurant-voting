package com.github.zenuralimov.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDate;

@Value
@EqualsAndHashCode
@ToString(callSuper = true)
public class VoteTo {

    LocalDate date;

    int restaurantId;

    public VoteTo(LocalDate date, int restaurantId) {
        this.date = date;
        this.restaurantId = restaurantId;
    }
}