package com.boardgaming.domain.auth.domain.repository;

import com.boardgaming.domain.auth.domain.RefreshToken;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import redis.embedded.RedisServer;
import static org.assertj.core.api.Assertions.*;

@DataRedisTest
class RefreshTokenRepositoryTest {
    private static RedisServer redisServer;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeAll
    static void startRedisServer() {
        redisServer = new RedisServer();
        redisServer.start();
    }

    @AfterAll
    static void endRedisServer() {
        redisServer.stop();
    }

    @BeforeEach
    void setUp() {
        refreshTokenRepository.deleteAll();
    }

    //given
    RefreshToken refreshToken = new RefreshToken("userId", "value", 3600L);

    @Test
    @DisplayName("existsByValue 테스트")
    void test1() {
        //when1
        refreshTokenRepository.save(refreshToken);

        //then1
        assertThat(refreshTokenRepository.existsByValue(refreshToken.getValue())).isEqualTo(true);

        //when2
        refreshTokenRepository.delete(refreshToken);

        //then2
        assertThat(refreshTokenRepository.existsByValue(refreshToken.getValue())).isEqualTo(false);
    }

    @Test
    @DisplayName("findByValue 테스트")
    void test2() {
        //when
        refreshTokenRepository.save(refreshToken);
        RefreshToken findRefreshToken = refreshTokenRepository.findByValue(refreshToken.getValue())
            .orElse(null);

        //then
        assertThat(findRefreshToken).isNotNull();
        assertThat(findRefreshToken.getTimeout()).isEqualTo(refreshToken.getTimeout());
        assertThat(findRefreshToken.getUserId()).isEqualTo(refreshToken.getUserId());
        assertThat(findRefreshToken.getValue()).isEqualTo(refreshToken.getValue());
    }
}