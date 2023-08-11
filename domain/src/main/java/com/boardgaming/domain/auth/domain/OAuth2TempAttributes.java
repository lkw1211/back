package com.boardgaming.domain.auth.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Map;
import java.util.UUID;

@Getter
@RedisHash(value = "OAuth2TempAttributes", timeToLive = 60 * 60 * 24L)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2TempAttributes {
    @Id
    private String key;

    private Map<String, Object> attributes;
    private String nameAttributesKey;
    private String name;
    private String email;
    private String phoneNumber;
    private String profileImageUrl;
    private OAuth2Provider provider;

    private OAuth2TempAttributes(
        final String key,
        final Map<String, Object> attributes,
        final String nameAttributesKey,
        final String name,
        final String email,
        final String phoneNumber,
        final String profileImageUrl,
        final OAuth2Provider provider
    ) {
        this.key = key;
        this.attributes = attributes;
        this.nameAttributesKey = nameAttributesKey;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
    }

    public static OAuth2TempAttributes fromOAuth2Attributes(
        final OAuth2Attributes oAuth2Attributes
    ) {
        return new OAuth2TempAttributes(
            UUID.randomUUID().toString(),
            oAuth2Attributes.getAttributes(),
            oAuth2Attributes.getNameAttributesKey(),
            oAuth2Attributes.getName(),
            oAuth2Attributes.getEmail(),
            oAuth2Attributes.getPhoneNumber(),
            oAuth2Attributes.getProfileImageUrl(),
            oAuth2Attributes.getProvider()
        );
    }

    public OAuth2Attributes toOAuth2Attributes() {
        return new OAuth2Attributes(
            attributes,
            nameAttributesKey,
            name,
            email,
            phoneNumber,
            profileImageUrl,
            provider
        );
    }
}
