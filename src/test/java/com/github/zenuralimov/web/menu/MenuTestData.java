package com.github.zenuralimov.web.menu;

import com.github.zenuralimov.model.Menu;
import com.github.zenuralimov.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static com.github.zenuralimov.web.dish.DishTestData.*;

public class MenuTestData {
    public static final MatcherFactory.Matcher<Menu> MENU_MATCHER
            = MatcherFactory.usingIgnoringFieldsComparator(Menu.class, "restaurant", "dishes.menu");

    public static final int MENU1_ID = 1;
    public static final int NOT_FOUND = 100;

    public static final Menu kfc_menu1 = new Menu(MENU1_ID, LocalDate.now().minusDays(1));
    public static final Menu mc_menu1 = new Menu(MENU1_ID + 1, LocalDate.now().minusDays(1));
    public static final Menu king_menu1 = new Menu(MENU1_ID + 2, LocalDate.now().minusDays(1));
    public static final Menu kfc_menu2 = new Menu(MENU1_ID + 3, LocalDate.now());
    public static final Menu mc_menu2 = new Menu(MENU1_ID + 4, LocalDate.now());
    public static final Menu king_menu2 = new Menu(MENU1_ID + 5, LocalDate.now());

    public static final List<Menu> kfcMenu = List.of(kfc_menu2, kfc_menu1);
    public static final List<Menu> mcMenu = List.of(mc_menu2, mc_menu1);
    public static final List<Menu> kingMenu = List.of(king_menu2, king_menu1);

    static {
        kfc_menu1.setDishes(kfc_menu1_dishes);
        mc_menu1.setDishes(mc_menu1_dishes);
        king_menu1.setDishes(king_menu1_dishes);
        kfc_menu2.setDishes(kfc_menu2_dishes);
        mc_menu2.setDishes(mc_menu2_dishes);
        king_menu2.setDishes(king_menu2_dishes);
    }

    public static Menu getNew() {
        return new Menu(null, LocalDate.now().minusDays(2));
    }

    public static Menu getUpdated() {
        return new Menu(MENU1_ID + 3, LocalDate.now());
    }
}
