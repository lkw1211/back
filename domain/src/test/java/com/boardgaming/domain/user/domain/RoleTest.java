package com.boardgaming.domain.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RoleTest {
    @Test
    @DisplayName("getAuthority 메소드 테스트")
    void getAuthority() {
        //given
        Role user = Role.USER;
        Role admin = Role.ADMIN;
        String userAuthority = "ROLE_USER";
        String adminAuthority = "ROLE_ADMIN";

        //when
        Object result1 = user.getAuthority();
        Object result2 = admin.getAuthority();

        //then
        assertThat(result1).isEqualTo(userAuthority);
        assertThat(result2).isEqualTo(adminAuthority);
    }
}