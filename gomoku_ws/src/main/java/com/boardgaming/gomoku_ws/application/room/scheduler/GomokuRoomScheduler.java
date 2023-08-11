package com.boardgaming.gomoku_ws.application.room.scheduler;

import com.boardgaming.domain.room.domain.GomokuRoom;
import com.boardgaming.domain.room.dto.response.GomokuRoomListResponse;
import com.boardgaming.gomoku_ws.application.room.command.GomokuRoomService;
import com.boardgaming.gomoku_ws.application.room.query.GomokuRoomQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GomokuRoomScheduler {
    private final GomokuRoomQuery gomokuRoomQuery;
    private final GomokuRoomService gomokuRoomService;
    private final List<GomokuRoomListResponse> waitingRoomList = new ArrayList<>();

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void updateGomokuRoomStatus() {
        updateStartStatusGomokuRoom();
        deleteEndStatusGomokuRoom();
    }

    private void updateStartStatusGomokuRoom() {
        List<GomokuRoom> startGomokuRoomList = gomokuRoomQuery.getStartStatusRoomList();

        startGomokuRoomList.stream()
            .filter(GomokuRoom::notModifiedMoreThanThreeMinutes)
            .forEach(gomokuRoomService::endCheck);
    }

    private void deleteEndStatusGomokuRoom() {
        List<GomokuRoom> endGomokuRoomList = gomokuRoomQuery.getEndStatusRoomList();

        gomokuRoomService.deleteGomokuRooms(endGomokuRoomList.stream()
            .filter(GomokuRoom::notModifiedMoreThanThreeMinutes)
            .toList());
    }

    @Scheduled(cron = "5/10 * * * * *")
    public void updateWaitingRoomList() {
        List<GomokuRoomListResponse> newWaitingRoomlist = gomokuRoomQuery.getAllStartRoomList();
        if (!waitingRoomList.equals(newWaitingRoomlist)) {
            waitingRoomList.clear();
            waitingRoomList.addAll(newWaitingRoomlist);
            gomokuRoomService.sendWaitingRoomList(waitingRoomList);
        }
    }
}
