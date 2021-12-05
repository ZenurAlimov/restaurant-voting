package com.github.zenuralimov.web.vote;

import com.github.zenuralimov.model.Vote;
import com.github.zenuralimov.to.VoteTo;
import com.github.zenuralimov.web.MatcherFactory;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import static com.github.zenuralimov.web.restaurant.RestaurantTestData.*;
import static com.github.zenuralimov.web.user.UserTestData.admin;
import static com.github.zenuralimov.web.user.UserTestData.user;
import static com.github.zenuralimov.web.vote.VoteController.TIME_LIMIT;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "user", "restaurant");
    public static MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class, "");

    public static final int VOTE1_ID = 1;
    public static final int ADMIN_VOTE_ID = 3;

    public static final Vote vote1 = new Vote(VOTE1_ID, LocalDate.now().minusDays(1));
    public static final Vote vote2 = new Vote(VOTE1_ID + 1, LocalDate.now());
    public static final Vote vote3 = new Vote(ADMIN_VOTE_ID, LocalDate.now().minusDays(1));

    static {
        vote1.setUser(user);
        vote1.setRestaurant(kfc);
        vote2.setUser(user);
        vote2.setRestaurant(king);
        vote3.setUser(admin);
        vote3.setRestaurant(mc);
    }

    public static final List<Vote> userVotes = List.of(vote2, vote1);

    public static Vote getNew() {
        return new Vote(null, LocalDate.now());
    }

    public static Vote getUpdated() {
        return new Vote(vote2.id(), LocalDate.now());
    }
}
