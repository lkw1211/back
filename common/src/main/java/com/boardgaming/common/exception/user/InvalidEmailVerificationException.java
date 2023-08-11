package com.boardgaming.common.exception.user;

import com.boardgaming.common.exception.common.BusinessException;
import com.boardgaming.common.exception.common.ErrorCode;

public class InvalidEmailVerificationException extends BusinessException {
    public InvalidEmailVerificationException() {
        super(ErrorCode.INVALID_EMAIL_VERIFICATION);
    }
}
