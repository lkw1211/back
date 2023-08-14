package com.boardgaming.domain.auth.dto.request;

import com.boardgaming.domain.user.domain.Role;
import com.boardgaming.domain.user.domain.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class SignUpRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("유효성 체크 실패 테스트")
    void test1() {
        //given
        SignUpRequest request = new SignUpRequest();

        //when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(request);

        //then
        assertThat(violations).hasSize(5);
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("name"));
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("email"));
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("sessionKey"));
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("password"));
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("confirmPassword"));
    }

    @Test
    @DisplayName("유효성 체크 성공 테스트")
    void test2() throws NoSuchFieldException, IllegalAccessException {
        //given
        SignUpRequest signUpRequest = new SignUpRequest();
        Field nameField = SignUpRequest.class.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(signUpRequest, "John Doe");

        Field emailField = SignUpRequest.class.getDeclaredField("email");
        emailField.setAccessible(true);
        emailField.set(signUpRequest, "john@example.com");

        Field passwordField = SignUpRequest.class.getDeclaredField("password");
        passwordField.setAccessible(true);
        passwordField.set(signUpRequest, "password123");

        Field sessionkeyField = SignUpRequest.class.getDeclaredField("sessionKey");
        sessionkeyField.setAccessible(true);
        sessionkeyField.set(signUpRequest, "sessionKey");

        Field confirmPasswordField = SignUpRequest.class.getDeclaredField("confirmPassword");
        confirmPasswordField.setAccessible(true);
        confirmPasswordField.set(signUpRequest, "password123");

        //when
        Set<ConstraintViolation<SignUpRequest>> violations = validator.validate(signUpRequest);

        //then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("toEntity 테스트")
    void test3() throws NoSuchFieldException, IllegalAccessException {
        SignUpRequest signUpRequest = new SignUpRequest();
        Field nameField = signUpRequest.getClass().getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(signUpRequest, "John Doe");

        Field emailField = signUpRequest.getClass().getDeclaredField("email");
        emailField.setAccessible(true);
        emailField.set(signUpRequest, "john@example.com");

        Field passwordField = signUpRequest.getClass().getDeclaredField("password");
        passwordField.setAccessible(true);
        passwordField.set(signUpRequest, "password123");

        User userEntity = signUpRequest.toEntity(signUpRequest.getPassword());

        assertThat(userEntity.getName()).isEqualTo(signUpRequest.getName());
        assertThat(userEntity.getEmail()).isEqualTo(signUpRequest.getEmail());
        assertThat(userEntity.getPassword()).isEqualTo(signUpRequest.getPassword());
        assertThat(userEntity.getImageFileUrl()).isEqualTo(null);
        assertThat(userEntity.getProvider()).isEqualTo(null);
        assertThat(userEntity.getRole()).isEqualTo(Role.USER);
    }
}