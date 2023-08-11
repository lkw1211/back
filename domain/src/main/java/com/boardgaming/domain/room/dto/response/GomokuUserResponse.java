package com.boardgaming.domain.room.dto.response;

public record GomokuUserResponse(
    String id,
    String name,
    String email,
    String imageFileUrl
) {
    public static GomokuUserResponse of(
        final String id,
        final String name,
        final String email,
        final String imageFileUrl
    ) {
        return new GomokuUserResponse(id, name, email, imageFileUrl);
    }
}
