package com.boardgaming.domain.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "verified-email", timeToLive = 3600)
@NoArgsConstructor
public class VerifiedEmail {
    @Id
    @Indexed
    private String email;
    @Indexed
    private String sessionKey;

    @Builder
    public VerifiedEmail(
        final String email,
        final String sessionKey
    ) {
        this.email = email;
        this.sessionKey = sessionKey;
    }
}
