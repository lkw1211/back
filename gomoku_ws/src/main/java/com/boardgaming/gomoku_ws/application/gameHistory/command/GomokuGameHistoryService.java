package com.boardgaming.gomoku_ws.application.gameHistory.command;

import com.boardgaming.common.exception.room.NotFoundGomokuGameHistoryException;
import com.boardgaming.domain.gameHistory.domain.GomokuGameHistory;
import com.boardgaming.domain.gameHistory.domain.repository.GomokuGameHistoryRepository;
import com.boardgaming.domain.room.domain.GomokuRule;
import com.boardgaming.domain.user.domain.User;
import com.boardgaming.gomoku_ws.application.userHistory.command.GomokuUserHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GomokuGameHistoryService {
    private final GomokuGameHistoryRepository gomokuGameHistoryRepository;
    private final GomokuUserHistoryService gomokuUserHistoryService;

    @Transactional(propagation = Propagation.MANDATORY)
    public GomokuGameHistory create(
        final Long turnTime,
        final GomokuRule rule,
        final User blackPlayer,
        final User whitePlayer,
        final Long blackPlayerBeforeRating,
        final Long whitePlayerBeforeRating
    ) {
        return gomokuGameHistoryRepository.save(GomokuGameHistory.builder()
            .turnTime(turnTime)
            .rule(rule)
            .blackPlayer(blackPlayer)
            .whitePlayer(whitePlayer)
            .blackPlayerBeforeRating(blackPlayerBeforeRating)
            .whitePlayerBeforeRating(whitePlayerBeforeRating)
            .build());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public boolean addMove(
        final Long gameHistoryId,
        final Long move
    ) {
        GomokuGameHistory history = gomokuGameHistoryRepository.findById(gameHistoryId)
            .orElseThrow(NotFoundGomokuGameHistoryException::new);

        history.addMove(move);

        if (Objects.nonNull(history.getResult())) {
            Map<String, Long> ratingChangeMap = gomokuUserHistoryService.updateUserHistory(
                history.getBlackPlayerId(),
                history.getWhitePlayerId(),
                history.getResult().getBlackResult(),
                history.getResult().getWhiteResult()
            );

            history.updateAfterRating(
                ratingChangeMap.get(history.getBlackPlayerId()),
                ratingChangeMap.get(history.getWhitePlayerId())
            );

            return true;
        }

        return false;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void timeoutEnd(
        final Long gameHistoryId
    ) {
        GomokuGameHistory history = gomokuGameHistoryRepository.findById(gameHistoryId)
            .orElseThrow(NotFoundGomokuGameHistoryException::new);

        history.timeoutEnd();
        Map<String, Long> ratingChangeMap = gomokuUserHistoryService.updateUserHistory(
            history.getBlackPlayerId(),
            history.getWhitePlayerId(),
            history.getResult().getBlackResult(),
            history.getResult().getWhiteResult()
        );

        history.updateAfterRating(
            ratingChangeMap.get(history.getBlackPlayerId()),
            ratingChangeMap.get(history.getWhitePlayerId())
        );
    }
}
