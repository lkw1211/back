package com.boardgaming.gomoku_ws.controller.gameHistory.rest;

import com.boardgaming.core.config.annotation.LoginUser;
import com.boardgaming.domain.gameHistory.dto.response.GomokuGameHistoryResponse;
import com.boardgaming.domain.gameHistory.dto.response.GomokuUserGameHistorySimpleResponse;
import com.boardgaming.gomoku_ws.application.gameHistory.query.GomokuGameHistoryQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gameHistory")
@RequiredArgsConstructor
public class GomokuGameHistoryController {
    private final GomokuGameHistoryQuery gomokuGameHistoryQuery;

    @GetMapping
    public ResponseEntity<Page<GomokuUserGameHistorySimpleResponse>> getGomokuUserGameHistory(
        @LoginUser final String userId,
        @PageableDefault(sort = "id", direction = Sort.Direction.DESC) final Pageable pageable
    ) {
        return ResponseEntity.ok(gomokuGameHistoryQuery.getUserGameHistory(userId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GomokuGameHistoryResponse> getGomokuGameHistory(
        @LoginUser final String userId,
        @PathVariable("id") final Long id
    ) {
        return ResponseEntity.ok(gomokuGameHistoryQuery.getGameHistory(userId, id));
    }
}
