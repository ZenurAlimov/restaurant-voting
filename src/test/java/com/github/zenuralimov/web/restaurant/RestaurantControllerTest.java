package com.github.zenuralimov.web.restaurant;

import com.github.zenuralimov.util.RestaurantUtil;
import com.github.zenuralimov.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.github.zenuralimov.web.dish.DishTestData.*;
import static com.github.zenuralimov.web.restaurant.RestaurantTestData.*;
import static com.github.zenuralimov.web.user.UserTestData.USER_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(value = USER_MAIL)
class RestaurantControllerTest extends AbstractControllerTest {

    private static final String REST_URL = RestaurantController.REST_URL + '/';

    @Test
    void getByIdWithMenuToday() throws Exception {
        mc.setDishes(mc_menu2_dishes);

        perform(MockMvcRequestBuilders.get(REST_URL + MC_ID + "/with-menu"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REST_TO_MATCHER.contentJson(RestaurantUtil.createTo(mc)));
    }

    @Test
    void getAllWithMenuToday() throws Exception {
        kfc.setDishes(kfc_menu2_dishes);
        mc.setDishes(mc_menu2_dishes);
        king.setDishes(king_menu2_dishes);

        perform(MockMvcRequestBuilders.get(REST_URL + "with-menu"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(REST_TO_MATCHER.contentJson(RestaurantUtil.getTos(List.of(king, kfc, mc))));
    }
}