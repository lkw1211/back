package com.boardgaming.api.application.auth.command;

import com.boardgaming.api.application.auth.query.VerificationQuery;
import com.boardgaming.api.application.file.FileService;
import com.boardgaming.api.application.user.command.UserService;
import com.boardgaming.api.application.user.query.UserQuery;
import com.boardgaming.common.exception.auth.InvalidOAuth2TempKeyException;
import com.boardgaming.common.exception.user.InvalidPasswordConfirmException;
import com.boardgaming.common.exception.user.NotFoundUserException;
import com.boardgaming.core.config.auth.application.TokenService;
import com.boardgaming.core.util.CookieUtil;
import com.boardgaming.domain.auth.domain.OAuth2Attributes;
import com.boardgaming.domain.auth.domain.OAuth2TempAttributes;
import com.boardgaming.domain.auth.domain.TokenType;
import com.boardgaming.domain.auth.domain.repository.OAuth2TempAttributesRepository;
import com.boardgaming.domain.auth.dto.request.LoginRequest;
import com.boardgaming.domain.auth.dto.request.SignUpRequest;
import com.boardgaming.domain.auth.dto.request.SocialSignUpRequest;
import com.boardgaming.domain.auth.dto.response.SignUpResponse;
import com.boardgaming.domain.auth.dto.response.TokenResponse;
import com.boardgaming.domain.user.domain.User;
import com.boardgaming.domain.user.domain.repository.UserRepository;
import com.boardgaming.domain.user.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class SignService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final String domain;
    private final String sameSite;
    private final VerificationQuery verificationQuery;
    private final UserQuery userQuery;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final OAuth2TempAttributesRepository oAuth2TempAttributesRepository;
    private final FileService fileService;

    public SignService(
        final AuthenticationManagerBuilder authenticationManagerBuilder,
        final TokenService tokenService,
        final UserRepository userRepository,
        @Value("${custom.cookieDomain}") final String domain,
        @Value("${custom.sameSite}") final String sameSite,
        final VerificationQuery verificationQuery,
        final UserQuery userQuery,
        final UserService userService,
        final PasswordEncoder passwordEncoder,
        final OAuth2TempAttributesRepository oAuth2TempAttributesRepository,
        final FileService fileService
    ) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.domain = domain;
        this.sameSite = sameSite;
        this.verificationQuery = verificationQuery;
        this.userQuery = userQuery;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.oAuth2TempAttributesRepository = oAuth2TempAttributesRepository;
        this.fileService = fileService;
    }

    @Transactional(readOnly = true)
    public UserResponse signIn(
        final HttpServletResponse response,
        final LoginRequest request
    ) {
        signIn(response, getAuthentication(request.getEmail(), request.getPassword()));

        return getUserInfo(request);
    }

    private Authentication getAuthentication(
        final String email,
        final String password
    ) {
        AuthenticationManager authenticationManager = authenticationManagerBuilder.getObject();
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    @Transactional(readOnly = true)
    public void signIn(
        final HttpServletResponse response,
        final Authentication authenticate
    ) {
        TokenResponse tokenResponse = tokenService.createTokenResponse(authenticate);
        tokenService.saveRefreshToken(authenticate.getName(), tokenResponse.refreshToken());

        CookieUtil.addCookie(
            response,
            TokenType.ACCESS.name(),
            tokenResponse.accessToken(),
            tokenResponse.accessTokenLifeTime() / 1000,
            domain,
            sameSite
        );

        CookieUtil.addCookie(
            response,
            TokenType.REFRESH.name(),
            tokenResponse.refreshToken(),
            tokenResponse.refreshTokenLifeTime() / 1000,
            domain,
            sameSite
        );
    }

    public void signOut(
        final String userId,
        final HttpServletRequest request,
        final HttpServletResponse response
    ) {
        CookieUtil.deleteCookie(
            request,
            response,
            TokenType.ACCESS.name(),
            domain,
            sameSite
        );

        CookieUtil.deleteCookie(
            request,
            response,
            TokenType.REFRESH.name(),
            domain,
            sameSite
        );

        tokenService.deleteRefreshToken(userId);
    }

    @Transactional
    public SignUpResponse signUp(final SignUpRequest request) {
        userQuery.checkEmailDuplication(request);
        verificationQuery.checkEmailVerification(request.getEmail(), request.getSessionKey());
        checkPasswordConfirm(request);
        User user = request.toEntity(passwordEncoder.encode(request.getPassword()));
        userService.saveWithoutDuplicateId(user);

        return SignUpResponse.of(user);
    }

    private static void checkPasswordConfirm(final SignUpRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidPasswordConfirmException();
        }
    }

    private UserResponse getUserInfo(final LoginRequest request) {
        return UserResponse.of(
            userRepository.findByEmail(request.getEmail())
                .orElseThrow(NotFoundUserException::new));
    }

    @Transactional
    public SignUpResponse socialSignUp(
        final SocialSignUpRequest request,
        final HttpServletResponse response
    ) {
        OAuth2Attributes oAuth2Attributes = oAuth2TempAttributesRepository.findById(request.getKey())
            .map(OAuth2TempAttributes::toOAuth2Attributes)
            .orElseThrow(InvalidOAuth2TempKeyException::new);

        String password = UUID.randomUUID().toString();

        User user = oAuth2Attributes.toUser(
            request.getName(),
            passwordEncoder.encode(password));

        userService.saveWithoutDuplicateId(user);

        // OAuth2에서 받은 이미지 파일서버에 저장 후 url 변경
        String newImageFileUrl = fileService.uploadAndGetUrl(
            userService.getUserImageFileName(user.getId()),
            oAuth2Attributes.getProfileImageUrl()
        );

        user.updateImageFileUrl(newImageFileUrl);

        userRepository.save(user);

        oAuth2TempAttributesRepository.deleteById(request.getKey());
        signIn(response, getAuthentication(user.getEmail(), password));

        return SignUpResponse.of(user);
    }
}
