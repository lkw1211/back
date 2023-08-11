package com.boardgaming.common.exception.user;

import com.boardgaming.common.exception.common.BusinessException;
import com.boardgaming.common.exception.common.ErrorCode;

public class NotSentEmailVerificationException extends BusinessException {
    public NotSentEmailVerificationException() {
        super(ErrorCode.NOT_SENT_EMAIL_VERIFICATION);
    }
}
