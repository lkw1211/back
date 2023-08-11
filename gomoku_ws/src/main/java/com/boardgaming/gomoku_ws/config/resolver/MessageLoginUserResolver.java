package com.boardgaming.gomoku_ws.config.resolver;

import com.boardgaming.core.config.auth.filter.JwtFilter;
import com.boardgaming.core.config.auth.parser.JwtTokenParser;
import com.boardgaming.domain.auth.domain.TokenType;
import com.boardgaming.gomoku_ws.config.annotation.MessageLoginUser;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MessageLoginUserResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenParser jwtTokenParser;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MessageLoginUser.class);
    }

    @Override
    public String resolveArgument(final MethodParameter parameter, final Message<?> message) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        if (Objects.nonNull(headerAccessor.getNativeHeader("Cookie"))) {
            String[] cookies = Objects.requireNonNull(headerAccessor.getNativeHeader("Cookie")).get(0).split(";");

            String accessToken = null;

            for (String cookie : cookies) {
                String cookieTrim = cookie.trim();
                if (cookieTrim.startsWith(TokenType.ACCESS.name())) {
                    accessToken = JwtFilter.resolveToken(new Cookie("ACCESS", cookieTrim.split("=")[1]));
                    break;
                }
            }

            return ((UserDetails)jwtTokenParser.extractAuthentication(accessToken).getPrincipal()).getUsername();
        }

        throw new IllegalArgumentException();
    }
}
