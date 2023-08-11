package com.boardgaming.domain.room.domain;

public enum GomokuColor {
    BLACK,
    WHITE,
    NONE;

    static {
        BLACK.oppositeColor = WHITE;
        WHITE.oppositeColor = BLACK;
        NONE.oppositeColor = NONE;
    }
    private GomokuColor oppositeColor;

    public GomokuColor opposite() {
        return this.oppositeColor;
    }
}
