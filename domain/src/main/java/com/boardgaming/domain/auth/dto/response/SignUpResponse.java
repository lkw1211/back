package com.boardgaming.domain.auth.dto.response;

import com.boardgaming.domain.user.domain.User;

public record SignUpResponse(
    String id
) {
    public static SignUpResponse of(final User entity) {
        return new SignUpResponse(entity.getId());
    }
}
