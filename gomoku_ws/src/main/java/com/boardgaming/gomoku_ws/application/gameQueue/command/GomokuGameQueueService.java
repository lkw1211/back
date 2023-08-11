package com.boardgaming.gomoku_ws.application.gameQueue.command;

import com.boardgaming.common.exception.user.NotFoundUserException;
import com.boardgaming.domain.gameQueue.domain.GomokuGameQueue;
import com.boardgaming.domain.gameQueue.domain.repository.GomokuGameQueueRedisRepository;
import com.boardgaming.domain.gameQueue.dto.response.GameQueueSubResponse;
import com.boardgaming.domain.room.domain.GomokuRoom;
import com.boardgaming.domain.room.domain.GomokuRule;
import com.boardgaming.domain.user.domain.User;
import com.boardgaming.domain.user.domain.repository.UserRepository;
import com.boardgaming.gomoku_ws.application.room.command.GomokuRoomService;
import com.boardgaming.gomoku_ws.application.userHistory.query.GomokuUserHistoryQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GomokuGameQueueService {
    private final UserRepository userRepository;
    private final GomokuGameQueueRedisRepository gomokuGameQueueRedisRepository;
    private final SimpMessagingTemplate template;
    private final GomokuRoomService gomokuRoomService;
    private final GomokuUserHistoryQuery gomokuUserHistoryQuery;

    @Transactional
    public GameQueueSubResponse addGameQueue(final String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(NotFoundUserException::new);

        Long rating = gomokuUserHistoryQuery.getUserRating(user);

        gomokuGameQueueRedisRepository.save(
            GomokuGameQueue.builder()
                .userId(userId)
                .rating(rating)
                .build());

        return GameQueueSubResponse.of("/sub/game-queue/" + userId);
    }

    @Transactional
    public void removeGameQueue(final String userId) {
        gomokuGameQueueRedisRepository.deleteById(userId);
    }

    @Transactional
    public void matchGameQueue(final Collection<String> userIds) {
        gomokuGameQueueRedisRepository.deleteAllById(userIds);

        Map<String, User> playerMap = userRepository.findAllById(userIds)
            .stream()
            .collect(Collectors.toMap(User::getId, Function.identity()));

        String blackUserId = gomokuUserHistoryQuery.getLowBlackPlayRateUserId(userIds);
        String whiteUserId = userIds.stream()
            .filter(userId -> !userId.equals(blackUserId))
            .findFirst()
            .orElseThrow(NotFoundUserException::new);

        GomokuRoom gomokuRoom = gomokuRoomService.createRoom(
            playerMap.get(blackUserId),
            gomokuUserHistoryQuery.getGomokuUserHistory(blackUserId).rating(),
            playerMap.get(whiteUserId),
            gomokuUserHistoryQuery.getGomokuUserHistory(whiteUserId).rating(),
            GomokuRule.RENJU
        );

        userIds.forEach(userId -> template.convertAndSend("/sub/game-queue/" + userId, gomokuRoom.getId()));
    }
}
