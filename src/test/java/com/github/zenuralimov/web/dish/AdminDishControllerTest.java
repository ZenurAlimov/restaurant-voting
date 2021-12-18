package com.github.zenuralimov.web.dish;

import com.github.zenuralimov.model.Dish;
import com.github.zenuralimov.repository.DishRepository;
import com.github.zenuralimov.util.DishUtil;
import com.github.zenuralimov.util.JsonUtil;
import com.github.zenuralimov.web.AbstractControllerTest;
import com.github.zenuralimov.web.GlobalExceptionHandler;
import com.github.zenuralimov.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.github.zenuralimov.web.dish.DishTestData.*;
import static com.github.zenuralimov.web.restaurant.RestaurantTestData.KFC_ID;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(value = UserTestData.ADMIN_MAIL)
class AdminDishControllerTest extends AbstractControllerTest {

    static final String REST_URL = AdminDishController.REST_URL + '/';

    @Autowired
    private DishRepository dishRepository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH1_ID, KFC_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(kfc_menu1_dish1));
    }

    @Test
    void getAllByRestaurantAndDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-date", KFC_ID)
                .param("date", LocalDate.now().minusDays(1).toString()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_TO_MATCHER.contentJson(DishUtil.getTos(kfc_menu1_dishes)));
    }

    @Test
    void getAllByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL, KFC_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(
                        List.of(kfc_menu2_dish1, kfc_menu2_dish2, kfc_menu2_dish4, kfc_menu2_dish3, kfc_menu1_dish1, kfc_menu1_dish2, kfc_menu1_dish3, kfc_menu1_dish4)));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH13_ID, KFC_ID))
                .andExpect(status().isNoContent());
        assertFalse(dishRepository.get(DISH1_ID + 12, KFC_ID).isPresent());
    }

    @Test
    void deleteProhibit() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH1_ID, KFC_ID))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createWithLocation() throws Exception {
        Dish newDish = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL, KFC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isCreated());

        Dish created = DISH_MATCHER.readFromJson(action);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(dishRepository.get(newId, KFC_ID).orElse(null), newDish);
    }

    @Test
    void update() throws Exception {
        Dish updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + DISH13_ID, KFC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(dishRepository.get(DISH1_ID + 12, KFC_ID).orElse(null), updated);
    }

    @Test
    void createInvalid() throws Exception {
        Dish invalid = new Dish(null, null,100);
        perform(MockMvcRequestBuilders.post(REST_URL, KFC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateInvalid() throws Exception {
        Dish invalid = new Dish(DISH1_ID + 12, null, 300);
        perform(MockMvcRequestBuilders.put(REST_URL + DISH13_ID, KFC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        Dish invalid = new Dish(null, kfc_menu2_dish1.getName(), 300);
        perform(MockMvcRequestBuilders.post(REST_URL, KFC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_DISH)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        Dish invalid = new Dish(DISH1_ID + 12, kfc_menu2_dish2.getName(), 200);
        perform(MockMvcRequestBuilders.put(REST_URL + DISH13_ID, KFC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_DISH)));
    }
}