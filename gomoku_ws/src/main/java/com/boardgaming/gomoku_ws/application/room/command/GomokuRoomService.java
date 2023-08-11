package com.boardgaming.gomoku_ws.application.room.command;

import com.boardgaming.common.exception.room.InvalidRoomStateException;
import com.boardgaming.common.exception.room.NotFoundGomokuRoomException;
import com.boardgaming.common.exception.room.NotYourTurnException;
import com.boardgaming.common.exception.user.NotFoundUserException;
import com.boardgaming.domain.gameHistory.domain.GomokuGameHistory;
import com.boardgaming.domain.room.domain.GomokuColor;
import com.boardgaming.domain.room.domain.GomokuRoom;
import com.boardgaming.domain.room.domain.GomokuRoomStatus;
import com.boardgaming.domain.room.domain.GomokuRule;
import com.boardgaming.domain.room.domain.repository.GomokuRoomRedisRepository;
import com.boardgaming.domain.room.dto.request.GomokuMoveRequest;
import com.boardgaming.domain.room.dto.response.GomokuEnterRoomResponse;
import com.boardgaming.domain.room.dto.response.GomokuRoomListResponse;
import com.boardgaming.domain.room.dto.response.GomokuRoomResponse;
import com.boardgaming.domain.user.domain.User;
import com.boardgaming.domain.user.domain.repository.UserRepository;
import com.boardgaming.gomoku_ws.application.gameHistory.command.GomokuGameHistoryService;
import com.boardgaming.gomoku_ws.application.room.query.GomokuRoomQuery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class GomokuRoomService {
    private final GomokuRoomRedisRepository gomokuRoomRedisRepository;
    private final UserRepository userRepository;
    private final GomokuGameHistoryService gomokuGameHistoryService;
    private final GomokuRoomIdGenerator gomokuRoomIdGenerator;
    private final GomokuRoomQuery gomokuRoomQuery;
    private final SimpMessagingTemplate template;
    private final Long defaultTurnTime;

    public GomokuRoomService(
        final GomokuRoomRedisRepository gomokuRoomRedisRepository,
        final UserRepository userRepository,
        final GomokuGameHistoryService gomokuGameHistoryService,
        final GomokuRoomIdGenerator gomokuRoomIdGenerator,
        final GomokuRoomQuery gomokuRoomQuery,
        final SimpMessagingTemplate template,
        @Value("${gomoku.setting.defaultTurnTime}") final Long defaultTurnTime
    ) {
        this.gomokuRoomRedisRepository = gomokuRoomRedisRepository;
        this.userRepository = userRepository;
        this.gomokuGameHistoryService = gomokuGameHistoryService;
        this.gomokuRoomIdGenerator = gomokuRoomIdGenerator;
        this.gomokuRoomQuery = gomokuRoomQuery;
        this.template = template;
        this.defaultTurnTime = defaultTurnTime;
    }

    @Transactional
    public GomokuRoom createRoom(
        final User blackPlayer,
        final Long blackPlayerBeforeRating,
        final User whitePlayer,
        final Long whitePlayerBeforeRating,
        final GomokuRule rule
    ) {
        GomokuGameHistory history = gomokuGameHistoryService.create(
            defaultTurnTime,
            rule,
            blackPlayer,
            whitePlayer,
            blackPlayerBeforeRating,
            whitePlayerBeforeRating
        );

        return gomokuRoomRedisRepository.save(GomokuRoom.builder()
            .id(gomokuRoomIdGenerator.generateNextRoomId())
            .rule(rule)
            .turnTime(defaultTurnTime)
            .turnTimeEnd(System.currentTimeMillis() + defaultTurnTime * 1000)
            .blackPlayer(blackPlayer)
            .whitePlayer(whitePlayer)
            .gomokuGameHistoryId(history.getId())
            .status(GomokuRoomStatus.START)
            .gameTurn(GomokuColor.BLACK)
            .build());
    }

    @Transactional
    public void putMove(
        final String userId,
        final Long roomId,
        final GomokuMoveRequest request
    ) {
        User user = userRepository.findById(userId)
            .orElseThrow(NotFoundUserException::new);

        GomokuRoom room = gomokuRoomRedisRepository.findById(roomId)
            .orElseThrow(NotFoundGomokuRoomException::new);

        if (!room.getStatus().equals(GomokuRoomStatus.START)) {
            throw new InvalidRoomStateException();
        }

        if (!room.isTurnOf(user)) {
            throw new NotYourTurnException();
        }

        boolean isEnd = gomokuGameHistoryService.addMove(room.getGomokuGameHistoryId(), request.getMove());

        room.updateTurnState(isEnd);

        gomokuRoomRedisRepository.save(room);

        GomokuRoomResponse response = gomokuRoomQuery.getRoomDetail(room);
        template.convertAndSend("/sub/room/" + response.id(), response);
    }

    @Transactional
    public GomokuEnterRoomResponse enterRoomPlayer(
        final String userId,
        final Long roomId
    ) {
        userRepository.findById(userId)
            .orElseThrow(NotFoundUserException::new);

        GomokuRoom room = gomokuRoomRedisRepository.findById(roomId)
            .orElse(null);

        if (Objects.isNull(room) || !room.includePlayer(userId)) {
            return GomokuEnterRoomResponse.of(null);
        }

        return GomokuEnterRoomResponse.of(gomokuRoomQuery.getRoomDetail(room));
    }

    @Transactional
    public GomokuEnterRoomResponse enterRoomWatcher (
        final String userId,
        final Long roomId
    ) {
        userRepository.findById(userId)
            .orElseThrow(NotFoundUserException::new);

        GomokuRoom room = gomokuRoomRedisRepository.findByIdAndStatus(roomId, GomokuRoomStatus.START)
            .orElseThrow(NotFoundGomokuRoomException::new);

        return GomokuEnterRoomResponse.of(gomokuRoomQuery.getRoomDetail(room));
    }

    @Transactional
    public void endCheck(
        final String userId,
        final Long roomId
    ) {
        GomokuRoom room = gomokuRoomRedisRepository.findById(roomId)
            .orElseThrow(NotFoundGomokuRoomException::new);

        if (room.includePlayer(userId)) {
            endCheck(room);
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void endCheck(
        final GomokuRoom room
    ) {
        if (room.needEndUpdate()) {
            gomokuGameHistoryService.timeoutEnd(room.getGomokuGameHistoryId());

            room.gameEnd();
            gomokuRoomRedisRepository.save(room);

            GomokuRoomResponse response = gomokuRoomQuery.getRoomDetail(room);
            template.convertAndSend("/sub/room/" + response.id(), response);
        }
    }

    public void deleteGomokuRooms(final Collection<GomokuRoom> gomokuRooms) {
        gomokuRoomRedisRepository.deleteAll(gomokuRooms);
    }

    public void sendWaitingRoomList(final List<GomokuRoomListResponse> waitingRoomList) {
        template.convertAndSend("/sub/room", waitingRoomList);
    }
}
