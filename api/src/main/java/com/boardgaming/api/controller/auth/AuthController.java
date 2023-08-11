package com.boardgaming.api.controller.auth;

import com.boardgaming.api.application.auth.command.SignService;
import com.boardgaming.api.application.user.query.UserQuery;
import com.boardgaming.core.config.annotation.LoginUser;
import com.boardgaming.domain.auth.dto.request.LoginRequest;
import com.boardgaming.domain.auth.dto.request.SignUpRequest;
import com.boardgaming.domain.auth.dto.request.SocialSignUpRequest;
import com.boardgaming.domain.auth.dto.response.SignUpResponse;
import com.boardgaming.domain.user.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final SignService signService;
    private final UserQuery userQuery;

    @PostMapping
    public ResponseEntity<UserResponse> signin(
        @Valid @RequestBody final LoginRequest loginRequest,
        final HttpServletResponse response
    ) {
        return ResponseEntity.ok(signService.signIn(response, loginRequest));
    }

    @DeleteMapping
    public ResponseEntity<Void> signout(
        @LoginUser final String userId,
        final HttpServletRequest request,
        final HttpServletResponse response
    ) {
        signService.signOut(userId, request, response);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(
        @RequestBody @Valid final SignUpRequest request
    ) {
        return ResponseEntity.ok(signService.signUp(request));
    }

    @PostMapping("/sign-up/social")
    public ResponseEntity<SignUpResponse> socialSignUp(
        @RequestBody @Valid final SocialSignUpRequest request,
        final HttpServletResponse response
    ) {
        return ResponseEntity.ok(signService.socialSignUp(request, response));
    }

    @GetMapping
    public ResponseEntity<UserResponse> whoAmI(
        @LoginUser final String userId
    ) {
        return ResponseEntity.ok(userQuery.whoAmI(userId));
    }
}
