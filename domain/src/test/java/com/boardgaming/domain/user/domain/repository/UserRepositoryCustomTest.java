package com.boardgaming.domain.user.domain.repository;

import com.boardgaming.domain.auth.domain.OAuth2Provider;
import com.boardgaming.domain.config.QuerydslConfig;
import com.boardgaming.domain.user.domain.Role;
import com.boardgaming.domain.user.domain.User;
import com.boardgaming.domain.user.dto.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(value = { UserRepositoryCustom.class, QuerydslConfig.class })
@DataJpaTest
class UserRepositoryCustomTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepositoryCustom userRepositoryCustom;

    private final User user1 = User.builder()
        .email("user1-email")
        .name("user1-name")
        .imageFileUrl("user1-imageFileUrl")
        .provider(OAuth2Provider.GOOGLE)
        .role(Role.USER)
        .build();

    private final User user2 = User.builder()
        .email("user2-email")
        .name("user2-name")
        .imageFileUrl("user2-imageFileUrl")
        .role(Role.USER)
        .build();

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
        userRepository.saveAll(List.of(user1, user2));
    }

    @Test
    @DisplayName("getUserInfo 메소드 테스트")
    void getUserInfo() {
        //when
        UserResponse response1 = userRepositoryCustom.getUserInfo(user1.getEmail());
        UserResponse response2 = userRepositoryCustom.getUserInfo(user2.getEmail());
        UserResponse response3 = userRepositoryCustom.getUserInfo("hello bros!");

        //then
        assertThat(response1.id()).isEqualTo(user1.getId());
        assertThat(response1.email()).isEqualTo(user1.getEmail());
        assertThat(response1.name()).isEqualTo(user1.getName());
        assertThat(response1.role()).isEqualTo(user1.getRole());
        assertThat(response1.imageFileUrl()).isEqualTo(user1.getImageFileUrl());
        assertThat(response2.id()).isEqualTo(user2.getId());
        assertThat(response2.email()).isEqualTo(user2.getEmail());
        assertThat(response2.name()).isEqualTo(user2.getName());
        assertThat(response2.role()).isEqualTo(user2.getRole());
        assertThat(response2.imageFileUrl()).isEqualTo(user2.getImageFileUrl());
        assertThat(response3).isNull();
    }

    @Test
    @DisplayName("findById 메소드 테스트")
    void findById() {

    }
}