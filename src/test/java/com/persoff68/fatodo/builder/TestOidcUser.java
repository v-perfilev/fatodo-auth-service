package com.persoff68.fatodo.builder;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestOidcUser {
    private static final String DEFAULT_VALUE = "test_value";

    public static OidcUser create(String name) {
        String handledName = name != null ? name : DEFAULT_VALUE;
        List<? extends GrantedAuthority> authorityList = Collections.singletonList(new SimpleGrantedAuthority(
                "ROLE_USER"));
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put("sub", handledName);
        attributeMap.put("name", handledName);
        attributeMap.put("email", handledName + "@email.com");
        OidcIdToken token = new OidcIdToken("test", Instant.now(), Instant.MAX, attributeMap);
        return new DefaultOidcUser(authorityList, token);
    }

}
