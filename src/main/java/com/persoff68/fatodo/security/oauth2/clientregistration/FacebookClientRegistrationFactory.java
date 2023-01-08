package com.persoff68.fatodo.security.oauth2.clientregistration;

import com.persoff68.fatodo.config.constant.Provider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FacebookClientRegistrationFactory implements ClientRegistrationFactory {

    @Value("${spring.security.oauth2.client.registration.facebook.clientId}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.facebook.clientSecret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.facebook.redirectUri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.facebook.scope}")
    private List<String> scope;

    @Override
    public ClientRegistration create() {
        String registrationId = Provider.FACEBOOK.getValue().toLowerCase();
        return CommonOAuth2Provider.FACEBOOK.getBuilder(registrationId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri(redirectUri)
                .scope(scope)
                .build();
    }

}
