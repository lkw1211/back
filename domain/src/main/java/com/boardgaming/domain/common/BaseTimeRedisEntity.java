package com.boardgaming.domain.common;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class BaseTimeRedisEntity {
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public BaseTimeRedisEntity() {
        this.createdDate = LocalDateTime.now();
        this.modifiedDate = this.createdDate;
    }

    public void updateModifiedDate() {
        this.modifiedDate = LocalDateTime.now();
    }
}
