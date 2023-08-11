package com.boardgaming.domain.auth.dto.request;

import com.boardgaming.domain.user.domain.Role;
import com.boardgaming.domain.user.domain.User;
import lombok.Getter;

@Getter
public class SignUpRequest {
    private String name;
    private String email;
    private String sessionKey;
    private String password;
    private String confirmPassword;

    public User toEntity(final String password) {
        return User.builder()
            .email(email)
            .name(name)
            .password(password)
            .role(Role.USER)
            .build();
    }
}
