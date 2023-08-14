package com.boardgaming.domain.room.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GomokuColorTest {

    @Test
    @DisplayName("opposite 메소드 테스트")
    void test1() {
        //given
        GomokuColor color1 = GomokuColor.BLACK;
        GomokuColor color2 = GomokuColor.WHITE;
        GomokuColor color3 = GomokuColor.NONE;
        GomokuColor color1OppositeExpected = GomokuColor.WHITE;
        GomokuColor color2OppositeExpected = GomokuColor.BLACK;
        GomokuColor color3OppositeExpected = GomokuColor.NONE;

        //when
        GomokuColor color1Opposite = color1.opposite();
        GomokuColor color2Opposite = color2.opposite();
        GomokuColor color3Opposite = color3.opposite();

        //then
        assertThat(color1Opposite).isEqualTo(color1OppositeExpected);
        assertThat(color2Opposite).isEqualTo(color2OppositeExpected);
        assertThat(color3Opposite).isEqualTo(color3OppositeExpected);
    }
}