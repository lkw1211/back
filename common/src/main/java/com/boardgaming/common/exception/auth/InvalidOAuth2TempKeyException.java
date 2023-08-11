package com.boardgaming.common.exception.auth;

import com.boardgaming.common.exception.common.BusinessException;
import com.boardgaming.common.exception.common.ErrorCode;

public class InvalidOAuth2TempKeyException extends BusinessException {
    public InvalidOAuth2TempKeyException() {
        super(ErrorCode.INVALID_OAUTH2_TEMP_KEY);
    }
}
