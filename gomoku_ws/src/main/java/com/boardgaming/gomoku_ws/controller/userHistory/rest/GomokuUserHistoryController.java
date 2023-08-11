package com.boardgaming.gomoku_ws.controller.userHistory.rest;

import com.boardgaming.core.config.annotation.LoginUser;
import com.boardgaming.domain.userHistory.dto.response.GomokuUserHistoryResponse;
import com.boardgaming.gomoku_ws.application.userHistory.query.GomokuUserHistoryQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/userHistory")
@RequiredArgsConstructor
public class GomokuUserHistoryController {
    private final GomokuUserHistoryQuery gomokuUserHistoryQuery;

    @GetMapping
    public ResponseEntity<GomokuUserHistoryResponse> getGomokuUserHistory(
        @LoginUser final String userId
    ) {
        return ResponseEntity.ok(gomokuUserHistoryQuery.getGomokuUserHistory(userId));
    }
}
