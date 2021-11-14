package com.github.zenuralimov.web.dish;

import com.github.zenuralimov.model.Dish;
import com.github.zenuralimov.web.MatcherFactory;

import java.util.List;

public class DishTestData {

    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER
            = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "menu");

    public static final int DISH1_ID = 1;
    public static final int NOT_FOUND = 100;

    public static final Dish kfc_menu1_dish1 = new Dish(DISH1_ID, "Chicken wings", 120);
    public static final Dish kfc_menu1_dish2 = new Dish(DISH1_ID + 1, "Burger", 120);
    public static final Dish kfc_menu1_dish3 = new Dish(DISH1_ID + 2, "Coke", 90);
    public static final Dish kfc_menu1_dish4 = new Dish(DISH1_ID + 3, "Sauce", 30);
    public static final Dish mc_menu1_dish1 = new Dish(DISH1_ID + 4, "Cheeseburger", 120);
    public static final Dish mc_menu1_dish2 = new Dish(DISH1_ID + 5, "Mustard", 30);
    public static final Dish mc_menu1_dish3 = new Dish(DISH1_ID + 6, "Chips", 90);
    public static final Dish mc_menu1_dish4 = new Dish(DISH1_ID + 7, "Soda", 70);
    public static final Dish king_menu1_dish1 = new Dish(DISH1_ID + 8, "Buffalo wings", 120);
    public static final Dish king_menu1_dish2 = new Dish(DISH1_ID + 9, "French fries", 130);
    public static final Dish king_menu1_dish3 = new Dish(DISH1_ID + 10, "Mayo", 30);
    public static final Dish king_menu1_dish4 = new Dish(DISH1_ID + 11, "Ice cream", 80);

    public static final Dish kfc_menu2_dish1 = new Dish(DISH1_ID + 12, "Hamburger", 130);
    public static final Dish kfc_menu2_dish2 = new Dish(DISH1_ID + 13, "Hot-dog", 110);
    public static final Dish kfc_menu2_dish3 = new Dish(DISH1_ID + 14, "Mayonnaise", 30);
    public static final Dish kfc_menu2_dish4 = new Dish(DISH1_ID + 15, "Milkshake", 80);
    public static final Dish mc_menu2_dish1 = new Dish(DISH1_ID + 16, "Nachos with cheese", 110);
    public static final Dish mc_menu2_dish2 = new Dish(DISH1_ID + 17, "Onion rings", 80);
    public static final Dish mc_menu2_dish3 = new Dish(DISH1_ID + 18, "Pizza", 100);
    public static final Dish mc_menu2_dish4 = new Dish(DISH1_ID + 19, "Popcorn", 50);
    public static final Dish king_menu2_dish1 = new Dish(DISH1_ID + 20, "Fish and chips", 120);
    public static final Dish king_menu2_dish2 = new Dish(DISH1_ID + 21, "Sandwich", 110);
    public static final Dish king_menu2_dish3 = new Dish(DISH1_ID + 22, "Sauce", 50);
    public static final Dish king_menu2_dish4 = new Dish(DISH1_ID + 23, "Wrap", 120);

    public static final List<Dish> kfc_menu1_dishes = List.of(kfc_menu1_dish1, kfc_menu1_dish2, kfc_menu1_dish3, kfc_menu1_dish4);
    public static final List<Dish> mc_menu1_dishes = List.of(mc_menu1_dish1, mc_menu1_dish2, mc_menu1_dish3, mc_menu1_dish4);
    public static final List<Dish> king_menu1_dishes = List.of(king_menu1_dish1, king_menu1_dish2, king_menu1_dish3, king_menu1_dish4);
    public static final List<Dish> kfc_menu2_dishes = List.of(kfc_menu2_dish1, kfc_menu2_dish2, kfc_menu2_dish3, kfc_menu2_dish4);
    public static final List<Dish> mc_menu2_dishes = List.of(mc_menu2_dish1, mc_menu2_dish2, mc_menu2_dish3, mc_menu2_dish4);
    public static final List<Dish> king_menu2_dishes = List.of(king_menu2_dish1, king_menu2_dish2, king_menu2_dish3, king_menu2_dish4);

    public static Dish getNew() {
        return new Dish(null, "New Dish1", 200);
    }

    public static Dish getUpdated() {
        return new Dish(DISH1_ID, "Updated Dish", 300);

    }
}
