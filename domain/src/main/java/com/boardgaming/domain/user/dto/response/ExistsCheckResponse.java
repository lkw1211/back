package com.boardgaming.domain.user.dto.response;

public record ExistsCheckResponse(
    boolean exists
) {
    public static ExistsCheckResponse of(final boolean exists) {
        return new ExistsCheckResponse(exists);
    }
}
