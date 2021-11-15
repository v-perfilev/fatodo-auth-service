package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.dto.LocalUserDTO;
import lombok.Builder;

public class TestLocalUserDTO extends LocalUserDTO {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    TestLocalUserDTO(String email, String username, String password, String language, String timezone) {
        super(email, username, password, language, timezone);
    }

    public static TestLocalUserDTOBuilder defaultBuilder() {
        return TestLocalUserDTO.builder()
                .email(DEFAULT_VALUE + "@email.com")
                .username(DEFAULT_VALUE)
                .password(DEFAULT_VALUE)
                .language(DEFAULT_VALUE)
                .timezone(DEFAULT_VALUE);
    }

}
