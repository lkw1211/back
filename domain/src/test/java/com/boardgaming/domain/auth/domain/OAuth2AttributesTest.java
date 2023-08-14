package com.boardgaming.domain.auth.domain;

import com.boardgaming.domain.user.domain.Role;
import com.boardgaming.domain.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class OAuth2AttributesTest {
    private Map<String, Object> attributes;
    private OAuth2Provider provider;

    //given
    @BeforeEach
    void setUp() {
        attributes = new HashMap<>();
        attributes.put("email", "johndoe@example.com");
        attributes.put("picture", "https://example.com/profile.jpg");

        provider = OAuth2Provider.GOOGLE;
    }

    @Test
    @DisplayName("of 메소드 테스트")
    void test1() {
        //when
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(provider, attributes);

        //then
        assertThat(oAuth2Attributes).isNotNull();
        assertThat(oAuth2Attributes.getProvider()).isEqualTo(provider);
        assertThat(oAuth2Attributes.getEmail()).isEqualTo("johndoe@example.com");
        assertThat(oAuth2Attributes.getProfileImageUrl()).isEqualTo("https://example.com/profile.jpg");
        assertThat(oAuth2Attributes.getAttributes()).isEqualTo(attributes);
    }

    @Test
    @DisplayName("toUser 메소드 테스트")
    void test2() {
        //given
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.builder()
            .attributes(attributes)
            .email("johndoe@example.com")
            .profileImageUrl("https://example.com/profile.jpg")
            .provider(provider)
            .build();

        //when
        User user = oAuth2Attributes.toUser("John Doe", "password");

        //then
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("johndoe@example.com");
        assertThat(user.getImageFileUrl()).isEqualTo("https://example.com/profile.jpg");
        assertThat(user.getProvider()).isEqualTo(provider);
        assertThat(user.getRole()).isEqualTo(Role.USER);
    }
}