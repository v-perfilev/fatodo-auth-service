package com.persoff68.fatodo.security.oauth2.userinfo;

import com.persoff68.fatodo.config.constant.Providers;
import com.persoff68.fatodo.security.exception.OAuth2ProviderNotSupportedProblem;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String provider, Map<String, Object> attributes) {
        provider = provider.toUpperCase();

        if (provider.equals(Providers.GOOGLE)) {
            return GoogleOAuth2UserInfo.from(attributes);
        }
        if (provider.equals(Providers.FACEBOOK)) {
            return FacebookOAuth2UserInfo.from(attributes);
        }

        throw new OAuth2ProviderNotSupportedProblem(provider);
    }
}
