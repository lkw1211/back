package com.boardgaming.domain.userHistory.dto.response;

public record GomokuUserHistoryResponse(
    Long winCnt,
    Long loseCnt,
    Long drawCnt,
    Long rating
) {
}
