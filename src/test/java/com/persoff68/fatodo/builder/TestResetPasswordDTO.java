package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.dto.ResetPasswordDTO;
import lombok.Builder;

import java.util.UUID;

public class TestResetPasswordDTO extends ResetPasswordDTO {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    TestResetPasswordDTO(UUID userId, String password) {
        super(userId, password);
    }

    public static TestResetPasswordDTOBuilder defaultBuilder() {
        return TestResetPasswordDTO.builder()
                .userId(UUID.randomUUID())
                .password(DEFAULT_VALUE);
    }

}
