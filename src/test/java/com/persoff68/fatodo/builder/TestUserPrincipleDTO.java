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
    TestUserPrincipleDTO(UUID id,
                         String email,
                         String username,
                         Set<String> authorities,
                         String password,
                         String provider,
                         String providerId,
                         boolean activated,
                         Info info,
                         Settings settings) {
        super(email, username, authorities, password, provider, providerId, activated, info, settings);
        this.setId(id);
    }

    public static TestUserPrincipleDTOBuilder defaultBuilder() {
        return TestUserPrincipleDTO.builder()
                .id(UUID.randomUUID())
                .email(DEFAULT_VALUE + "@email.com")
                .username(DEFAULT_VALUE)
                .password(DEFAULT_VALUE)
                .provider(Provider.LOCAL.getValue())
                .providerId(DEFAULT_VALUE)
                .authorities(Collections.singleton("ROLE_USER"))
                .activated(true);
    }
}
