package com.boardgaming.domain.gameQueue.domain;

import com.boardgaming.domain.common.BaseTimeRedisEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("gomoku-game-queue")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GomokuGameQueue extends BaseTimeRedisEntity {
    @Id
    private String userId;
    private Long rating;

    @Builder
    public GomokuGameQueue(
        final String userId,
        final Long rating
    ) {
        super();
        this.userId = userId;
        this.rating = rating;
    }

    public static boolean isPlayableRating(
        final GomokuGameQueue target,
        final GomokuGameQueue other
    ) {
        return Math.abs(target.rating - other.rating) <= Math.abs(target.rating) * 0.2;
    }
}
