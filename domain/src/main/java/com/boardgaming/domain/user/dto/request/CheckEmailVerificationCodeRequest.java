package com.boardgaming.domain.user.dto.request;

import lombok.Getter;

@Getter
public class CheckEmailVerificationCodeRequest {
    private String email;
    private String sessionKey;
    private String verificationCode;
}
