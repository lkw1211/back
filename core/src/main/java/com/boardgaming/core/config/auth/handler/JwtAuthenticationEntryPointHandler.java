package com.boardgaming.core.config.auth.handler;

import com.boardgaming.common.exception.common.ErrorCode;
import com.boardgaming.common.exception.common.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class JwtAuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final AuthenticationException authException
    ) throws IOException {
        if (authException.getClass().equals(BadCredentialsException.class)) {
            throw new BadCredentialsException("");
        }

        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.HANDLE_AUTHENTICATION_ENTRYPOINT);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorResponse.getStatus());
        try (OutputStream outputStream = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(outputStream, errorResponse);
            outputStream.flush();
        }
    }
}
