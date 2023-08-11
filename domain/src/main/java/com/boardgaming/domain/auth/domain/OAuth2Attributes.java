package com.boardgaming.domain.auth.domain;

import com.boardgaming.domain.user.domain.Role;
import com.boardgaming.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuth2Attributes {
    private Map<String, Object> attributes;
    private String nameAttributesKey;
    private String name;
    private String email;
    private String phoneNumber;
    private String profileImageUrl;
    private OAuth2Provider provider;

    @Builder
    public OAuth2Attributes(
        final Map<String, Object> attributes,
        final String nameAttributesKey,
        final String name,
        final String email,
        final String phoneNumber,
        final String profileImageUrl,
        final OAuth2Provider provider
    ) {
        this.attributes = attributes;
        this.nameAttributesKey = nameAttributesKey;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
    }

    public static OAuth2Attributes of(
        final OAuth2Provider provider,
        final Map<String, Object> attributes
    ) {
        if (provider.equals(OAuth2Provider.GOOGLE)) {
            return ofGoogle("sub", attributes, provider);
        }

        return null;
    }

    private static OAuth2Attributes ofGoogle(
        final String userNameAttributeName,
        final Map<String, Object> attributes,
        final OAuth2Provider provider
    ) {
        return OAuth2Attributes.builder()
            .name(String.valueOf(attributes.get("name")))
            .email(String.valueOf(attributes.get("email")))
            .profileImageUrl(String.valueOf(attributes.get("picture")))
            .attributes(attributes)
            .nameAttributesKey(userNameAttributeName)
            .provider(provider)
            .build();
    }

    public User toUser(
        final String name,
        final String password
    ) {
        return User.builder()
            .email(email)
            .name(name)
            .imageFileUrl(profileImageUrl)
            .password(password)
            .provider(provider)
            .role(Role.USER)
            .build();
    }
}

