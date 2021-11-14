package com.github.ZenurAlimov.web.vote;

import com.github.ZenurAlimov.repository.VoteRepository;
import com.github.ZenurAlimov.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.ZenurAlimov.web.restaurant.RestaurantTestData.KFC_ID;
import static com.github.ZenurAlimov.web.user.UserTestData.ADMIN_MAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(value = ADMIN_MAIL)
class AdminVoteControllerTest extends AbstractControllerTest {

    static final String REST_URL = AdminVoteController.REST_URL + '/';

    @Autowired
    private VoteRepository repository;

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + KFC_ID))
                .andExpect(status().isNoContent());
        assertFalse(repository.findById(KFC_ID).isPresent());
    }
}