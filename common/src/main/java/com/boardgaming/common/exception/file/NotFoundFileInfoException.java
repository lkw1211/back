package com.boardgaming.common.exception.file;

import com.boardgaming.common.exception.common.BusinessException;
import com.boardgaming.common.exception.common.ErrorCode;

public class NotFoundFileInfoException extends BusinessException {
    public NotFoundFileInfoException() {
        super(ErrorCode.NOT_FOUND_FILE_INFO);
    }
}
