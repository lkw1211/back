package com.boardgaming.domain.room.dto.request;

import lombok.Getter;

@Getter
public class GomokuMoveRequest {
    private Long move;

    @Override
    public String toString() {
        return "GomokuMoveRequest{" +
            "move=" + move +
            '}';
    }
}
