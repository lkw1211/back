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
    public static GomokuUserGameHistorySimpleResponse of(
        final GomokuGameHistory history,
        final String userId
    ) {
        String opponent;
        GomokuColor userColor;
        GomokuUserResult result;
        Long beforeRating;
        Long afterRating;

        if (history.getBlackPlayerId().equals(userId)) {
            opponent = history.getWhitePlayerName();
            userColor = GomokuColor.BLACK;
            result = history.getResult().getBlackResult();
            beforeRating = history.getBlackPlayerBeforeRating();
            afterRating = history.getBlackPlayerAfterRating();
        } else {
            opponent = history.getBlackPlayerName();
            userColor = GomokuColor.WHITE;
            result = history.getResult().getWhiteResult();
            beforeRating = history.getWhitePlayerBeforeRating();
            afterRating = history.getWhitePlayerAfterRating();
        }

        return new GomokuUserGameHistorySimpleResponse(
            history.getId(),
            opponent,
            userColor,
            result,
            beforeRating,
            afterRating
        );
    }
}
