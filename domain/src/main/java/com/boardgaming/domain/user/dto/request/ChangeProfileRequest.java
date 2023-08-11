package com.boardgaming.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChangeProfileRequest {
    @NotBlank
    private String name;
}
