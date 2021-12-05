package com.github.zenuralimov.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MenuTo extends BaseTo {

    LocalDate date;

    public MenuTo(Integer id, LocalDate date) {
        super(id);
        this.date = date;
    }
}
