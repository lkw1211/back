package com.boardgaming.core.config.auth.application;

import com.boardgaming.core.config.auth.provider.JwtTokenProvider;
import com.boardgaming.domain.auth.domain.RefreshToken;
import com.boardgaming.domain.auth.domain.repository.RefreshTokenRepository;
import com.boardgaming.domain.auth.dto.response.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final Long accessTokenLifeTime;
    private final Long refreshTokenLifeTime;

    public TokenService(
        final JwtTokenProvider jwtTokenProvider,
        final RefreshTokenRepository refreshTokenRepository,
        @Value("${custom.jwt.access-token-life-time}") final Long accessTokenLifeTime,
        @Value("${custom.jwt.refresh-token-life-time}") final Long refreshTokenLifeTime
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.accessTokenLifeTime = accessTokenLifeTime;
        this.refreshTokenLifeTime = refreshTokenLifeTime;
    }

    public TokenResponse createTokenResponse(final Authentication authentication) {
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        return new TokenResponse(accessToken, refreshToken, accessTokenLifeTime, refreshTokenLifeTime);
    }

    public String createAccessToken(Authentication authenticate) {
        return jwtTokenProvider.createAccessToken(authenticate);
    }

    public void saveRefreshToken(
        final String userId,
        final String refreshToken
    ) {
        refreshTokenRepository.save(new RefreshToken(userId, refreshToken, refreshTokenLifeTime));
    }

    public void deleteRefreshToken(
        final String userId
    ) {
        refreshTokenRepository.deleteById(userId);
    }

    public String refreshTokenByUserId(final String refreshToken) {
        return refreshTokenRepository.findByValue(refreshToken).orElseThrow(IllegalArgumentException::new).getUserId();
    }
}
