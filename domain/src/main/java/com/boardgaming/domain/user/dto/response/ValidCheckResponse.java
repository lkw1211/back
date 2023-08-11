package com.boardgaming.domain.user.dto.response;

public record ValidCheckResponse(
    boolean valid
) {
    public static ValidCheckResponse of(final boolean valid) {
        return new ValidCheckResponse(valid);
    }
}
