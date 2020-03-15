package com.persoff68.fatodo.security.oauth2.userinfo;

import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.security.exception.OAuth2ProviderNotSupportedException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String provider, Map<String, Object> attributes) {
        provider = provider.toUpperCase();

        if (provider.equals(Provider.Constants.GOOGLE_VALUE)) {
            return GoogleOAuth2UserInfo.from(attributes);
        }
        if (provider.equals(Provider.Constants.FACEBOOK_VALUE)) {
            return FacebookOAuth2UserInfo.from(attributes);
        }

        throw new OAuth2ProviderNotSupportedException(provider);
    }
}
