package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import lombok.Builder;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class TestUserPrincipleDTO extends UserPrincipalDTO {
    private static final String DEFAULT_VALUE = "test_value";

    @Builder
    TestUserPrincipleDTO(UUID id, String email, String username, String password, String provider, String providerId, Set<String> authorities, String language, boolean activated) {
        super(email, username, password, provider, providerId, authorities, language, activated);
        this.setId(id);
    }

    public static TestUserPrincipleDTOBuilder defaultBuilder() {
        return TestUserPrincipleDTO.builder()
                .id(UUID.randomUUID())
                .email(DEFAULT_VALUE)
                .username(DEFAULT_VALUE)
                .password(DEFAULT_VALUE)
                .provider(Provider.LOCAL.getValue())
                .providerId(DEFAULT_VALUE)
                .authorities(Collections.singleton("ROLE_USER"))
                .language(DEFAULT_VALUE)
                .activated(true);
    }
}
