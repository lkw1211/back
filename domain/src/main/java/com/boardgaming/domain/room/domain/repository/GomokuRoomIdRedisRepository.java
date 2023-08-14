package com.boardgaming.domain.room.domain.repository;

import com.boardgaming.domain.room.domain.GomokuRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GomokuRoomIdRedisRepository {
    private final ZSetOperations<String, Long> zSetOperations;
    private static final String GomokuRoomRedisHashName = GomokuRoom.getRedisHashName();

    public void deleteAll(final Collection<GomokuRoom> gomokuRooms) {
        zSetOperations.remove(GomokuRoomRedisHashName, gomokuRooms.stream().map(GomokuRoom::getId).toArray());
    }

    public void deleteAll() {
        zSetOperations.removeRange(GomokuRoomRedisHashName, 0, Long.MAX_VALUE);
    }

    public void deleteById(final Long id) {
        zSetOperations.remove(GomokuRoomRedisHashName, id);
    }

    public void save(final GomokuRoom room) {
        zSetOperations.add(GomokuRoomRedisHashName, room.getId(), System.currentTimeMillis());
    }

    public List<Long> findAllOrderByDesc() {
        return Optional.ofNullable(zSetOperations.reverseRange(GomokuRoomRedisHashName, 0, Long.MAX_VALUE))
            .map(set -> set.stream().toList())
            .orElse(List.of());
    }
}
