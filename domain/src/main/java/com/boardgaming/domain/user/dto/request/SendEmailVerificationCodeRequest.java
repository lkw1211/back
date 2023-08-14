package com.boardgaming.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SendEmailVerificationCodeRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String sessionKey;
}
