package com.boardgaming.domain.auth.dto.request;

import lombok.Getter;

@Getter
public class SocialSignUpRequest {
    private String name;
    private String key;
}
