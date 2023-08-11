package com.boardgaming.domain.userHistory.dto.response;

import com.boardgaming.domain.userHistory.domain.GomokuUserHistory;

public record GomokuUserHistoryResponse(
    Long winCnt,
    Long loseCnt,
    Long drawCnt,
    Long rating
) {
    public static GomokuUserHistoryResponse of(final GomokuUserHistory history) {
        return new GomokuUserHistoryResponse(
            history.getWinCnt(),
            history.getLoseCnt(),
            history.getDrawCnt(),
            history.getRating()
        );
    }
}
