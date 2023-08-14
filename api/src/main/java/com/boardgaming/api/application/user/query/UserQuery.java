package com.boardgaming.api.application.user.query;

import com.boardgaming.common.exception.user.DuplicateEmailException;
import com.boardgaming.domain.auth.dto.request.SignUpRequest;
import com.boardgaming.domain.user.domain.repository.UserRepository;
import com.boardgaming.domain.user.domain.repository.UserRepositoryCustom;
import com.boardgaming.domain.user.dto.response.ExistsCheckResponse;
import com.boardgaming.domain.user.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserQuery {
    private final UserRepository userRepository;
    private final UserRepositoryCustom userRepositoryCustom;

    public ExistsCheckResponse checkNameExists(final String name) {
        return new ExistsCheckResponse(userRepository.existsByName(name));
    }

    public ExistsCheckResponse checkEmailExists(final String email) {
        return new ExistsCheckResponse(userRepository.existsByEmail(email));
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
        return userRepositoryCustom.findById(userId);
    }
}
