package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.ActivationMail;
import lombok.Builder;

import java.util.UUID;

public class TestActivationMail extends ActivationMail {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    TestActivationMail(String language, String email, String username, UUID code) {
        super(language, email, username, code);
    }

    public static TestActivationMailBuilder defaultBuilder() {
        return TestActivationMail.builder()
                .language(DEFAULT_VALUE)
                .email(DEFAULT_VALUE + "@email.com")
                .username(DEFAULT_VALUE)
                .code(UUID.randomUUID());
    }

}
