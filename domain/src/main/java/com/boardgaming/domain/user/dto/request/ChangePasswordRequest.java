package com.boardgaming.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChangePasswordRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String sessionKey;
    @NotBlank
    private String password;
    @NotBlank
    private String confirmPassword;
}
