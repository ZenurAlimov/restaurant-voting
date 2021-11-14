package com.github.ZenurAlimov.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Vote extends BaseEntity {

    private LocalDate date;
}
