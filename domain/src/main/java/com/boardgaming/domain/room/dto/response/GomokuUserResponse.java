package com.boardgaming.domain.room.dto.response;

public record GomokuUserResponse(
    String id,
    String name,
    String email,
    String imageFileUrl
) {
}
