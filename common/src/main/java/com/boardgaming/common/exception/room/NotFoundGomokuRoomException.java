package com.boardgaming.common.exception.room;

import com.boardgaming.common.exception.common.BusinessException;
import com.boardgaming.common.exception.common.ErrorCode;

public class NotFoundGomokuRoomException extends BusinessException {
    public NotFoundGomokuRoomException() {
        super(ErrorCode.NOT_FOUND_GOMOKU_ROOM);
    }
}
