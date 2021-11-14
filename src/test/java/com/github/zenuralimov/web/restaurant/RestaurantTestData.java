package com.github.zenuralimov.web.restaurant;

import com.github.zenuralimov.model.Restaurant;
import com.github.zenuralimov.to.RestaurantTo;
import com.github.zenuralimov.web.MatcherFactory;

import static com.github.zenuralimov.web.menu.MenuTestData.*;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> REST_MATCHER
            = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "menus.restaurant", "menus.dishes.menu");
    public static final MatcherFactory.Matcher<RestaurantTo> REST_TO_MATCHER
            = MatcherFactory.usingIgnoringFieldsComparator(RestaurantTo.class);

    public static final int KFC_ID = 1;
    public static final int MC_ID = 2;
    public static final int KING_ID = 3;
    public static final int NOT_FOUND = 100;

    public static final Restaurant kfc = new Restaurant(KFC_ID, "KFC");
    public static final Restaurant mc = new Restaurant(MC_ID, "McDonalds");
    public static final Restaurant king = new Restaurant(KING_ID, "BurgerKing");

    static {
        kfc.setMenus(kfcMenu);
        mc.setMenus(mcMenu);
        king.setMenus(kingMenu);
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "New restaurant");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(KING_ID, "Updated BurgerKing");
    }
}
