package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.dto.ResetPasswordMailDTO;
import lombok.Builder;

import java.util.UUID;

public class TestResetPasswordMailDTO extends ResetPasswordMailDTO {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    public TestResetPasswordMailDTO(String language, String email, String username, UUID code) {
        super(language, email, username, code);
    }

    public static TestResetPasswordMailDTOBuilder defaultBuilder() {
        return TestResetPasswordMailDTO.builder()
                .language(DEFAULT_VALUE)
                .email(DEFAULT_VALUE + "@email.com")
                .username(DEFAULT_VALUE)
                .code(UUID.randomUUID());
    }

}
