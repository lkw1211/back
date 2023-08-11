package com.boardgaming.common.exception.room;

import com.boardgaming.common.exception.common.BusinessException;
import com.boardgaming.common.exception.common.ErrorCode;

public class NotYourTurnException extends BusinessException {
    public NotYourTurnException() {
        super(ErrorCode.NOT_YOUR_TURN);
    }
}
