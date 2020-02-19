package com.persoff68.fatodo.security.oauth2user;

import com.persoff68.fatodo.exception.OAuth2ProviderNotSupportedException;
import com.persoff68.fatodo.model.constant.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    private static final String GOOGLE = AuthProvider.GOOGLE.toString();
    private static final String FACEBOOK = AuthProvider.FACEBOOK.toString();

    public static OAuth2UserInfo getOAuth2UserInfo(String provider, Map<String, Object> attributes) {
        provider = provider.toUpperCase();

        if (provider.equals(GOOGLE)) {
            return GoogleOAuth2UserInfo.from(attributes);
        }
        if (provider.equals(FACEBOOK)) {
            return FacebookOAuth2UserInfo.from(attributes);
        }

        throw new OAuth2ProviderNotSupportedException(provider);
    }
}
