package com.boardgaming.api.controller.user;

import com.boardgaming.api.application.file.FileService;
import com.boardgaming.api.application.user.command.UserService;
import com.boardgaming.api.application.user.query.UserQuery;
import com.boardgaming.core.config.annotation.LoginUser;
import com.boardgaming.domain.user.dto.request.ChangePasswordRequest;
import com.boardgaming.domain.user.dto.request.ChangeProfileRequest;
import com.boardgaming.domain.user.dto.response.ExistsCheckResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserQuery userQuery;
    private final FileService fileService;

    @PatchMapping("/change/password")
    public ResponseEntity<Void> changePassword(
        @RequestBody @Valid final ChangePasswordRequest request
    ) {
        userService.changePassword(request);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/change/image")
    public ResponseEntity<Void> changeUserImage(
        @LoginUser final String userId,
        @RequestParam("file") final MultipartFile file
    ) {
        userService.changeUserImage(userId, file);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/change/profile")
    public ResponseEntity<Void> changeProfile(
        @LoginUser final String userId,
        @RequestBody @Valid final ChangeProfileRequest request
    ) {
        userService.changeProfile(userId, request);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check/name")
    public ResponseEntity<ExistsCheckResponse> checkNameExists(
        @RequestParam(name = "name") final String name
    ) {
        return ResponseEntity.ok(userQuery.checkNameExists(name));
    }

    @GetMapping("/check/email")
    public ResponseEntity<ExistsCheckResponse> checkEmailExists(
        @RequestParam(name = "email") final String email
    ) {
        return ResponseEntity.ok(userQuery.checkEmailExists(email));
    }
}
