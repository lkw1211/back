package com.boardgaming.domain.user.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SendEmailVerificationCodeRequestTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    @DisplayName("유효성 체크 실패 test")
    void test1() {
        //given
        SendEmailVerificationCodeRequest request = new SendEmailVerificationCodeRequest();
        List<String> violationPropertyPaths = List.of("email", "sessionKey");

        //when
        Set<ConstraintViolation<SendEmailVerificationCodeRequest>> violations = validator.validate(request);

        //then
        assertThat(violations).hasSize(violationPropertyPaths.size());
        violationPropertyPaths.forEach(violationPropertyPath -> {
            assertThat(violations).anyMatch(violation -> violation.getPropertyPath().toString().equals(violationPropertyPath));
        });
    }
}