package com.boardgaming.api.application.user.query;

import com.boardgaming.common.exception.user.DuplicateEmailException;
import com.boardgaming.common.exception.user.NotFoundUserException;
import com.boardgaming.domain.user.domain.Role;
import com.boardgaming.domain.user.domain.User;
import com.boardgaming.domain.user.domain.repository.UserRepository;
import com.boardgaming.domain.auth.dto.request.SignUpRequest;
import com.boardgaming.domain.user.dto.response.ExistsCheckResponse;
import com.boardgaming.domain.user.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserQuery {
    private final UserRepository userRepository;

    public ExistsCheckResponse checkNameExists(final String name) {
        return ExistsCheckResponse.of(userRepository.existsByName(name));
    }

    public ExistsCheckResponse checkEmailExists(final String email) {
        return ExistsCheckResponse.of(userRepository.existsByEmail(email));
    }

    public void checkEmailDuplication(final SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new DuplicateEmailException();
        }
    }

    public UserResponse whoAmI(final String userId) {
        return getUserInfo(userId);
    }

    private UserResponse getUserInfo(final String userId) {
        return UserResponse.of(
            userRepository.findById(userId)
                .orElseThrow(NotFoundUserException::new));
    }
}
