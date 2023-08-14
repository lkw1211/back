package com.boardgaming.domain.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class OAuth2TempAttributesTest {
    //given
    private OAuth2Attributes oAuth2Attributes = OAuth2Attributes.builder()
        .attributes(Map.of(
            "name", "John Doe",
            "email", "johndoe@example.com"
        ))
        .email("johndoe@example.com")
        .profileImageUrl("https://example.com/profile.jpg")
        .provider(OAuth2Provider.GOOGLE)
        .build();

    @Test
    @DisplayName("fromOAuth2Attributes 메소드 테스트")
    void test1() {
        //when
        OAuth2TempAttributes tempAttributes = OAuth2TempAttributes.fromOAuth2Attributes(oAuth2Attributes);

        //then
        assertThat(tempAttributes.getAttributes()).isEqualTo(oAuth2Attributes.getAttributes());
        assertThat(tempAttributes.getNameAttributesKey()).isEqualTo(oAuth2Attributes.getNameAttributesKey());
        assertThat(tempAttributes.getEmail()).isEqualTo(oAuth2Attributes.getEmail());
        assertThat(tempAttributes.getProfileImageUrl()).isEqualTo(oAuth2Attributes.getProfileImageUrl());
        assertThat(tempAttributes.getProvider()).isEqualTo(oAuth2Attributes.getProvider());
    }

    @Test
    @DisplayName("toOAuth2Attributes 메소드 테스트")
    void test2() {
        //given
        OAuth2TempAttributes tempAttributes = OAuth2TempAttributes.fromOAuth2Attributes(oAuth2Attributes);

        //when
        OAuth2Attributes oAuth2Attributes1 = tempAttributes.toOAuth2Attributes();

        //then
        assertThat(oAuth2Attributes1.getAttributes()).isEqualTo(tempAttributes.getAttributes());
        assertThat(oAuth2Attributes1.getNameAttributesKey()).isEqualTo(tempAttributes.getNameAttributesKey());
        assertThat(oAuth2Attributes1.getEmail()).isEqualTo(tempAttributes.getEmail());
        assertThat(oAuth2Attributes1.getProfileImageUrl()).isEqualTo(tempAttributes.getProfileImageUrl());
        assertThat(oAuth2Attributes1.getProvider()).isEqualTo(tempAttributes.getProvider());
    }
}