package com.github.ZenurAlimov.web.restaurant;

import com.github.ZenurAlimov.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.github.ZenurAlimov.web.menu.MenuTestData.*;
import static com.github.ZenurAlimov.web.restaurant.RestaurantTestData.*;
import static com.github.ZenurAlimov.web.user.UserTestData.USER_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(value = USER_MAIL)
class RestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = RestaurantController.REST_URL + '/';

    @Test
    void getByIdWithMenuToday() throws Exception {
        mc.setMenus(List.of(mc_menu2));

        perform(MockMvcRequestBuilders.get(REST_URL + MC_ID + "/with-menu"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REST_MATCHER.contentJson(mc));
    }

    @Test
    void getAllWithMenuToday() throws Exception {
        kfc.setMenus(List.of(kfc_menu2));
        mc.setMenus(List.of(mc_menu2));
        king.setMenus(List.of(king_menu2));

        perform(MockMvcRequestBuilders.get(REST_URL + "with-menu"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REST_MATCHER.contentJson(List.of(kfc, mc, king)));
    }
}