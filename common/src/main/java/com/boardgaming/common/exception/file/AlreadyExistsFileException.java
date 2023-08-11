package com.boardgaming.common.exception.file;

import com.boardgaming.common.exception.common.BusinessException;
import com.boardgaming.common.exception.common.ErrorCode;

public class AlreadyExistsFileException extends BusinessException {
    public AlreadyExistsFileException() {
        super(ErrorCode.ALREADY_EXISTS_FILE);
    }
}
