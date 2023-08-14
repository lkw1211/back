package com.boardgaming.domain.room.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GomokuBoardTest {
    @Test
    @DisplayName("rankOf, fileOf, makeMove 메소드 테스트")
    void test1() {
        //given
        List<Integer> moves = new ArrayList<>();
        for (int i = 0; i < 14; ++i) {
            for (int j = 0; j < 14; ++j) {
                moves.add(GomokuBoard.makeMove(i, j));
            }
        }

        List<Integer> ranks = new ArrayList<>();
        List<Integer> files = new ArrayList<>();

        //when
        for (int k = 0; k < moves.size(); ++k) {
            ranks.add(GomokuBoard.rankOf(moves.get(k)));
            files.add(GomokuBoard.fileOf(moves.get(k)));
        }

        //then
        for (int i = 0; i < 14; ++i) {
            for (int j = 0; j < 14; ++j) {
                assertThat(ranks.get(i*14+j)).isEqualTo(i);
                assertThat(files.get(i*14+j)).isEqualTo(j);
            }
        }
    }

    @Test
    @DisplayName("initialize 메소드 테스트")
    void test2() {
        //given
        List<Integer> rankStack = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14);
        List<Integer> fileStack = List.of(14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0);
        List<Integer> moveStack = rankStack.stream().map(rank -> GomokuBoard.makeMove(rank, fileStack.get(rank))).toList();

        //when
        GomokuBoard board = GomokuBoard.initialize(moveStack);

        //then
        for (int i = 0; i < moveStack.size(); i++) {
            assertThat(i % 2 == 0 ? GomokuColor.BLACK : GomokuColor.WHITE).isEqualTo(board.get_board()[rankStack.get(i)][fileStack.get(i)]);
        }
    }

    @Test
    @DisplayName("doMove, checkFiveInRow 메소드 테스트")
    void test3() {
        //given
        List<Integer> moveStack1 = List.of(
            GomokuBoard.makeMove(5,7), GomokuBoard.makeMove(5, 6),
            GomokuBoard.makeMove(6,7), GomokuBoard.makeMove(6, 6),
            GomokuBoard.makeMove(7,7), GomokuBoard.makeMove(7, 6),
            GomokuBoard.makeMove(8,7), GomokuBoard.makeMove(8, 6),
            GomokuBoard.makeMove(9,7), GomokuBoard.makeMove(9, 6)
        );

        List<Integer> moveStack2 = List.of(
            GomokuBoard.makeMove(5,7), GomokuBoard.makeMove(5, 6),
            GomokuBoard.makeMove(6,7), GomokuBoard.makeMove(6, 6),
            GomokuBoard.makeMove(7,7), GomokuBoard.makeMove(7, 6),
            GomokuBoard.makeMove(8,7), GomokuBoard.makeMove(8, 6)
        );

        List<Integer> moveStack3 = List.of(
            GomokuBoard.makeMove(5,7), GomokuBoard.makeMove(5, 6),
            GomokuBoard.makeMove(6,7), GomokuBoard.makeMove(6, 6),
            GomokuBoard.makeMove(7,7), GomokuBoard.makeMove(7, 6),
            GomokuBoard.makeMove(8,7), GomokuBoard.makeMove(8, 6),
            GomokuBoard.makeMove(1,1), GomokuBoard.makeMove(9, 6)
        );

        //when1
        GomokuBoard board = GomokuBoard.initialize(moveStack1);

        //then1
        assertThat(board.getFiveInRowColor()).isEqualTo(GomokuColor.BLACK);

        //when2
        board = GomokuBoard.initialize(moveStack2);

        //then2
        assertThat(board.getFiveInRowColor()).isEqualTo(null);

        //when3
        board = GomokuBoard.initialize(moveStack3);

        //then3
        assertThat(board.getFiveInRowColor()).isEqualTo(GomokuColor.WHITE);
    }
}