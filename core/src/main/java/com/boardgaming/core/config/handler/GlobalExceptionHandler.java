package com.boardgaming.core.config.handler;

import com.boardgaming.common.exception.common.BusinessException;
import com.boardgaming.common.exception.common.ErrorCode;
import com.boardgaming.common.exception.common.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.core.AuthenticationException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException exception) {
        ErrorResponse response = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ErrorResponse response = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ErrorResponse> handleAuthenticationException(final AuthenticationException e) {
        return new ResponseEntity<>(ErrorResponse.of(ErrorCode.HANDLE_AUTHENTICATION_ENTRYPOINT), HttpStatus.BAD_REQUEST);
    }
}
