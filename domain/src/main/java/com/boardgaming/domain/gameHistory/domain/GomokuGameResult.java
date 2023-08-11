package com.boardgaming.domain.gameHistory.domain;

import com.boardgaming.domain.userHistory.domain.GomokuUserResult;
import lombok.Getter;

@Getter
public enum GomokuGameResult {
    BLACK_WIN(GomokuUserResult.WIN, GomokuUserResult.LOSE),
    WHITE_WIN(GomokuUserResult.LOSE, GomokuUserResult.WIN),
    DRAW(GomokuUserResult.DRAW, GomokuUserResult.DRAW);

    private GomokuUserResult blackResult;
    private GomokuUserResult whiteResult;

    GomokuGameResult(
        final GomokuUserResult blackResult,
        final GomokuUserResult whiteResult
    ) {
        this.blackResult = blackResult;
        this.whiteResult = whiteResult;
    }
}
