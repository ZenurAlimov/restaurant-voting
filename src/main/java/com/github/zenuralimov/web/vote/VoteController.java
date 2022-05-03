package com.github.zenuralimov.web.vote;

import com.github.zenuralimov.error.IllegalRequestDataException;
import com.github.zenuralimov.model.Restaurant;
import com.github.zenuralimov.model.Vote;
import com.github.zenuralimov.repository.RestaurantRepository;
import com.github.zenuralimov.repository.UserRepository;
import com.github.zenuralimov.repository.VoteRepository;
import com.github.zenuralimov.to.VoteTo;
import com.github.zenuralimov.util.CommonMapper;
import com.github.zenuralimov.util.TimeUtil;
import com.github.zenuralimov.web.AuthUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.github.zenuralimov.util.TimeUtil.getTimeLimit;
import static com.github.zenuralimov.web.GlobalExceptionHandler.EXCEPTION_UPDATE_VOTE;

@RestController
@RequestMapping(value = VoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteController {

    static final String REST_URL = "/api/votes";

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final CommonMapper commonMapper;

    @GetMapping
    public List<VoteTo> getAll(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get All Votes for user {}", authUser.id());
        return commonMapper.getVoteTos(voteRepository.getAll(authUser.id()));
    }

    @GetMapping("/by-date")
    public VoteTo get(@AuthenticationPrincipal AuthUser authUser,
                      @RequestParam @Nullable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        int userId = authUser.id();
        LocalDate voteDate = (date == null) ? LocalDate.now() : date;
        log.info("vote for user {} by date {}", userId, voteDate);
        return voteRepository.getByDate(userId, voteDate).map(commonMapper::createTo).orElseThrow(
                () -> new IllegalRequestDataException("Vote for date=" + voteDate + " not found"));
    }

    @Transactional
    @PostMapping
    public ResponseEntity<VoteTo> createOrUpdate(@AuthenticationPrincipal AuthUser authUser,
                                                 @RequestParam int restaurantId) {
        int userId = authUser.id();
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new IllegalRequestDataException("Restaurant with id=" + restaurantId + " not found")
        );
        Vote vote = voteRepository.getByDate(userId, LocalDate.now()).orElse(
                new Vote(LocalDate.now(), userRepository.getById(userId), restaurant)
        );
        if (vote.isNew()) {
            log.info("vote saved for the restaurant {}", restaurantId);
            Vote created = voteRepository.save(vote);
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(REST_URL).buildAndExpand().toUri();
            return ResponseEntity.created(uriOfNewResource).body(commonMapper.createTo(created));
        }
        if (!LocalTime.now().isBefore(getTimeLimit())) {
            throw new IllegalRequestDataException(EXCEPTION_UPDATE_VOTE + TimeUtil.toString(getTimeLimit()));
        }
        if (vote.getRestaurant().id() == restaurantId) {
            log.info("trying to vote for the restaurant {} again", restaurantId);
        } else {
            log.info("user {} changed vote for the restaurant {}", userId, restaurantId);
            vote.setRestaurant(restaurant);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}