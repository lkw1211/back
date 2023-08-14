package com.boardgaming.domain.auth.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class SocialSignUpRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("유효성 체크 실패 테스트")
    void test1() {
        //given
        SocialSignUpRequest request = new SocialSignUpRequest();

        //when
        Set<ConstraintViolation<SocialSignUpRequest>> violations = validator.validate(request);

        //then
        assertThat(violations).hasSize(2);
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("name"));
        assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals("key"));
    }

    @Test
    @DisplayName("유효성 체크 성공 테스트")
    void test2() throws NoSuchFieldException, IllegalAccessException {
        //given
        SocialSignUpRequest request = new SocialSignUpRequest();

        Field nameField = SocialSignUpRequest.class.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(request, "John Doe");

        Field keyField = SocialSignUpRequest.class.getDeclaredField("key");
        keyField.setAccessible(true);
        keyField.set(request, "keykeykey");

        //when
        Set<ConstraintViolation<SocialSignUpRequest>> violations = validator.validate(request);

        //then
        assertThat(violations).isEmpty();
    }
}