package com.boardgaming.common.exception.room;

import com.boardgaming.common.exception.common.BusinessException;
import com.boardgaming.common.exception.common.ErrorCode;

public class NotFoundGomokuGameHistoryException extends BusinessException {
    public NotFoundGomokuGameHistoryException() {
        super(ErrorCode.NOT_FOUND_GOMOKU_GAME_HISTORY);
    }
}
