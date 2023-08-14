package com.boardgaming.domain.room.domain.repository;

import com.boardgaming.domain.room.domain.GomokuRoom;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GomokuRoomRedisRepository extends CrudRepository<GomokuRoom, Long> {
    List<GomokuRoom> findByBlackPlayerIdOrWhitePlayerId(final String blackPlayerId, final String whitePlayerId);
}
