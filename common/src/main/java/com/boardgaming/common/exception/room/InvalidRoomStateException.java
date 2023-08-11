package com.boardgaming.common.exception.room;

import com.boardgaming.common.exception.common.BusinessException;
import com.boardgaming.common.exception.common.ErrorCode;

public class InvalidRoomStateException extends BusinessException {
    public InvalidRoomStateException() {
        super(ErrorCode.INVALID_ROOM_STATE);
    }
}
