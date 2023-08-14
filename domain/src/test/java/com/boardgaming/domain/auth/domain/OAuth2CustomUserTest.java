package com.boardgaming.domain.auth.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class OAuth2CustomUserTest {

    private Map<String, Object> attributes;
    private List<GrantedAuthority> authorities;
    private String userId;

    //given
    @BeforeEach
    void setUp() {
        attributes = new HashMap<>();
        attributes.put("name", "John Doe");
        attributes.put("email", "johndoe@example.com");

        authorities = Collections.singletonList(() -> "ROLE_USER");
        userId = "123456";
    }

    @Test
    @DisplayName("전체 생성자 및 getter 메소드 테스트")
    void test1() {
        //when
        OAuth2CustomUser customUser = new OAuth2CustomUser(attributes, authorities, userId);

        //then
        assertThat(customUser.getAttributes()).isEqualTo(attributes);
        assertThat(customUser.getAuthorities()).isEqualTo(authorities);
        assertThat(customUser.getName()).isEqualTo(userId);
    }
}