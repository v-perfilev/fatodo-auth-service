package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import lombok.Builder;

public class BuildableOAuth2UserDTO extends OAuth2UserDTO {
    private static final String DEFAULT_VALUE = "test";

    @Builder
    public BuildableOAuth2UserDTO(String email, String username, String provider, String providerId, String language) {
        super(email, username, provider, providerId, language);
        this.setEmail(DEFAULT_VALUE);
        this.setUsername(DEFAULT_VALUE);
        this.setProvider(DEFAULT_VALUE);
        this.setProviderId(DEFAULT_VALUE);
        this.setLanguage(DEFAULT_VALUE);
    }

}
