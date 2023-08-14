package com.boardgaming.gomoku_ws.application.gameHistory.command;

import com.boardgaming.common.exception.room.NotFoundGomokuGameHistoryException;
import com.boardgaming.domain.gameHistory.domain.GomokuGameHistory;
import com.boardgaming.domain.gameHistory.domain.GomokuGameResult;
import com.boardgaming.domain.gameHistory.domain.GomokuGameResultReason;
import com.boardgaming.domain.gameHistory.domain.repository.GomokuGameHistoryRepository;
import com.boardgaming.domain.room.domain.GomokuColor;
import com.boardgaming.domain.room.domain.GomokuRule;
import com.boardgaming.domain.user.domain.User;
import com.boardgaming.gomoku_ws.application.userHistory.command.GomokuUserHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

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
        return gomokuGameHistoryRepository.save(
            GomokuGameHistory.builder()
                .turnTime(turnTime)
                .rule(rule)
                .blackPlayerId(blackPlayer.getId())
                .blackPlayerName(blackPlayer.getName())
                .whitePlayerId(whitePlayer.getId())
                .blackPlayerName(whitePlayer.getName())
                .blackPlayerBeforeRating(blackPlayerBeforeRating)
                .whitePlayerBeforeRating(whitePlayerBeforeRating)
                .build());
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void addMove(
        final Long gameHistoryId,
        final Integer move
    ) {
        boolean isEnd = false;

        GomokuGameHistory history = gomokuGameHistoryRepository.findById(gameHistoryId)
            .orElseThrow(NotFoundGomokuGameHistoryException::new);

        history.addMove(move);
    }

    @Transactional
    public void fiveInRowEnd(
        final Long gameHistoryId,
        final GomokuColor winnerColor
    ) {
        GomokuGameHistory history = gomokuGameHistoryRepository.findById(gameHistoryId)
            .orElseThrow(NotFoundGomokuGameHistoryException::new);

        fiveInRowEnd(history, winnerColor);
    }

    public void fiveInRowEnd(
        final GomokuGameHistory history,
        final GomokuColor winnerColor
    ) {
        GomokuGameResult result = switch (winnerColor) {
            case BLACK -> GomokuGameResult.BLACK_WIN;
            case WHITE -> GomokuGameResult.WHITE_WIN;
            default -> throw new UnsupportedOperationException();
        };

        history.updateResultAndReason(
            result,
            GomokuGameResultReason.DEFAULT
        );

        updateRatingChange(history);
    }

    @Transactional
    public void timeoutEnd(final Long gameHistoryId) {
        GomokuGameHistory history = gomokuGameHistoryRepository.findById(gameHistoryId)
            .orElseThrow(NotFoundGomokuGameHistoryException::new);

        timeoutEnd(history);
    }

    @Transactional
    public void timeoutEnd(final GomokuGameHistory history) {
        GomokuGameResult result = switch (history.getCurrentTurn()) {
            case BLACK -> GomokuGameResult.WHITE_WIN;
            case WHITE -> GomokuGameResult.BLACK_WIN;
            default -> throw new UnsupportedOperationException();
        };

        history.updateResultAndReason(
            result,
            GomokuGameResultReason.TIME_OUT
        );

        updateRatingChange(history);
    }

    public void updateRatingChange(final GomokuGameHistory history) {
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
