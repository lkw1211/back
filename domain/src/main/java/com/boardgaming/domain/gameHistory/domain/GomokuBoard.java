package com.boardgaming.domain.gameHistory.domain;


import com.boardgaming.domain.room.domain.GomokuColor;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class GomokuBoard {
    private static final int BOARD_SIZE = 15;
    private static final int BOARD_BOUNDARY = 4;
    private static final int BOARD_SIDE_BIT = 5;
    private static final int[][] DIRECTION = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}};
    private final GomokuColor[][] _board = new GomokuColor[BOARD_SIZE][BOARD_SIZE];
    private int pieceCnt = 0;
    private GomokuColor turn = GomokuColor.BLACK;
    private GomokuColor fiveInRowColor;

    @Builder
    public GomokuBoard() {
        initialize(List.of());
    }

    public static int rankOf(final int move) {
        return (move >> BOARD_SIDE_BIT) - BOARD_BOUNDARY;
    }

    public static int fileOf(final int move) {
        return (move & ((1 << BOARD_SIDE_BIT) - 1)) - BOARD_BOUNDARY;
    }

    public void initialize(final List<Long> moveStack) {
        for (GomokuColor[] row : this._board) {
            Arrays.fill(row, GomokuColor.NONE);
        }
        this.pieceCnt = 0;
        this.turn = GomokuColor.BLACK;
        this.fiveInRowColor = null;

        for (int i = 0; i < moveStack.size(); i++) {
            int move = moveStack.get(i).intValue();
            doMove(this, rankOf(move), fileOf(move), i == moveStack.size() - 1);
        }
    }

    public static void doMove(
        final GomokuBoard board,
        final int rank,
        final int file,
        final boolean checkFiveInRow
    ) {
        board._board[rank][file] = board.turn;
        board.pieceCnt += 1;
        if (board.pieceCnt > 8 && checkFiveInRow) {
            checkFiveInRow(board, rank, file);
        }
        board.turn = board.turn.opposite();
    }

    private static void checkFiveInRow(
        final GomokuBoard board,
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

                if (board._board[checkRank][checkFile].equals(board.turn)) {
                    if (streak > 3) {
                        board.fiveInRowColor = board.turn;
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
