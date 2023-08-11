package com.boardgaming.common.exception.user;

import com.boardgaming.common.exception.common.BusinessException;
import com.boardgaming.common.exception.common.ErrorCode;

public class DuplicateEmailException extends BusinessException {
    public DuplicateEmailException() {
        super(ErrorCode.DUPLICATE_EMAIL);
    }
}
