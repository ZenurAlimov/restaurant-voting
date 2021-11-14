package com.github.ZenurAlimov.util;

import com.github.ZenurAlimov.model.Vote;
import com.github.ZenurAlimov.to.VoteTo;

import java.util.Collection;
import java.util.List;

public class VoteUtil {
    public static List<VoteTo> getTos(Collection<Vote> votes) {
        return votes.stream()
                .map(VoteUtil::createTo)
                .toList();
    }

    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getDate(), vote.getRestaurant().id(), vote.getRestaurant().getName());
    }
}
