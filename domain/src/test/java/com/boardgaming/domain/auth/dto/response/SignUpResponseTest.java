package com.boardgaming.domain.auth.dto.response;

import com.boardgaming.domain.user.domain.Role;
import com.boardgaming.domain.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SignUpResponseTest {
    //given
    private final User user = User.builder()
        .email("John@Doe.mail")
        .name("John Doe")
        .imageFileUrl("https://example.com")
        .password("Doe John")
        .role(Role.USER)
        .build();

    @Test
    @DisplayName("of 메소드 테스트")
    void test1() {
        //when
        SignUpResponse signUpResponse = SignUpResponse.of(user);

        //then
        assertThat(signUpResponse.id()).isEqualTo(user.getId());
    }
}