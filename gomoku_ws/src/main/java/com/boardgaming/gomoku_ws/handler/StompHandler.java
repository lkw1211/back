package com.boardgaming.gomoku_ws.handler;

import com.boardgaming.core.config.auth.parser.JwtTokenParser;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
    private final JwtTokenParser jwtTokenParser;

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
        return message;
    }
}