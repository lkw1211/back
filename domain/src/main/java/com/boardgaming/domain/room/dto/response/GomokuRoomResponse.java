package com.boardgaming.domain.room.dto.response;

import com.boardgaming.domain.gameHistory.domain.GomokuGameHistory;
import com.boardgaming.domain.gameHistory.domain.GomokuGameResult;
import com.boardgaming.domain.gameHistory.domain.GomokuGameResultReason;
import com.boardgaming.domain.room.domain.GomokuColor;
import com.boardgaming.domain.room.domain.GomokuRoom;
import com.boardgaming.domain.room.domain.GomokuRoomStatus;
import com.boardgaming.domain.room.domain.GomokuRule;
import com.boardgaming.domain.userHistory.dto.response.GomokuUserHistoryResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record GomokuRoomResponse(
    Long id,
    GomokuRule rule,
    Long turnTime,
    Long turnTimeLeft,
    GomokuUserResponse blackPlayer,
    GomokuUserHistoryResponse blackGameUserHistory,
    GomokuUserResponse whitePlayer,
    GomokuUserHistoryResponse whiteGameUserHistory,
    List<Long> moveStack,
    GomokuRoomStatus gameStatus,
    GomokuColor gameTurn,
    GomokuGameResult gameResult,
    GomokuGameResultReason gameResultReason
) {
    public static GomokuRoomResponse of(
        final GomokuRoom room,
        final GomokuUserResponse blackPlayer,
        final GomokuUserHistoryResponse blackGameUserHistory,
        final GomokuUserResponse whitePlayer,
        final GomokuUserHistoryResponse whiteGameUserHistory,
        final GomokuGameHistory history
    ) {
        return new GomokuRoomResponse(
            room.getId(),
            room.getRule(),
            room.getTurnTime(),
            Optional.ofNullable(room.getTurnTimeEnd())
                .map(timeEnd -> timeEnd - System.currentTimeMillis())
                .orElse(0L),
            blackPlayer,
            blackGameUserHistory,
            whitePlayer,
            whiteGameUserHistory,
            Optional.ofNullable(history)
                .map(GomokuGameHistory::getMoveStack)
                .orElse(new ArrayList<>()),
            room.getStatus(),
            room.getGameTurn(),
            Optional.ofNullable(history)
                .map(GomokuGameHistory::getResult)
                .orElse(null),
            Optional.ofNullable(history)
                .map(GomokuGameHistory::getReason)
                .orElse(null)
        );
    }
}
