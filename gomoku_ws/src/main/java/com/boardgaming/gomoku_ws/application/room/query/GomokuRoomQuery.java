package com.boardgaming.gomoku_ws.application.room.query;

import com.boardgaming.common.exception.user.NotFoundUserException;
import com.boardgaming.domain.room.domain.GomokuRoom;
import com.boardgaming.domain.room.domain.repository.GomokuRoomIdRedisRepository;
import com.boardgaming.domain.room.domain.repository.GomokuRoomRedisRepository;
import com.boardgaming.domain.room.dto.response.GomokuRoomListResponse;
import com.boardgaming.domain.user.domain.User;
import com.boardgaming.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GomokuRoomQuery {
    private final GomokuRoomRedisRepository gomokuRoomRedisRepository;
    private final GomokuRoomIdRedisRepository gomokuRoomIdRedisRepository;
    private final UserRepository userRepository;

    public List<GomokuRoomListResponse> getAllRoomResponseListReverseOrder() {
        List<Long> gomokuRoomIds = gomokuRoomIdRedisRepository.findAllOrderByDesc();

        return StreamSupport.stream(gomokuRoomRedisRepository.findAllById(gomokuRoomIds).spliterator(), false)
            .map(GomokuRoomListResponse::of)
            .toList();
    }

    public List<GomokuRoom> getAllRoomList() {
        return StreamSupport.stream(gomokuRoomRedisRepository.findAll().spliterator(), false)
            .toList();
    }

    public GomokuRoomListResponse getMyGameRoom(final String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(NotFoundUserException::new);

        return gomokuRoomRedisRepository.findByBlackPlayerIdOrWhitePlayerId(userId, userId)
            .stream()
            .filter(GomokuRoom::isStart)
            .findAny()
            .map(GomokuRoomListResponse::of)
            .orElse(null);
    }
}
