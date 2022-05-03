package com.github.zenuralimov.web.vote;

import com.github.zenuralimov.repository.VoteRepository;
import com.github.zenuralimov.to.VoteTo;
import com.github.zenuralimov.util.TimeUtil;
import com.github.zenuralimov.web.AbstractControllerTest;
import com.github.zenuralimov.web.GlobalExceptionHandler;
import com.github.zenuralimov.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.github.zenuralimov.util.TimeUtil.getTimeLimit;
import static com.github.zenuralimov.web.restaurant.RestaurantTestData.KING_ID;
import static com.github.zenuralimov.web.restaurant.RestaurantTestData.MC_ID;
import static com.github.zenuralimov.web.user.UserTestData.ADMIN_MAIL;
import static com.github.zenuralimov.web.user.UserTestData.USER_MAIL;
import static com.github.zenuralimov.web.vote.VoteTestData.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends AbstractControllerTest {

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
                .andExpect(VOTE_TO_MATCHER.contentJson(commonMapper.getVoteTos(userVotes)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/by-date")
                .param("date", LocalDate.now().minusDays(1).toString()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_TO_MATCHER.contentJson(commonMapper.createTo(vote1)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void create() throws Exception {
        VoteTo newVote = VoteTestData.getNew();
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(KING_ID)))
                .andDo(print())
                .andExpect(status().isCreated());

        VoteTo created = repository.getByDate(UserTestData.ADMIN_ID, LocalDate.now()).map(commonMapper::createTo).orElse(null);
        VoteTestData.VOTE_TO_MATCHER.assertMatch(created, newVote);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateBefore() throws Exception {
        VoteTo updated = VoteTestData.getUpdated();
        TimeUtil.setTimeLimit(LocalTime.now().plusSeconds(1));
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(MC_ID)))
                .andDo(print())
                .andExpect(status().isNoContent());

        VoteTestData.VOTE_TO_MATCHER.assertMatch(repository.getByDate(UserTestData.USER_ID, LocalDate.now()).map(commonMapper::createTo).orElse(null), updated);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateAfter() throws Exception {
        LocalTime localTime = LocalTime.now();
        TimeUtil.setTimeLimit(localTime);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .param("restaurantId", String.valueOf(MC_ID)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(GlobalExceptionHandler.EXCEPTION_UPDATE_VOTE + TimeUtil.toString(getTimeLimit()))));
    }
}