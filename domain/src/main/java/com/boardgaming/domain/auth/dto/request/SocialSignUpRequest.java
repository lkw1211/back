package com.boardgaming.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SocialSignUpRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String key;
}
