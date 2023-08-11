package com.boardgaming.common.exception.user;

import com.boardgaming.common.exception.common.BusinessException;
import com.boardgaming.common.exception.common.ErrorCode;

public class NotFoundUserException extends BusinessException {
    public NotFoundUserException() {
        super(ErrorCode.NOT_FOUND_USER);
    }
}