package com.github.ZenurAlimov.model;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "dish", uniqueConstraints = @UniqueConstraint(columnNames = {"menu_id", "name"}, name = "dish_unique_menu_name_idx"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Dish extends NamedEntity {

    @Column(name = "price", nullable = false, columnDefinition = "int")
    @NotNull
    @Range(min = 10, max = 1000)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @ToString.Exclude
    private Menu menu;
}
