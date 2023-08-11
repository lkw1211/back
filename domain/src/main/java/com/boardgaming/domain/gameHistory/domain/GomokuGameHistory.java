package com.boardgaming.domain.gameHistory.domain;

import com.boardgaming.domain.common.BaseTimeEntity;
import com.boardgaming.domain.room.domain.GomokuRule;
import com.boardgaming.domain.user.domain.User;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GomokuGameHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String blackPlayerId;
    private String blackPlayerName;
    private String blackPlayerEmail;
    private Long blackPlayerBeforeRating;
    private Long blackPlayerAfterRating;
    private String blackPlayerImageFileUrl;
    private String whitePlayerId;
    private String whitePlayerName;
    private String whitePlayerEmail;
    private String whitePlayerImageFileUrl;
    private Long whitePlayerBeforeRating;
    private Long whitePlayerAfterRating;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private final List<Long> moveStack = new ArrayList<>();

    @Transient
    private final GomokuBoard board = GomokuBoard.builder().build();

    @Enumerated(value = EnumType.STRING)
    private GomokuRule rule;

    private Long turnTime;

    @Enumerated(value = EnumType.STRING)
    private GomokuGameResult result;

    @Enumerated(value = EnumType.STRING)
    private GomokuGameResultReason reason;

    @Builder
    public GomokuGameHistory(
        final User blackPlayer,
        final User whitePlayer,
        final Long blackPlayerBeforeRating,
        final Long whitePlayerBeforeRating,
        final GomokuRule rule,
        final Long turnTime,
        final GomokuGameResult result,
        final GomokuGameResultReason reason
    ) {
        this.blackPlayerId = blackPlayer.getId();
        this.blackPlayerName = blackPlayer.getName();
        this.blackPlayerEmail = blackPlayer.getEmail();
        this.blackPlayerBeforeRating = blackPlayerBeforeRating;
        this.blackPlayerImageFileUrl = blackPlayer.getImageFileUrl();
        this.whitePlayerId = whitePlayer.getId();
        this.whitePlayerName = whitePlayer.getName();
        this.whitePlayerEmail = whitePlayer.getEmail();
        this.whitePlayerImageFileUrl = whitePlayer.getImageFileUrl();
        this.whitePlayerBeforeRating = whitePlayerBeforeRating;
        this.rule = rule;
        this.turnTime = turnTime;
        this.result = result;
        this.reason = reason;
    }

    public void updateAfterRating(
        final Long blackPlayerAfterRating,
        final Long whitePlayerAfterRating
    ) {
        this.blackPlayerAfterRating = blackPlayerAfterRating;
        this.whitePlayerAfterRating = whitePlayerAfterRating;
    }

    public void addMove(final Long move) {
        this.moveStack.add(move);

        if (this.board.getPieceCnt() != this.moveStack.size() - 1) {
            this.board.initialize(this.moveStack);
        } else {
            GomokuBoard.doMove(this.board, GomokuBoard.rankOf(move.intValue()), GomokuBoard.fileOf(move.intValue()), true);
        }

        if (isFiveInRowEnd()) {
            fiveInRowEnd();
        }
    }

    public void timeoutEnd() {
        if (this.moveStack.size() % 2 == 0) {
            this.result = GomokuGameResult.WHITE_WIN;
        } else {
            this.result = GomokuGameResult.BLACK_WIN;
        }
        this.reason = GomokuGameResultReason.TIME_OUT;
    }

    private boolean isFiveInRowEnd() {
        return Objects.nonNull(this.board.getFiveInRowColor());
    }

    private void fiveInRowEnd() {
        switch (this.board.getFiveInRowColor()) {
            case NONE -> throw new UnsupportedOperationException();
            case BLACK -> {
                this.result = GomokuGameResult.BLACK_WIN;
                this.reason = GomokuGameResultReason.DEFAULT;
            }
            case WHITE -> {
                this.result = GomokuGameResult.WHITE_WIN;
                this.reason = GomokuGameResultReason.DEFAULT;
            }
        }
    }
}
