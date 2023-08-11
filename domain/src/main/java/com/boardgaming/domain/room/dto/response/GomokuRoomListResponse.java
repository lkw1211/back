package com.boardgaming.domain.room.dto.response;

import com.boardgaming.domain.room.domain.GomokuRoom;

public record GomokuRoomListResponse(
    Long id,
    GomokuUserResponse blackPlayer,
    GomokuUserResponse whitePlayer,
    Long pieceCnt
) {
    public static GomokuRoomListResponse of(
        final GomokuRoom entity
    ) {
        return new GomokuRoomListResponse(
            entity.getId(),
            GomokuUserResponse.of(entity.getBlackPlayerId(), entity.getBlackPlayerName(), entity.getBlackPlayerEmail(), entity.getBlackPlayerImageFileUrl()),
            GomokuUserResponse.of(entity.getWhitePlayerId(), entity.getWhitePlayerName(), entity.getWhitePlayerEmail(), entity.getWhitePlayerImageFileUrl()),
            entity.getPieceCnt()
        );
    }
}
