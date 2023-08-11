package com.boardgaming.gomoku_ws.application.room.query;

import com.boardgaming.common.exception.user.NotFoundUserException;
import com.boardgaming.domain.gameHistory.domain.GomokuGameHistory;
import com.boardgaming.domain.room.domain.GomokuRoom;
import com.boardgaming.domain.room.domain.GomokuRoomStatus;
import com.boardgaming.domain.room.domain.repository.GomokuRoomRedisRepository;
import com.boardgaming.domain.room.dto.response.GomokuRoomListResponse;
import com.boardgaming.domain.room.dto.response.GomokuRoomResponse;
import com.boardgaming.domain.room.dto.response.GomokuUserResponse;
import com.boardgaming.domain.user.domain.User;
import com.boardgaming.domain.user.domain.repository.UserRepository;
import com.boardgaming.domain.userHistory.dto.response.GomokuUserHistoryResponse;
import com.boardgaming.gomoku_ws.application.gameHistory.query.GomokuGameHistoryQuery;
import com.boardgaming.gomoku_ws.application.userHistory.query.GomokuUserHistoryQuery;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class GomokuRoomQuery {
    private final GomokuRoomRedisRepository gomokuRoomRedisRepository;
    private final GomokuUserHistoryQuery gomokuUserHistoryQuery;
    private final GomokuGameHistoryQuery gomokuGameHistoryQuery;
    private final SimpUserRegistry simpUserRegistry;
    private final UserRepository userRepository;

    public GomokuRoomQuery(
        final GomokuRoomRedisRepository gomokuRoomRedisRepository,
        final GomokuUserHistoryQuery gomokuUserHistoryQuery,
        final GomokuGameHistoryQuery gomokuGameHistoryQuery,
        final SimpUserRegistry simpUserRegistry,
        final UserRepository userRepository
    ) {
        this.gomokuRoomRedisRepository = gomokuRoomRedisRepository;
        this.gomokuUserHistoryQuery = gomokuUserHistoryQuery;
        this.gomokuGameHistoryQuery = gomokuGameHistoryQuery;
        this.simpUserRegistry = simpUserRegistry;
        this.userRepository = userRepository;
    }

    public List<GomokuRoomListResponse> getAllStartRoomList() {
        return gomokuRoomRedisRepository.findAllByStatus(GomokuRoomStatus.START)
            .stream()
            .sorted(Collections.reverseOrder(Comparator.comparing(GomokuRoom::getCreatedDate, LocalDateTime::compareTo)))
            .map(GomokuRoomListResponse::of)
            .toList();
    }

    public GomokuRoomListResponse getMyGameRoom(final String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(NotFoundUserException::new);

        return gomokuRoomRedisRepository.findAllByBlackPlayerIdOrWhitePlayerId(userId, userId)
                .stream()
                .filter(GomokuRoom::isStart)
                .findAny()
                .map(GomokuRoomListResponse::of)
                .orElse(null);
    }

    public List<GomokuRoom> getStartStatusRoomList() {
        return gomokuRoomRedisRepository.findAllByStatus(GomokuRoomStatus.START);
    }

    public List<GomokuRoom> getEndStatusRoomList() {
        return gomokuRoomRedisRepository.findAllByStatus(GomokuRoomStatus.END);
    }

    public Long getRoomSubscriberCnt(final Long roomId) {
        return simpUserRegistry.getUsers()
            .stream()
            .filter(user -> user.getSessions()
                .stream()
                .flatMap(session -> session.getSubscriptions().stream().map(SimpSubscription::getDestination))
                .anyMatch(destination -> destination.equals("/sub/room/"+roomId)))
            .count();
    }

    public GomokuRoomResponse getRoomDetail(final GomokuRoom room) {
        String blackPlayerImageFileUrl = userRepository.findById(room.getBlackPlayerId())
                .map(User::getImageFileUrl)
                .orElse(null);

        GomokuUserResponse blackPlayerResponse = GomokuUserResponse.of(
            room.getBlackPlayerId(),
            room.getBlackPlayerName(),
            room.getBlackPlayerEmail(),
            blackPlayerImageFileUrl
        );

        GomokuUserHistoryResponse blackPlayerGameHistory = gomokuUserHistoryQuery.getGomokuUserHistory(room.getBlackPlayerId());

        String whitePlayerImageFileUrl = userRepository.findById(room.getWhitePlayerId())
                .map(User::getImageFileUrl)
                .orElse(null);

        GomokuUserResponse whitePlayerResponse = GomokuUserResponse.of(
            room.getWhitePlayerId(),
            room.getWhitePlayerName(),
            room.getWhitePlayerEmail(),
            whitePlayerImageFileUrl
        );

        GomokuUserHistoryResponse whitePlayerGameHistory = gomokuUserHistoryQuery.getGomokuUserHistory(room.getWhitePlayerId());

        GomokuGameHistory gameHistory = gomokuGameHistoryQuery.findGameHistory(room.getGomokuGameHistoryId())
            .orElse(null);

        GomokuRoomResponse roomResponse = GomokuRoomResponse.of(
            room,
            blackPlayerResponse,
            blackPlayerGameHistory,
            whitePlayerResponse,
            whitePlayerGameHistory,
            gameHistory
        );

        return roomResponse;
    }
}
