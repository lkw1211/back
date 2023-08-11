package com.boardgaming.gomoku_ws.application.userHistory.query;

import com.boardgaming.common.exception.user.NotFoundUserException;
import com.boardgaming.domain.user.domain.User;
import com.boardgaming.domain.userHistory.domain.GomokuUserHistory;
import com.boardgaming.domain.userHistory.domain.repository.GomokuUserHistoryRepository;
import com.boardgaming.domain.userHistory.dto.response.GomokuUserHistoryResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Comparator;

@Service
@Transactional(readOnly = true)
public class GomokuUserHistoryQuery {
    private final GomokuUserHistoryRepository gomokuUserHistoryRepository;
    private final Long defaultRating;

    public GomokuUserHistoryQuery(
        final GomokuUserHistoryRepository gomokuUserHistoryRepository,
        @Value("${gomoku.setting.defaultRating}") final Long defaultRating
    ) {
        this.gomokuUserHistoryRepository = gomokuUserHistoryRepository;
        this.defaultRating = defaultRating;
    }

    public Long getUserRating(final User user) {
        return gomokuUserHistoryRepository.findByUserId(user.getId())
                .map(GomokuUserHistory::getRating)
                .orElseGet(() -> gomokuUserHistoryRepository.save(
                        GomokuUserHistory.builder()
                                .userId(user.getId())
                                .rating(defaultRating)
                                .build()
                ).getRating());
    }

    public String getLowBlackPlayRateUserId(final Collection<String> userIds) {
        String lowBlackPlayRateUserId = gomokuUserHistoryRepository.findByUserIdIn(userIds)
            .stream()
            .sorted(Comparator.comparing(GomokuUserHistory::getBlackPlayRate))
            .map(GomokuUserHistory::getUserId)
            .toList()
            .get(0);

        return userIds.stream()
            .filter(userId -> userId.equals(lowBlackPlayRateUserId))
            .findAny()
            .orElseThrow(NotFoundUserException::new);
    }

    public GomokuUserHistoryResponse getGomokuUserHistory(final String userId) {
        return GomokuUserHistoryResponse.of(gomokuUserHistoryRepository.findByUserId(userId)
            .orElse(GomokuUserHistory.builder()
                .rating(defaultRating)
                .build()));
    }
}
