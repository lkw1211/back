package com.boardgaming.domain.auth.domain.repository;

import com.boardgaming.domain.auth.domain.OAuth2Attributes;
import com.boardgaming.domain.auth.domain.OAuth2Provider;
import com.boardgaming.domain.auth.domain.OAuth2TempAttributes;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import redis.embedded.RedisServer;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DataRedisTest
class OAuth2TempAttributesRepositoryTest {
    private static RedisServer redisServer;
    @Autowired
    private OAuth2TempAttributesRepository repository;

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
    @DisplayName("저장 및 불러오기 테스트")
    void test1() throws ClassNotFoundException {
        //given
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.builder()
            .attributes(Map.of(
                "name", "John Doe",
                "email", "johndoe@example.com"
            ))
            .email("johndoe@example.com")
            .profileImageUrl("https://example.com/profile.jpg")
            .provider(OAuth2Provider.GOOGLE)
            .build();
        OAuth2TempAttributes tempAttributes = OAuth2TempAttributes.fromOAuth2Attributes(oAuth2Attributes);

        //when
        repository.save(tempAttributes);
        OAuth2TempAttributes tempAttributes2 = repository.findById(tempAttributes.getKey())
            .orElseThrow(RuntimeException::new);

        //then
        assertThat(tempAttributes.getKey()).isEqualTo(tempAttributes2.getKey());
        assertThat(tempAttributes.getAttributes()).isEqualTo(tempAttributes2.getAttributes());
        assertThat(tempAttributes.getNameAttributesKey()).isEqualTo(tempAttributes2.getNameAttributesKey());
        assertThat(tempAttributes.getEmail()).isEqualTo(tempAttributes2.getEmail());
        assertThat(tempAttributes.getProfileImageUrl()).isEqualTo(tempAttributes2.getProfileImageUrl());
        assertThat(tempAttributes.getProvider()).isEqualTo(tempAttributes2.getProvider());
    }
}