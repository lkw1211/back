package com.boardgaming.gomoku_ws.controller.room.rest;

import com.boardgaming.core.config.annotation.LoginUser;
import com.boardgaming.domain.room.dto.response.GomokuEnterRoomResponse;
import com.boardgaming.domain.room.dto.response.GomokuRoomListResponse;
import com.boardgaming.gomoku_ws.application.room.command.GomokuRoomService;
import com.boardgaming.gomoku_ws.application.room.query.GomokuRoomQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class GomokuRoomController {
    private final GomokuRoomService gomokuRoomService;
    private final GomokuRoomQuery gomokuRoomQuery;

    @PostMapping("/{roomId}/player")
    public ResponseEntity<GomokuEnterRoomResponse> enterRoomPlayer(
        @LoginUser final String userId,
        @PathVariable final Long roomId
    ) {
        return ResponseEntity.ok(gomokuRoomService.enterRoomPlayer(userId, roomId));
    }

    @PostMapping("/{roomId}/watcher")
    public ResponseEntity<GomokuEnterRoomResponse> enterRoomWatcher(
        @LoginUser final String userId,
        @PathVariable final Long roomId
    ) {
        return ResponseEntity.ok(gomokuRoomService.enterRoomWatcher(userId, roomId));
    }

    @GetMapping("/play")
    public ResponseEntity<GomokuRoomListResponse> getMyPlayRoom(
        @LoginUser final String userId
    ) {
        return ResponseEntity.ok(gomokuRoomQuery.getMyGameRoom(userId));
    }

    @GetMapping
    public ResponseEntity<List<GomokuRoomListResponse>> getAllRoomList(
        @LoginUser final String userId
    ) {
        return ResponseEntity.ok(gomokuRoomQuery.getAllStartRoomList());
    }
}
