package com.boardgaming.core.config.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final AccessDeniedException accessDeniedException
    ) {
        throw new AccessDeniedException("");
    }
}
