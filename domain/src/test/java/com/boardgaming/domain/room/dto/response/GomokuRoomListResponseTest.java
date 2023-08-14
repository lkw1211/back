package com.boardgaming.domain.room.dto.response;

import com.boardgaming.domain.room.domain.GomokuRoom;
import com.boardgaming.domain.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GomokuRoomListResponseTest {
    @Test
    @DisplayName("of 메소드 테스트")
    void of() {
        //given
        User user1 = User.builder()
            .email("user1")
            .name("user1-name")
            .imageFileUrl("user1-imageFileUrl")
            .build();

        User user2 = User.builder()
            .email("user2")
            .name("user2-name")
            .imageFileUrl("user2-imageFileUrl")
            .build();

        GomokuRoom room = GomokuRoom.builder()
            .id(123L)
            .blackPlayer(user1)
            .whitePlayer(user2)
            .build();

        //when
        GomokuRoomListResponse response = GomokuRoomListResponse.of(room);

        //then
        assertThat(response.id()).isEqualTo(room.getId());
        assertThat(response.blackPlayer().id()).isEqualTo(user1.getId());
        assertThat(response.blackPlayer().email()).isEqualTo(user1.getEmail());
        assertThat(response.blackPlayer().name()).isEqualTo(user1.getName());
        assertThat(response.blackPlayer().imageFileUrl()).isEqualTo(user1.getImageFileUrl());
        assertThat(response.whitePlayer().id()).isEqualTo(user2.getId());
        assertThat(response.whitePlayer().email()).isEqualTo(user2.getEmail());
        assertThat(response.whitePlayer().name()).isEqualTo(user2.getName());
        assertThat(response.whitePlayer().imageFileUrl()).isEqualTo(user2.getImageFileUrl());
        assertThat(response.pieceCnt()).isEqualTo(0L);
    }
}