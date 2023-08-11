package com.boardgaming.domain.room.dto.response;

import java.util.Objects;

public record GomokuEnterRoomResponse(
    boolean valid,
    GomokuRoomResponse roomDetail
) {
    public static GomokuEnterRoomResponse of(
        final GomokuRoomResponse roomDetail
    ) {
        return new GomokuEnterRoomResponse(
            Objects.nonNull(roomDetail),
            roomDetail
        );
    }
}
