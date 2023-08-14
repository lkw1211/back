package com.boardgaming.domain.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserTest {
    @Test
    @DisplayName("regenerateId 메소드 테스트")
    void regenerateId() {
        //given
        User user = User.builder()
            .build();

        String userIdBefore = user.getId();

        //when
        user.regenerateId();
        String userIdAfter = user.getId();

        //then
        assertThat(userIdBefore).isNotEqualTo(userIdAfter);
    }

    @Test
    @DisplayName("updatePassword 메소드 테스트")
    void updatePassword() {
        //given
        String beforePassword = "beforePassword";
        String afterPassword = "afterPassword";

        User user = User.builder()
            .password(beforePassword)
            .build();

        //when
        user.updatePassword(afterPassword);

        //then
        assertThat(user.getPassword()).isEqualTo(afterPassword);
    }

    @Test
    @DisplayName("updateName 메소드 테스트")
    void updateName() {
        //given
        String beforeName = "beforeName";
        String afterName = "afterName";

        User user = User.builder()
            .name(beforeName)
            .build();

        //when
        user.updateName(afterName);

        //then
        assertThat(user.getName()).isEqualTo(afterName);
    }

    @Test
    @DisplayName("updateImageFileUrl 메소드 테스트")
    void updateImageFileUrl() {
        //given
        String beforeImageFileUrl = "beforeImageFileUrl";
        String afterImageFileUrl = "afterImageFileUrl";

        User user = User.builder()
            .imageFileUrl(beforeImageFileUrl)
            .build();

        //when
        user.updateName(afterImageFileUrl);

        //then
        assertThat(user.getName()).isEqualTo(afterImageFileUrl);
    }
}