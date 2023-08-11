package com.boardgaming.domain.room.domain;

import com.boardgaming.domain.common.BaseTimeRedisEntity;
import com.boardgaming.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@RedisHash("gomoku-room")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GomokuRoom extends BaseTimeRedisEntity {
    @Id
    @Indexed
    private Long id;
    private GomokuRule rule;
    private GomokuColor gameTurn;
    private Long turnTime;
    private Long turnTimeEnd;
    @Indexed
    private String blackPlayerId;
    private String blackPlayerName;
    private String blackPlayerEmail;
    private String blackPlayerImageFileUrl;
    @Indexed
    private String whitePlayerId;
    private String whitePlayerName;
    private String whitePlayerEmail;
    private String whitePlayerImageFileUrl;
    private Long gomokuGameHistoryId;
    @Indexed
    private GomokuRoomStatus status;
    private Long pieceCnt;

    @Builder
    public GomokuRoom(
        final Long id,
        final GomokuRule rule,
        final Long turnTime,
        final Long turnTimeEnd,
        final User blackPlayer,
        final User whitePlayer,
        final Long gomokuGameHistoryId,
        final GomokuRoomStatus status,
        final GomokuColor gameTurn
    ) {
        super();

        this.id = id;
        this.rule = rule;
        this.turnTime = turnTime;
        this.turnTimeEnd = turnTimeEnd;
        this.blackPlayerId = blackPlayer.getId();
        this.blackPlayerName = blackPlayer.getName();
        this.blackPlayerEmail = blackPlayer.getEmail();
        this.blackPlayerImageFileUrl = blackPlayer.getImageFileUrl();
        this.whitePlayerId = whitePlayer.getId();
        this.whitePlayerName = whitePlayer.getName();
        this.whitePlayerEmail = whitePlayer.getEmail();
        this.whitePlayerImageFileUrl = whitePlayer.getImageFileUrl();
        this.gomokuGameHistoryId = gomokuGameHistoryId;
        this.status = status;
        this.gameTurn = gameTurn;
        this.pieceCnt = 0L;
    }

    public boolean includePlayer(final String userId) {
        return this.blackPlayerId.equals(userId) || this.whitePlayerId.equals(userId);
    }

    public boolean isTurnOf(final User user) {
        return (this.gameTurn.equals(GomokuColor.BLACK) && this.blackPlayerId.equals(user.getId()))
            || (this.gameTurn.equals(GomokuColor.WHITE) && this.whitePlayerId.equals(user.getId()));
    }

    public void updateTurnState(final boolean isEnd) {
        if (isEnd) {
            this.status = GomokuRoomStatus.END;
            this.turnTimeEnd = System.currentTimeMillis();
        } else {
            changeTurn();
        }
        updateModifiedDate();
    }

    public static boolean isStart(final GomokuRoom room) {
        return room.status.equals(GomokuRoomStatus.START);
    }

    private void changeTurn() {
        this.gameTurn = this.gameTurn.opposite();
        this.turnTimeEnd = System.currentTimeMillis() + this.turnTime * 1000;
        this.pieceCnt += 1;
    }

    public boolean needEndUpdate() {
        return Objects.nonNull(this.gomokuGameHistoryId) && this.turnTimeEnd <= System.currentTimeMillis() && this.status.equals(GomokuRoomStatus.START);
    }

    public void gameEnd() {
        this.status = GomokuRoomStatus.END;
        updateModifiedDate();
    }

    public boolean notModifiedMoreThanThreeMinutes() {
        return this.getModifiedDate().plusMinutes(3).isBefore(LocalDateTime.now());
    }
}
