package com.github.zenuralimov.util;

import com.github.zenuralimov.model.Vote;
import com.github.zenuralimov.to.VoteTo;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class VoteUtil {
    public static List<VoteTo> getTos(Collection<Vote> votes) {
        return votes.stream()
                .map(VoteUtil::createTo)
                .toList();
    }

    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getDate(), vote.getRestaurant().id());
    }
}
