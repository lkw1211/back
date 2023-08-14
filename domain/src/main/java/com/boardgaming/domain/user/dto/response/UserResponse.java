package com.boardgaming.domain.user.dto.response;

import com.boardgaming.domain.user.domain.Role;

public record UserResponse(
    String id,
    String email,
    String name,
    Role role,
    String imageFileUrl
) {
}
