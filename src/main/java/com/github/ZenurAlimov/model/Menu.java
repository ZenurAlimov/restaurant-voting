package com.github.ZenurAlimov.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Menu extends BaseEntity {

    private LocalDate date;
}
