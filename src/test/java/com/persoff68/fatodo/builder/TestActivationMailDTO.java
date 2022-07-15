package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.dto.ActivationMailDTO;
import lombok.Builder;

import java.util.UUID;

public class TestActivationMailDTO extends ActivationMailDTO {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    TestActivationMailDTO(String language, String email, String username, UUID code) {
        super(language, email, username, code);
    }

    public static TestActivationMailDTOBuilder defaultBuilder() {
        return TestActivationMailDTO.builder()
                .language(DEFAULT_VALUE)
                .email(DEFAULT_VALUE + "@email.com")
                .username(DEFAULT_VALUE)
                .code(UUID.randomUUID());
    }

}
