package com.boardgaming.domain.auth.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("유효성 체크 실패 test")
    void test1() {
        //given
        LoginRequest request = new LoginRequest();

        //when
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        //then
        assertThat(violations).hasSize(2);
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("email"));
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("password"));
    }

    @Test
    @DisplayName("유효성 체크 성공 test")
    void test2() throws NoSuchFieldException, IllegalAccessException {
        //given
        LoginRequest request = new LoginRequest();
        Field emailField = LoginRequest.class.getDeclaredField("email");
        emailField.setAccessible(true);
        emailField.set(request, "user@example.com");

        Field passwordField = LoginRequest.class.getDeclaredField("password");
        passwordField.setAccessible(true);
        passwordField.set(request, "password123");

        //when
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        //then
        assertThat(violations).isEmpty();
    }
}