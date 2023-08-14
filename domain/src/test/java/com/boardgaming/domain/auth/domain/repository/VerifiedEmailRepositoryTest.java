package com.boardgaming.domain.auth.domain.repository;

import com.boardgaming.domain.auth.domain.VerifiedEmail;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import redis.embedded.RedisServer;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
class VerifiedEmailRepositoryTest {
    private static RedisServer redisServer;
    @Autowired
    private VerifiedEmailRepository repository;

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
        repository.deleteAll();
    }

    @Test
    @DisplayName("findByEmailAndSessionKey 메소드 테스트")
    void test1() {
        //given
        VerifiedEmail verifiedEmail = VerifiedEmail.builder()
            .email("email")
            .sessionKey("sessionKey")
            .build();

        //when
        repository.save(verifiedEmail);
        VerifiedEmail verifiedEmail2 = repository.findByEmailAndSessionKey(verifiedEmail.getEmail(), verifiedEmail.getSessionKey())
            .orElseThrow(RuntimeException::new);

        //then
        assertThat(verifiedEmail2.getEmail()).isEqualTo(verifiedEmail.getEmail());
        assertThat(verifiedEmail2.getSessionKey()).isEqualTo(verifiedEmail.getSessionKey());
    }
}