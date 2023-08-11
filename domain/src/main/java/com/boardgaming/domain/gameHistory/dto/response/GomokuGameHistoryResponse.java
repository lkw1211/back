package com.boardgaming.domain.gameHistory.dto.response;

import com.boardgaming.domain.gameHistory.domain.GomokuGameHistory;
import com.boardgaming.domain.gameHistory.domain.GomokuGameResult;
import com.boardgaming.domain.gameHistory.domain.GomokuGameResultReason;
import com.boardgaming.domain.room.dto.response.GomokuUserResponse;
import com.boardgaming.domain.userHistory.dto.response.GomokuUserHistoryResponse;

import java.util.List;

public record GomokuGameHistoryResponse(
    Long id,
    GomokuUserResponse blackPlayer,
    GomokuUserHistoryResponse blackGameUserHistory,
    Long blackPlayerBeforeRating,
    Long blackPlayerAfterRating,
    GomokuUserResponse whitePlayer,
    GomokuUserHistoryResponse whiteGameUserHistory,
    Long whitePlayerBeforeRating,
    Long whitePlayerAfterRating,
    GomokuGameResult gameResult,
    GomokuGameResultReason gameResultReason,
    List<Long> moveStack
) {
    public static GomokuGameHistoryResponse of(
        final GomokuUserResponse blackPlayer,
        final GomokuUserHistoryResponse blackGameUserHistory,
        final GomokuUserResponse whitePlayer,
        final GomokuUserHistoryResponse whiteGameUserHistory,
        final GomokuGameHistory history
    ) {
        return new GomokuGameHistoryResponse(
            history.getId(),
            blackPlayer,
            blackGameUserHistory,
            history.getBlackPlayerBeforeRating(),
            history.getBlackPlayerAfterRating(),
            whitePlayer,
            whiteGameUserHistory,
            history.getWhitePlayerBeforeRating(),
            history.getWhitePlayerAfterRating(),
            history.getResult(),
            history.getReason(),
            history.getMoveStack()
        );
    }
}
