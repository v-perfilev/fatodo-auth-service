package com.persoff68.fatodo.builder;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestOAuth2User {
    private static final String DEFAULT_VALUE = "test_value";

    public static OAuth2User create(String name) {
        String handledName = name != null ? name : DEFAULT_VALUE;
        List<? extends GrantedAuthority> authorityList = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put("id", handledName);
        attributeMap.put("name", handledName);
        attributeMap.put("email", handledName + "@email.com");
        String nameAttributeKey = "id";
        return new DefaultOAuth2User(authorityList, attributeMap, nameAttributeKey);
    }

}
