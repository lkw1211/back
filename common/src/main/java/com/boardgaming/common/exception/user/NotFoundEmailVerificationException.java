package com.boardgaming.common.exception.user;

import com.boardgaming.common.exception.common.BusinessException;
import com.boardgaming.common.exception.common.ErrorCode;

public class NotFoundEmailVerificationException extends BusinessException {
    public NotFoundEmailVerificationException() {
        super(ErrorCode.NOT_FOUND_EMAIL_VERIFICATION);
    }
}
