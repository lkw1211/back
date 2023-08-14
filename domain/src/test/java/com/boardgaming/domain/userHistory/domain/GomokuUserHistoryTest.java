package com.boardgaming.domain.userHistory.domain;

import com.boardgaming.domain.room.domain.GomokuColor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GomokuUserHistoryTest {
    @Test
    @DisplayName("updateHistory 메소드 테스트")
    void updateHistory() {
        //given
        GomokuUserHistory history1 = GomokuUserHistory.builder()
            .userId("userId1")
            .rating(1200L)
            .build();

        GomokuUserHistory history2 = GomokuUserHistory.builder()
            .userId("userId2")
            .rating(1300L)
            .build();

        GomokuUserHistory history3 = GomokuUserHistory.builder()
            .userId("userId3")
            .rating(1400L)
            .build();

        GomokuUserHistory history4 = GomokuUserHistory.builder()
            .userId("userId4")
            .rating(1500L)
            .build();

        //when
        history1.updateHistory(GomokuUserResult.WIN, GomokuColor.BLACK, history2.getRating());
        history2.updateHistory(GomokuUserResult.LOSE, GomokuColor.WHITE, history1.getRating());
        history3.updateHistory(GomokuUserResult.DRAW, GomokuColor.BLACK, history4.getRating());
        history4.updateHistory(GomokuUserResult.DRAW, GomokuColor.WHITE, history3.getRating());

        //then
        assertThat(history1.getBlackPlayCnt()).isEqualTo(1);
        assertThat(history1.getWhitePlayCnt()).isEqualTo(0);
        assertThat(history1.getWinCnt()).isEqualTo(1);
        assertThat(history1.getDrawCnt()).isEqualTo(0);
        assertThat(history1.getLoseCnt()).isEqualTo(0);
        assertThat(history1.getRating()).isGreaterThan(1200L);
        assertThat(history2.getBlackPlayCnt()).isEqualTo(0);
        assertThat(history2.getWhitePlayCnt()).isEqualTo(1);
        assertThat(history2.getWinCnt()).isEqualTo(0);
        assertThat(history2.getDrawCnt()).isEqualTo(0);
        assertThat(history2.getLoseCnt()).isEqualTo(1);
        assertThat(history2.getRating()).isLessThan(1300L);
        assertThat(history3.getBlackPlayCnt()).isEqualTo(1);
        assertThat(history3.getWhitePlayCnt()).isEqualTo(0);
        assertThat(history3.getWinCnt()).isEqualTo(0);
        assertThat(history3.getDrawCnt()).isEqualTo(1);
        assertThat(history3.getLoseCnt()).isEqualTo(0);
        assertThat(history3.getRating()).isGreaterThan(1400L);
        assertThat(history4.getBlackPlayCnt()).isEqualTo(0);
        assertThat(history4.getWhitePlayCnt()).isEqualTo(1);
        assertThat(history4.getWinCnt()).isEqualTo(0);
        assertThat(history4.getDrawCnt()).isEqualTo(1);
        assertThat(history4.getLoseCnt()).isEqualTo(0);
        assertThat(history4.getRating()).isLessThan(1500L);
    }

    @Test
    void getBlackPlayRate() {
        //given
        GomokuUserHistory history1 = GomokuUserHistory.builder()
            .userId("userId1")
            .rating(1200L)
            .build();

        GomokuUserHistory history2 = GomokuUserHistory.builder()
            .userId("userId2")
            .rating(1300L)
            .build();

        //when //then
        assertThat(history1.getBlackPlayRate()).isEqualTo(0.5D);
        history1.updateHistory(GomokuUserResult.WIN, GomokuColor.BLACK, history2.getRating());
        assertThat(history1.getBlackPlayRate()).isEqualTo(1D);
        history1.updateHistory(GomokuUserResult.LOSE, GomokuColor.WHITE, history2.getRating());
        assertThat(history1.getBlackPlayRate()).isEqualTo(0.5D);
        history1.updateHistory(GomokuUserResult.DRAW, GomokuColor.BLACK, history2.getRating());
        assertThat(history1.getBlackPlayRate()).isEqualTo(2D / 3);
        history1.updateHistory(GomokuUserResult.DRAW, GomokuColor.WHITE, history2.getRating());
        assertThat(history1.getBlackPlayRate()).isEqualTo(0.5D);
    }
}