package com.boardgaming.domain.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class EmailVerificationTest {

    @Test
    @DisplayName("updateVerification 메소드 테스트")
    void test1() {
        //given
        EmailVerification emailVerification = EmailVerification.builder()
            .email("email")
            .verificationCode("123")
            .sessionKey("sessionKey")
            .build();

        //when
        emailVerification.updateVerification("321");

        //then
        assertThat(emailVerification.getVerificationCode()).isEqualTo("321");
        assertThat(emailVerification.getEmail()).isEqualTo("email");
        assertThat(emailVerification.getSessionKey()).isEqualTo("sessionKey");
    }
}