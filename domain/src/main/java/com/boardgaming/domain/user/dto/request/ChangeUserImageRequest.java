package com.boardgaming.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChangeUserImageRequest {
    @NotBlank
    private String imageFileInfoId;
}
