package com.github.ZenurAlimov.web.dish;

import com.github.ZenurAlimov.model.Dish;
import com.github.ZenurAlimov.repository.DishRepository;
import com.github.ZenurAlimov.util.JsonUtil;
import com.github.ZenurAlimov.web.AbstractControllerTest;
import com.github.ZenurAlimov.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.github.ZenurAlimov.web.dish.DishTestData.*;
import static com.github.ZenurAlimov.web.menu.MenuTestData.MENU1_ID;
import static com.github.ZenurAlimov.web.restaurant.RestaurantTestData.KFC_ID;
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
        perform(MockMvcRequestBuilders.get(REST_URL + DISH1_ID, KFC_ID, MENU1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(kfc_menu1_dish1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DISH1_ID + 7, KFC_ID, MENU1_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL, KFC_ID, MENU1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(kfc_menu1_dishes));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH1_ID, KFC_ID, MENU1_ID))
                .andExpect(status().isNoContent());
        assertFalse(dishRepository.get(DISH1_ID, MENU1_ID).isPresent());
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND, KFC_ID, MENU1_ID))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createWithLocation() throws Exception {
        Dish newDish = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL, KFC_ID, MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish)))
                .andDo(print())
                .andExpect(status().isCreated());

        Dish created = DISH_MATCHER.readFromJson(action);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(dishRepository.get(newId, MENU1_ID).orElse(null), newDish);
    }

    @Test
    void update() throws Exception {
        Dish updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + DISH1_ID, KFC_ID, MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(dishRepository.get(DISH1_ID, MENU1_ID).orElse(null), updated);
    }

    @Test
    void createInvalid() throws Exception {
        Dish invalid = new Dish(null, null,100);
        perform(MockMvcRequestBuilders.post(REST_URL, KFC_ID, MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateInvalid() throws Exception {
        Dish invalid = new Dish(DISH1_ID, null, 300);
        perform(MockMvcRequestBuilders.put(REST_URL + DISH1_ID, KFC_ID, MENU1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() {
        Dish invalid = new Dish(null, kfc_menu1_dish1.getName(), 300);
        assertThrows(Exception.class, () ->
                perform(MockMvcRequestBuilders.post(REST_URL, KFC_ID, MENU1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(invalid)))
                        .andDo(print())
                        .andExpect(status().isUnprocessableEntity())
        );
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() {
        Dish invalid = new Dish(DISH1_ID, kfc_menu1_dish2.getName(), 200);
        assertThrows(Exception.class, () ->
                perform(MockMvcRequestBuilders.put(REST_URL + DISH1_ID, KFC_ID, MENU1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(invalid)))
                        .andDo(print())
        );
    }
}