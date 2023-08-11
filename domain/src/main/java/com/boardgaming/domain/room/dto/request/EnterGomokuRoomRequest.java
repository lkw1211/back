package com.boardgaming.domain.room.dto.request;

import lombok.Getter;

@Getter
public class EnterGomokuRoomRequest {
    private String userId;
    private Long roomId;
}
