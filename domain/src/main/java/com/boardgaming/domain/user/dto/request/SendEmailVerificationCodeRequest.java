package com.boardgaming.domain.user.dto.request;

import lombok.Getter;

@Getter
public class SendEmailVerificationCodeRequest {
    private String email;
    private String sessionKey;
}
