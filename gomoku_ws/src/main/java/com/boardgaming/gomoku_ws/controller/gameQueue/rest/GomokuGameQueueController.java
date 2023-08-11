package com.boardgaming.gomoku_ws.controller.gameQueue.rest;

import com.boardgaming.core.config.annotation.LoginUser;
import com.boardgaming.domain.gameQueue.dto.response.GameQueueSubResponse;
import com.boardgaming.gomoku_ws.application.gameQueue.command.GomokuGameQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game-queue")
@RequiredArgsConstructor
public class GomokuGameQueueController {
    private final GomokuGameQueueService gomokuGameQueueService;

    @PostMapping
    public ResponseEntity<GameQueueSubResponse> addGameQueue(@LoginUser final String userId) {
        return ResponseEntity.ok(gomokuGameQueueService.addGameQueue(userId));
    }

    @DeleteMapping
    public ResponseEntity<Void> removeGameQueue(@LoginUser final String userId) {
        gomokuGameQueueService.removeGameQueue(userId);

        return ResponseEntity.noContent().build();
    }
}
