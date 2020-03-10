package com.persoff68.fatodo;

import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FactoryUtils {

    public static OAuth2User createOAuth2User(String id) {
        List<? extends GrantedAuthority> authorityList = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        Map<String, Object> attributeMap = new HashMap<>();
        attributeMap.put("id", "test_id_" + id);
        attributeMap.put("name", "test_name" + id);
        attributeMap.put("email", "test_" + id + "@email.com");
        String nameAttributeKey = "id";
        return new DefaultOAuth2User(authorityList, attributeMap, nameAttributeKey);
    }

    public static UserDTO createUserDTO(int id, String provider) {
        Set<String> authorityList = Collections.singleton("ROLE_USER");
        UserDTO userDTO = new UserDTO();
        userDTO.setId("test_id_" + id);
        userDTO.setUsername("test_" + id + "@email.com");
        userDTO.setEmail("test_" + id + "@email.com");
        userDTO.setProvider(provider);
        userDTO.setAuthorities(authorityList);
        return userDTO;
    }

    public static UserPrincipalDTO createUserPrincipalDTO(int id, String provider) {
        Set<String> authorityList = Collections.singleton("ROLE_USER");
        UserPrincipalDTO userPrincipalDTO = new UserPrincipalDTO();
        userPrincipalDTO.setId("test_id_" + id);
        userPrincipalDTO.setUsername("test_" + id + "@email.com");
        userPrincipalDTO.setEmail("test_" + id + "@email.com");
        userPrincipalDTO.setProvider(provider);
        userPrincipalDTO.setAuthorities(authorityList);
        return userPrincipalDTO;
    }

    public static OAuth2UserRequest createUserRequest(String provider) {
        ClientRegistration clientRegistration = CommonOAuth2Provider.valueOf(provider).getBuilder(provider)
                .clientId("test_client_id")
                .clientSecret("test_client_secret")
                .build();
        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                "test_token_value",
                Instant.now(),
                Instant.MAX);
        return new OAuth2UserRequest(clientRegistration, oAuth2AccessToken);
    }

}
