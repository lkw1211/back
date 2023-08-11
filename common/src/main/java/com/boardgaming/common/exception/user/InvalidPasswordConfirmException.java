package com.boardgaming.common.exception.user;

import com.boardgaming.common.exception.common.BusinessException;
import com.boardgaming.common.exception.common.ErrorCode;

public class InvalidPasswordConfirmException extends BusinessException {
    public InvalidPasswordConfirmException() {
        super(ErrorCode.INVALID_PASSWORD_CONFIRM);
    }
}
