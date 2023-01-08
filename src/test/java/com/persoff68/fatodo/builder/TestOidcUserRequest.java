package com.persoff68.fatodo.builder;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class TestOidcUserRequest {

    public static OidcUserRequest create(String provider) {
        ClientRegistration clientRegistration = ClientRegistration
                .withRegistrationId(provider)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .clientId("test_client_id")
                .clientSecret("test_client_secret")
                .redirectUri("https://test_redirect_uri")
                .authorizationUri("https://test_authorization_uri")
                .tokenUri("https://test_token_uri")
                .build();
        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                "test_token_value", Instant.now(), Instant.MAX);
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", "test_value");
        claims.put("name", "test_value");
        claims.put("email", "test_value@email.com");
        OidcIdToken oidcIdToken = new OidcIdToken("test_id_token", Instant.now(), Instant.MAX, claims);
        return new OidcUserRequest(clientRegistration, oAuth2AccessToken, oidcIdToken);
    }

}
