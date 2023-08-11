package com.boardgaming.domain.user.dto.response;

import com.boardgaming.domain.user.domain.Role;
import com.boardgaming.domain.user.domain.User;

public record UserResponse(
    String id,
    String email,
    String name,
    Role role,
    String imageFileUrl
) {
    public static UserResponse of(
        final User entity
    ) {
        return new UserResponse(
            entity.getId(),
            entity.getEmail(),
            entity.getName(),
            entity.getRole(),
            entity.getImageFileUrl()
        );
    }
}
