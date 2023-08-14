package com.boardgaming.domain.user.domain.repository;

import com.boardgaming.domain.auth.domain.OAuth2Provider;
import com.boardgaming.domain.user.domain.Role;
import com.boardgaming.domain.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

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
    void findByEmail() {
        //when
        User find1 = userRepository.findByEmail(user1.getEmail())
            .orElseThrow(RuntimeException::new);
        User find2 = userRepository.findByEmail(user2.getEmail())
            .orElseThrow(RuntimeException::new);

        //then
        assertThat(find1.getEmail()).isEqualTo(user1.getEmail());
        assertThat(find1.getId()).isEqualTo(user1.getId());
        assertThat(find1.getName()).isEqualTo(user1.getName());
        assertThat(find1.getImageFileUrl()).isEqualTo(user1.getImageFileUrl());
        assertThat(find2.getEmail()).isEqualTo(user2.getEmail());
        assertThat(find2.getId()).isEqualTo(user2.getId());
        assertThat(find2.getName()).isEqualTo(user2.getName());
        assertThat(find2.getImageFileUrl()).isEqualTo(user2.getImageFileUrl());
    }

    @Test
    void existsByName() {
        //when
        boolean result1 = userRepository.existsByName(user1.getName());
        boolean result2 = userRepository.existsByName(user2.getName());
        boolean result3 = userRepository.existsByName("testName");

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isTrue();
        assertThat(result3).isFalse();
    }

    @Test
    void existsByEmail() {
        //when
        boolean result1 = userRepository.existsByEmail(user1.getEmail());
        boolean result2 = userRepository.existsByEmail(user2.getEmail());
        boolean result3 = userRepository.existsByEmail("testEmail");

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isTrue();
        assertThat(result3).isFalse();
    }

    @Test
    void findByEmailAndProvider() {
        //when
        Optional<User> result1 = userRepository.findByEmailAndProvider(user1.getEmail(), OAuth2Provider.GOOGLE);
        Optional<User> result2 = userRepository.findByEmailAndProvider(user2.getEmail(), OAuth2Provider.GOOGLE);

        //then
        assertThat(result1.isPresent()).isTrue();
        assertThat(result1.get().getEmail()).isEqualTo(user1.getEmail());
        assertThat(result1.get().getName()).isEqualTo(user1.getName());
        assertThat(result1.get().getId()).isEqualTo(user1.getId());
        assertThat(result1.get().getImageFileUrl()).isEqualTo(user1.getImageFileUrl());
        assertThat(result2.isPresent()).isFalse();
    }
}