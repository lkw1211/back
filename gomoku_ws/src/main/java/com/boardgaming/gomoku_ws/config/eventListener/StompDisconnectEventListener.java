package com.boardgaming.gomoku_ws.config.eventListener;

import com.boardgaming.gomoku_ws.application.gameQueue.command.GomokuGameQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompDisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {
    private final GomokuGameQueueService gomokuGameQueueService;

    @Override
    public void onApplicationEvent(final SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        if (headerAccessor.getCommand() != null) {
            String userId = Objects.requireNonNull(event.getUser()).getName();
            gomokuGameQueueService.removeGameQueue(userId);

            log.error("Socket disconnected " + userId);
        }
    }
}
