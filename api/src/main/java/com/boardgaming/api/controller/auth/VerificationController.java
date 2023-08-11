package com.boardgaming.api.controller.auth;

import com.boardgaming.api.application.auth.command.VerificationService;
import com.boardgaming.domain.user.dto.request.CheckEmailVerificationCodeRequest;
import com.boardgaming.domain.user.dto.request.SendEmailVerificationCodeRequest;
import com.boardgaming.domain.user.dto.response.ValidCheckResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/verification")
public class VerificationController {
    private final VerificationService verificationService;

    @PostMapping("/email/send/code")
    public ResponseEntity<Void> sendEmailVerificationCode(
        @RequestBody @Valid final SendEmailVerificationCodeRequest request
    ) {
        verificationService.sendEmailVerificationCode(request);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/email/check/code")
    public ResponseEntity<ValidCheckResponse> checkEmailVerificationCode(
        @RequestBody @Valid final CheckEmailVerificationCodeRequest request
    ) {
        return ResponseEntity.ok(verificationService.checkEmailVerificationCode(request));
    }
}
