package com.boardgaming.domain.auth.domain;

public enum OAuth2Provider {
    GOOGLE("google");

    private String registrationId;

    OAuth2Provider(final String registrationId) {
        this.registrationId = registrationId;
    }
}