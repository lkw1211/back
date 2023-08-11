package com.boardgaming.gomoku_ws.application.userHistory.command;

import com.boardgaming.domain.room.domain.GomokuColor;
import com.boardgaming.domain.userHistory.domain.GomokuUserHistory;
import com.boardgaming.domain.userHistory.domain.GomokuUserResult;
import com.boardgaming.domain.userHistory.domain.repository.GomokuUserHistoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class GomokuUserHistoryService {
    private final GomokuUserHistoryRepository gomokuUserHistoryRepository;
    private final Long defaultRating;

    public GomokuUserHistoryService(
        final GomokuUserHistoryRepository gomokuUserHistoryRepository,
        @Value("${gomoku.setting.defaultRating}") final Long defaultRating
    ) {
        this.gomokuUserHistoryRepository = gomokuUserHistoryRepository;
        this.defaultRating = defaultRating;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Map<String, Long> updateUserHistory(
        final String blackPlayerId,
        final String whitePlayerId,
        final GomokuUserResult blackResult,
        final GomokuUserResult whiteResult
    ) {
        GomokuUserHistory blackPlayerHistory = gomokuUserHistoryRepository.findByUserId(blackPlayerId)
            .orElseGet(() -> gomokuUserHistoryRepository.save(GomokuUserHistory.builder()
                .userId(blackPlayerId)
                .rating(defaultRating)
                .build()));

        GomokuUserHistory whitePlayerHistory = gomokuUserHistoryRepository.findByUserId(whitePlayerId)
            .orElseGet(() -> gomokuUserHistoryRepository.save(GomokuUserHistory.builder()
                .userId(whitePlayerId)
                .rating(defaultRating)
                .build()));

        blackPlayerHistory.updateHistory(blackResult, GomokuColor.BLACK, whitePlayerHistory.getRating());
        whitePlayerHistory.updateHistory(whiteResult, GomokuColor.WHITE, blackPlayerHistory.getRating());

        return Map.of(
            blackPlayerId,
            blackPlayerHistory.getRating(),
            whitePlayerId,
            whitePlayerHistory.getRating()
        );
    }
}
