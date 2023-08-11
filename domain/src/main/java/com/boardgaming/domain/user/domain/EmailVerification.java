package com.boardgaming.domain.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "email-verification", timeToLive = 130)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerification {
    @Id
    @Indexed
    private String email;
    private String verificationCode;
    @Indexed
    private String sessionKey;

    @Builder
    public EmailVerification(
        final String email,
        final String verificationCode,
        final String sessionKey
    ) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.sessionKey = sessionKey;
    }

    public void updateVerification(
        final String verificationCode
    ) {
        this.verificationCode = verificationCode;
    }
}
