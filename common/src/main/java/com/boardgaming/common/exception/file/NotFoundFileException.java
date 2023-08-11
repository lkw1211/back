package com.boardgaming.common.exception.file;

import com.boardgaming.common.exception.common.BusinessException;
import com.boardgaming.common.exception.common.ErrorCode;

public class NotFoundFileException extends BusinessException {
    public NotFoundFileException() {
        super(ErrorCode.NOT_FOUND_FILE);
    }
}
