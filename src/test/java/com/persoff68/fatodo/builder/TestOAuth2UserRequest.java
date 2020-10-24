package com.persoff68.fatodo.builder;

import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

import java.time.Instant;

public class TestOAuth2UserRequest {

    public static OAuth2UserRequest create(String provider) {
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
