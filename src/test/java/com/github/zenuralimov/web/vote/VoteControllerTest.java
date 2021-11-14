package com.github.zenuralimov.web.vote;

import com.github.zenuralimov.model.Vote;
import com.github.zenuralimov.repository.VoteRepository;
import com.github.zenuralimov.util.VoteUtil;
import com.github.zenuralimov.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.github.zenuralimov.util.JsonUtil.writeValue;
import static com.github.zenuralimov.web.restaurant.RestaurantTestData.*;
import static com.github.zenuralimov.web.user.UserTestData.*;
import static com.github.zenuralimov.web.vote.VoteTestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {
    public static final LocalDate TODAY = LocalDate.now();

    private static final String REST_URL = VoteController.REST_URL + '/';

    @Autowired
    private VoteRepository repository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(VoteUtil.getTos(userVotes)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getAllByRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-restaurantId" + "?restaurantId=" + KFC_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(VOTE_TO_MATCHER.contentJson(VoteUtil.getTos(List.of(vote1))));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("from", TODAY.toString())
                .param("to", TODAY.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(VOTE_TO_MATCHER.contentJson(VoteUtil.getTos(List.of(vote2))));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Vote newVote = VoteTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "?restaurantId=" + KING_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newVote)));

        Vote created = VoteTestData.VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        VoteTestData.VOTE_MATCHER.assertMatch(created, newVote);
        VoteTestData.VOTE_MATCHER.assertMatch(repository.getById(newId), newVote);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateVote() throws Exception {
        Vote updated = VoteTestData.getUpdated();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "?restaurantId=" + MC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)));

        if (LocalTime.now().isAfter(LocalTime.of(11, 0))) {
            action.andExpect(status().isUnprocessableEntity());
        } else {
            action.andDo(print()).andExpect(status().isNoContent());
            VoteTestData.VOTE_MATCHER.assertMatch(repository.getByDate(USER_ID, TODAY).orElse(null), VoteTestData.getUpdated());
        }
    }
}