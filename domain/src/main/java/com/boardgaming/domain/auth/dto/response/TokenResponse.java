package com.boardgaming.domain.auth.dto.response;

public record TokenResponse(
    String accessToken,
    String refreshToken,
    long accessTokenLifeTime,
    long refreshTokenLifeTime
) {}
