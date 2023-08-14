package com.boardgaming.domain.auth.domain.repository;

import com.boardgaming.domain.auth.domain.EmailVerification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import redis.embedded.RedisServer;

import static org.assertj.core.api.Assertions.*;

@DataRedisTest
class EmailVerificationRepositoryTest {
    private static RedisServer redisServer;
    @Autowired
    private EmailVerificationRepository repository;

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
        EmailVerification emailVerification = EmailVerification.builder()
            .email("email")
            .verificationCode("verificationCode")
            .sessionKey("sessionKey")
            .build();

        //when
        repository.save(emailVerification);
        EmailVerification emailVerification2 = repository.findByEmailAndSessionKey(emailVerification.getEmail(), emailVerification.getSessionKey())
            .orElseThrow(RuntimeException::new);

        //then
        assertThat(emailVerification2.getEmail()).isEqualTo(emailVerification.getEmail());
        assertThat(emailVerification2.getVerificationCode()).isEqualTo(emailVerification.getVerificationCode());
        assertThat(emailVerification2.getSessionKey()).isEqualTo(emailVerification.getSessionKey());
    }
}