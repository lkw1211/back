package com.boardgaming.gomoku_ws.controller.room.message;

import com.boardgaming.domain.room.dto.request.GomokuMoveRequest;
import com.boardgaming.gomoku_ws.application.room.command.GomokuRoomService;
import com.boardgaming.gomoku_ws.config.annotation.MessageLoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GomokuRoomMessageController {
    private final GomokuRoomService gomokuRoomService;

    @MessageMapping("/room/{roomId}/player/put")
    public void putMove(
        @MessageLoginUser final String userId,
        @DestinationVariable final Long roomId,
        @Payload final GomokuMoveRequest request
    ) {
        gomokuRoomService.putMove(userId, roomId, request);
    }

    @MessageMapping("/room/{roomId}/player/end-check")
    public void endCheck(
        @MessageLoginUser final String userId,
        @DestinationVariable final Long roomId
    ) {
        gomokuRoomService.endCheck(userId, roomId);
    }
}

