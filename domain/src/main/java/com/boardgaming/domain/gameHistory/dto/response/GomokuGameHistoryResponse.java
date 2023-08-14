package com.boardgaming.domain.gameHistory.dto.response;

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
    List<Integer> moveStack
) {
}
