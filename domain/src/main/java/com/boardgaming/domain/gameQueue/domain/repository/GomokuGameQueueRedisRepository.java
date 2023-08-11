package com.boardgaming.domain.gameQueue.domain.repository;

import com.boardgaming.domain.gameQueue.domain.GomokuGameQueue;
import org.springframework.data.repository.CrudRepository;

public interface GomokuGameQueueRedisRepository extends CrudRepository<GomokuGameQueue, String> {
}
