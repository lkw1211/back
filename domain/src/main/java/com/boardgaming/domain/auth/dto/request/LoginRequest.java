package com.boardgaming.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {
    @NotBlank(message = "Enter id plz.")
    private String email;
    @NotBlank(message = "Enter password plz.")
    private String password;
}
