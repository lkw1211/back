package com.boardgaming.domain.gameQueue.dto.response;

public record GameQueueSubResponse(
    String subUrl
) {
    public static GameQueueSubResponse of(final String subUrl) {
        return new GameQueueSubResponse(
            subUrl
        );
    }
}
