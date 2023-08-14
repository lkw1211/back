package com.boardgaming.domain.auth.dto.request;

import com.boardgaming.domain.user.domain.Role;
import com.boardgaming.domain.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignUpRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String sessionKey;
    @NotBlank
    private String password;
    @NotBlank
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
