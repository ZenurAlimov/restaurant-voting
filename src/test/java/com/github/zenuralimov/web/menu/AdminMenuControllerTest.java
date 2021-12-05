package com.github.zenuralimov.web.menu;

import com.github.zenuralimov.model.Menu;
import com.github.zenuralimov.repository.MenuRepository;
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

import static com.github.zenuralimov.web.menu.MenuTestData.*;
import static com.github.zenuralimov.web.restaurant.RestaurantTestData.KFC_ID;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(value = UserTestData.ADMIN_MAIL)
class AdminMenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminMenuController.REST_URL + '/';

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU1_ID, KFC_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(kfc_menu1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND, KFC_ID))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter", KFC_ID)
                .param("from", LocalDate.now().minusDays(1).toString())
                .param("to", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_MATCHER.contentJson(kfcMenu));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MENU1_ID, KFC_ID))
                .andExpect(status().isNoContent());
        assertFalse(menuRepository.get(MENU1_ID, KFC_ID).isPresent());
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND, KFC_ID))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createWithLocation() throws Exception {
        Menu newMenu = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL, KFC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenu)))
                .andDo(print())
                .andExpect(status().isCreated());

        Menu created = MENU_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenu.setId(newId);
        MENU_MATCHER.assertMatch(created, newMenu);
        MENU_MATCHER.assertMatch(menuRepository.get(newId, KFC_ID).orElse(null), newMenu);
    }

    @Test
    void update() throws Exception {
        Menu updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + updated.id(), KFC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MENU_MATCHER.assertMatch(menuRepository.get(updated.id(), KFC_ID).orElse(null), updated);
    }

    @Test
    void createInvalid() throws Exception {
        Menu invalid = new Menu(null, null);
        perform(MockMvcRequestBuilders.post(REST_URL, KFC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateInvalid() throws Exception {
        Menu invalid = new Menu(MENU1_ID, null);
        perform(MockMvcRequestBuilders.put(REST_URL + MENU1_ID, KFC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        Menu invalid = new Menu(null, kfc_menu1.getDate());
        perform(MockMvcRequestBuilders.post(REST_URL, KFC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_MENU)));

    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        Menu invalid = new Menu(MENU1_ID, kfc_menu2.getDate());
        perform(MockMvcRequestBuilders.put(REST_URL + MENU1_ID, KFC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_DUPLICATE_MENU)));

    }
}