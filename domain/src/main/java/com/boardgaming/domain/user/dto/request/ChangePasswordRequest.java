package com.boardgaming.domain.user.dto.request;

import lombok.Getter;

@Getter
public class ChangePasswordRequest {
    private String email;
    private String sessionKey;
    private String password;
    private String confirmPassword;
}
