package com.github.zenuralimov.util;

import com.github.zenuralimov.model.Menu;
import com.github.zenuralimov.to.MenuTo;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class MenuUtil {
    public static List<MenuTo> getTos(Collection<Menu> menus) {
        return menus.stream()
                .map(MenuUtil::createTo)
                .toList();
    }

    public static MenuTo createTo(Menu menu) {
        return new MenuTo(menu.id(), menu.getDate());
    }

    public static Menu rollBackTo(MenuTo menuTo) {
        return new Menu(menuTo.getId(), menuTo.getDate());
    }
}