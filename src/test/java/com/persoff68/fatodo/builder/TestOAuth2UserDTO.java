package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import lombok.Builder;

public class TestOAuth2UserDTO extends OAuth2UserDTO {
    private static final String DEFAULT_VALUE = "test";

    @Builder
    TestOAuth2UserDTO(String email, String username, String provider, String providerId, String language) {
        super(email, username, provider, providerId, language);
    }

    public static TestOAuth2UserDTOBuilder defaultBuilder() {
        return TestOAuth2UserDTO.builder()
                .email(DEFAULT_VALUE)
                .username(DEFAULT_VALUE)
                .provider(DEFAULT_VALUE)
                .providerId(DEFAULT_VALUE)
                .language(DEFAULT_VALUE);
    }

}
