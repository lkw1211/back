package com.boardgaming.domain.room.dto.response;

import com.boardgaming.domain.gameHistory.domain.GomokuGameResult;
import com.boardgaming.domain.gameHistory.domain.GomokuGameResultReason;
import com.boardgaming.domain.room.domain.GomokuColor;
import com.boardgaming.domain.room.domain.GomokuRule;
import com.boardgaming.domain.userHistory.dto.response.GomokuUserHistoryResponse;

import java.util.List;

public record GomokuRoomResponse(
    Long id,
    GomokuRule rule,
    Long turnTime,
    Long turnTimeLeft,
    GomokuUserResponse blackPlayer,
    GomokuUserHistoryResponse blackGameUserHistory,
    GomokuUserResponse whitePlayer,
    GomokuUserHistoryResponse whiteGameUserHistory,
    List<Integer> moveStack,
    GomokuColor gameTurn,
    GomokuGameResult gameResult,
    GomokuGameResultReason gameResultReason
) {
}
