package com.boardgaming.domain.room.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GomokuBoard {
    private static final int BOARD_SIZE = 15;
    private static final int BOARD_BOUNDARY = 4;
    private static final int BOARD_SIDE_BIT = 5;
    private static final int[][] DIRECTION = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}};
    private final GomokuColor[][] _board = new GomokuColor[BOARD_SIZE][BOARD_SIZE];
    private int pieceCnt = 0;
    private GomokuColor turn = GomokuColor.BLACK;
    private GomokuColor fiveInRowColor;

    public static int rankOf(final int move) {
        return (move >> BOARD_SIDE_BIT) - BOARD_BOUNDARY;
    }

    public static int fileOf(final int move) {
        return (move & ((1 << BOARD_SIDE_BIT) - 1)) - BOARD_BOUNDARY;
    }
    public static int makeMove(final int rank, final int file) { return ((rank + BOARD_BOUNDARY) << BOARD_SIDE_BIT) + file + BOARD_BOUNDARY; }

    public static GomokuBoard initialize(final List<Integer> moveStack) {
        GomokuBoard board = new GomokuBoard();

        for (GomokuColor[] row : board._board) {
            Arrays.fill(row, GomokuColor.NONE);
        }
        board.pieceCnt = 0;
        board.turn = GomokuColor.BLACK;
        board.fiveInRowColor = null;

        moveStack.forEach(board::doMove);

        return board;
    }

    public void doMove(
        final int move
    ) {
        if (Objects.nonNull(this.fiveInRowColor)) {
            return;
        }
        int rank = rankOf(move);
        int file  = fileOf(move);

        this._board[rank][file] = this.turn;
        this.pieceCnt += 1;
        if (this.pieceCnt > 8) {
            checkFiveInRow(rank, file);
        }
        this.turn = this.turn.opposite();
    }

    private void checkFiveInRow(
        final int rank,
        final int file
    ) {
        for (int[] d : DIRECTION) {
            int streak = 0;
            for (int i = -4; i < 5; i++) {
                int checkRank = rank + i * d[0];
                int checkFile = file + i * d[1];

                if (!isValidMove(checkRank, checkFile)) {
                    continue;
                }

                if (this._board[checkRank][checkFile].equals(this.turn)) {
                    if (streak > 3) {
                        this.fiveInRowColor = this.turn;
                        return;
                    } else {
                        streak += 1;
                    }
                } else if (streak < 1 + i) {
                    break;
                } else {
                    streak = 0;
                }
            }
        }
    }

    private static boolean isValidMove(
        final int rank,
        final int file
    ) {
        return rank >= 0 && rank < BOARD_SIZE && file >= 0 && file < BOARD_SIZE;
    }
}
