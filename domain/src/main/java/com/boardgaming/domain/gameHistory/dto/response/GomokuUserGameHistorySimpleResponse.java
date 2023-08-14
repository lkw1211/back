package com.boardgaming.domain.gameHistory.dto.response;

import com.boardgaming.domain.gameHistory.domain.GomokuGameHistory;
import com.boardgaming.domain.room.domain.GomokuColor;
import com.boardgaming.domain.userHistory.domain.GomokuUserResult;

public record GomokuUserGameHistorySimpleResponse(
    Long id,
    String opponent,
    GomokuColor userColor,
    GomokuUserResult result,
    Long beforeRating,
    Long afterRating
) {
    public static GomokuUserGameHistorySimpleResponse blackPlayerOf(
        final GomokuGameHistory history
    ) {
        return new GomokuUserGameHistorySimpleResponse(
            history.getId(),
            history.getWhitePlayerName(),
            GomokuColor.BLACK,
            history.getResult().getBlackResult(),
            history.getBlackPlayerBeforeRating(),
            history.getBlackPlayerAfterRating()
        );
    }

    public static GomokuUserGameHistorySimpleResponse whitePlayerOf(
        final GomokuGameHistory history
    ) {
        return new GomokuUserGameHistorySimpleResponse(
            history.getId(),
            history.getBlackPlayerName(),
            GomokuColor.WHITE,
            history.getResult().getWhiteResult(),
            history.getWhitePlayerBeforeRating(),
            history.getWhitePlayerAfterRating()
        );
    }
}
