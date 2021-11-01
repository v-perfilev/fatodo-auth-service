package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.ResetPasswordMail;
import lombok.Builder;

import java.util.UUID;

public class TestResetPasswordMail extends ResetPasswordMail {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    public TestResetPasswordMail(String language, String email, String username, UUID code) {
        super(language, email, username, code);
    }

    public static TestResetPasswordMailBuilder defaultBuilder() {
        return TestResetPasswordMail.builder()
                .language(DEFAULT_VALUE)
                .email(DEFAULT_VALUE + "@email.com")
                .username(DEFAULT_VALUE)
                .code(UUID.randomUUID());
    }

}
