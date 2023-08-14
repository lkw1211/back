package com.boardgaming.gomoku_ws.application.room.command;

import com.boardgaming.common.exception.room.NotFoundGomokuRoomException;
import com.boardgaming.common.exception.room.NotYourTurnException;
import com.boardgaming.common.exception.user.NotFoundUserException;
import com.boardgaming.domain.config.GomokuRoomIdGenerator;
import com.boardgaming.domain.gameHistory.domain.GomokuGameHistory;
import com.boardgaming.domain.room.domain.GomokuColor;
import com.boardgaming.domain.room.domain.GomokuRoom;
import com.boardgaming.domain.room.domain.GomokuRule;
import com.boardgaming.domain.room.domain.repository.GomokuRoomIdRedisRepository;
import com.boardgaming.domain.room.domain.repository.GomokuRoomRedisRepository;
import com.boardgaming.domain.room.domain.repository.GomokuRoomRepositoryCustom;
import com.boardgaming.domain.room.dto.request.GomokuMoveRequest;
import com.boardgaming.domain.room.dto.response.GomokuRoomListResponse;
import com.boardgaming.domain.room.dto.response.GomokuRoomResponse;
import com.boardgaming.domain.user.domain.User;
import com.boardgaming.domain.user.domain.repository.UserRepository;
import com.boardgaming.gomoku_ws.application.gameHistory.command.GomokuGameHistoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class GomokuRoomService {
    private final GomokuRoomRedisRepository gomokuRoomRedisRepository;
    private final GomokuRoomIdRedisRepository gomokuRoomIdRedisRepository;
    private final UserRepository userRepository;
    private final GomokuGameHistoryService gomokuGameHistoryService;
    private final GomokuRoomIdGenerator gomokuRoomIdGenerator;
    private final GomokuRoomRepositoryCustom gomokuRoomRepositoryCustom;
    private final SimpMessagingTemplate template;
    private final Long defaultTurnTime;

    public GomokuRoomService(
        final GomokuRoomRedisRepository gomokuRoomRedisRepository,
        final UserRepository userRepository,
        final GomokuRoomIdRedisRepository gomokuRoomIdRedisRepository,
        final GomokuGameHistoryService gomokuGameHistoryService,
        final GomokuRoomIdGenerator gomokuRoomIdGenerator,
        final GomokuRoomRepositoryCustom gomokuRoomRepositoryCustom,
        final SimpMessagingTemplate template,
        @Value("${gomoku.setting.defaultTurnTime}") final Long defaultTurnTime
    ) {
        this.gomokuRoomRedisRepository = gomokuRoomRedisRepository;
        this.userRepository = userRepository;
        this.gomokuRoomIdRedisRepository = gomokuRoomIdRedisRepository;
        this.gomokuGameHistoryService = gomokuGameHistoryService;
        this.gomokuRoomIdGenerator = gomokuRoomIdGenerator;
        this.gomokuRoomRepositoryCustom = gomokuRoomRepositoryCustom;
        this.template = template;
        this.defaultTurnTime = defaultTurnTime;
    }

    private void save(final GomokuRoom room) {
        gomokuRoomRedisRepository.save(room);
        gomokuRoomIdRedisRepository.save(room);
    }

    private void deleteById(final Long id) {
        gomokuRoomRedisRepository.deleteById(id);
        gomokuRoomIdRedisRepository.deleteById(id);
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

        GomokuRoom room = GomokuRoom.builder()
            .id(gomokuRoomIdGenerator.generateNextRoomId())
            .rule(rule)
            .turnTime(defaultTurnTime)
            .turnTimeEnd(System.currentTimeMillis() + defaultTurnTime * 1000)
            .blackPlayer(blackPlayer)
            .whitePlayer(whitePlayer)
            .gomokuGameHistoryId(history.getId())
            .gameTurn(GomokuColor.BLACK)
            .build();

        save(room);

        return room;
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

        if (!room.isTurnOf(user.getId())) {
            throw new NotYourTurnException();
        }

        room.doMove(request.getMove());
;       gomokuGameHistoryService.addMove(room.getGomokuGameHistoryId(), request.getMove());

        if (Objects.nonNull(room.getFiveInRowColor())) {
            gomokuGameHistoryService.fiveInRowEnd(room.getGomokuGameHistoryId(), room.getFiveInRowColor());
            deleteById(room.getId());
        } else {
            room.updateTurnState();
        }

        GomokuRoomResponse response = gomokuRoomRepositoryCustom.getRoomDetail(room);
        template.convertAndSend("/sub/room/" + response.id(), response);
    }

    @Transactional
    public GomokuRoomResponse enterRoomPlayer(
        final String userId,
        final Long roomId
    ) {
        return gomokuRoomRedisRepository.findById(roomId)
            .filter(room -> room.includePlayer(userId))
            .map(gomokuRoomRepositoryCustom::getRoomDetail)
            .orElse(null);
    }

    @Transactional
    public GomokuRoomResponse enterRoomWatcher (
        final String userId,
        final Long roomId
    ) {
        return gomokuRoomRedisRepository.findById(roomId)
            .map(gomokuRoomRepositoryCustom::getRoomDetail)
            .orElse(null);
    }

    @Transactional
    public void endRoom(
        final String userId,
        final Long roomId
    ) {
        gomokuRoomRedisRepository.findById(roomId)
            .filter(room -> room.includePlayer(userId))
            .ifPresent(this::endRoom);
    }

    @Transactional
    public void endRoom(
        final GomokuRoom room
    ) {
        if (room.isEnd()) {
            gomokuGameHistoryService.timeoutEnd(room.getGomokuGameHistoryId());
            deleteById(room.getId());

            GomokuRoomResponse response = gomokuRoomRepositoryCustom.getRoomDetail(room);
            template.convertAndSend("/sub/room/" + response.id(), response);
        }
    }

    public void deleteGomokuRooms(final Collection<GomokuRoom> gomokuRooms) {
        List<GomokuRoom> toDeleteList = gomokuRooms.stream()
            .filter(GomokuRoom::needDelete)
            .toList();

        gomokuRoomIdRedisRepository.deleteAll(toDeleteList);
        gomokuRoomRedisRepository.deleteAll(toDeleteList);
    }

    public void sendWaitingRoomList(final List<GomokuRoomListResponse> waitingRoomList) {
        template.convertAndSend("/sub/room", waitingRoomList);
    }
}
