package com.boardgaming.api.application.user.command;

import com.boardgaming.api.application.auth.query.VerificationQuery;
import com.boardgaming.api.application.file.FileService;
import com.boardgaming.common.exception.user.InvalidPasswordConfirmException;
import com.boardgaming.common.exception.user.NotFoundUserException;
import com.boardgaming.domain.user.domain.User;
import com.boardgaming.domain.user.domain.repository.UserRepository;
import com.boardgaming.domain.user.dto.request.ChangePasswordRequest;
import com.boardgaming.domain.user.dto.request.ChangeProfileRequest;
import com.boardgaming.domain.userHistory.domain.repository.GomokuUserHistoryRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final VerificationQuery verificationQuery;
    private final PasswordEncoder passwordEncoder;
    private final GomokuUserHistoryRepository gomokuUserHistoryRepository;
    private final FileService fileService;
    private final static String USER_IMAGE_KEY_PREFIX = "user-image";

    public UserService(
        final UserRepository userRepository,
        final VerificationQuery verificationQuery,
        final PasswordEncoder passwordEncoder,
        final GomokuUserHistoryRepository gomokuUserHistoryRepository,
        final FileService fileService
    ) {
        this.userRepository = userRepository;
        this.verificationQuery = verificationQuery;
        this.passwordEncoder = passwordEncoder;
        this.gomokuUserHistoryRepository = gomokuUserHistoryRepository;
        this.fileService = fileService;
    }

    @Transactional
    public void saveWithoutDuplicateId(final User user) {
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            user.regenerateId();
            saveWithoutDuplicateId(user);
        }
    }

    @Transactional
    public void changePassword(
        final ChangePasswordRequest request
    ) {
        verificationQuery.checkEmailVerification(request.getEmail(), request.getSessionKey());
        checkPasswordConfirm(request);
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(NotFoundUserException::new);

        user.updatePassword(passwordEncoder.encode(request.getPassword()));
    }

    @Transactional
    public void changeUserImage(
        final String userId,
        final MultipartFile file
    ) {
       User user = userRepository.findById(userId)
           .orElseThrow(NotFoundUserException::new);

       String oldFileName = fileService.getKey(user.getImageFileUrl());
       String newFileName = getUserImageFileName(userId);

       fileService.putObject(newFileName, file);

       user.updateImageFileUrl(fileService.getUrl(newFileName));
       if (Objects.nonNull(oldFileName)) {
           fileService.deleteObject(oldFileName);
       }
    }

    public String getUserImageFileName(final String userId) {
        return String.join("-", USER_IMAGE_KEY_PREFIX, userId, String.valueOf(System.currentTimeMillis()));
    }

    @Transactional
    public void changeProfile(
        final String userId,
        final ChangeProfileRequest request
    ) {
        User user = userRepository.findById(userId)
            .orElseThrow(NotFoundUserException::new);

        user.updateProfile(request);
    }

    private static void checkPasswordConfirm(final ChangePasswordRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidPasswordConfirmException();
        }
    }

    @Transactional
    public void deleteUsers(final Collection<String> userId) {
        gomokuUserHistoryRepository.deleteAllByUserIdIn(userId);

        userRepository.deleteAllByIdInBatch(userId);
    }
}
