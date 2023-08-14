package com.boardgaming.domain.gameQueue.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GomokuGameQueueTest {
    @Test
    @DisplayName("isPlayableRating 메소드 테스트")
    void test1() {
        //given
        GomokuGameQueue gameQueue1 = GomokuGameQueue.builder()
            .userId("userId1")
            .rating(1200L)
            .build();

        GomokuGameQueue gameQueue2 = GomokuGameQueue.builder()
            .userId("userId2")
            .rating(1350L)
            .build();

        GomokuGameQueue gameQueue3 = GomokuGameQueue.builder()
            .userId("userId3")
            .rating(1500L)
            .build();

        //when
        Boolean isPlayable12 = GomokuGameQueue.isPlayableRating(gameQueue1, gameQueue2);
        Boolean isPlayable21 = GomokuGameQueue.isPlayableRating(gameQueue2, gameQueue1);
        Boolean isPlayable13 = GomokuGameQueue.isPlayableRating(gameQueue1, gameQueue3);
        Boolean isPlayable31 = GomokuGameQueue.isPlayableRating(gameQueue3, gameQueue1);
        Boolean isPlayable23 = GomokuGameQueue.isPlayableRating(gameQueue2, gameQueue3);
        Boolean isPlayable32 = GomokuGameQueue.isPlayableRating(gameQueue3, gameQueue2);

        //then
        assertThat(isPlayable12).isEqualTo(isPlayable21);
        assertThat(isPlayable13).isEqualTo(isPlayable31);
        assertThat(isPlayable23).isEqualTo(isPlayable32);
        assertThat(isPlayable12).isEqualTo(true);
        assertThat(isPlayable13).isEqualTo(false);
        assertThat(isPlayable23).isEqualTo(true);
    }
}