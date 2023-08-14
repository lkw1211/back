package com.boardgaming.domain.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class VerifiedEmailTest {

    @Test
    @DisplayName("builder 테스트")
    void test1() {
        //given
        String email = "liame";
        String sessionKey = "yeKnoisses";

        //when
        VerifiedEmail verifiedEmail = VerifiedEmail.builder()
            .email(email)
            .sessionKey(sessionKey)
            .build();

        //then
        assertThat(verifiedEmail.getEmail()).isEqualTo(email);
        assertThat(verifiedEmail.getSessionKey()).isEqualTo(sessionKey);
    }
}