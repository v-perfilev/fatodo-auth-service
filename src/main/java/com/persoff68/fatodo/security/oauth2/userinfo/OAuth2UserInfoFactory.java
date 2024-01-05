package com.persoff68.fatodo.security.oauth2.userinfo;

import com.persoff68.fatodo.config.constant.Provider;
import com.persoff68.fatodo.security.exception.OAuth2InternalException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    private OAuth2UserInfoFactory() {
    }

    public static OAuth2UserInfo getOAuth2UserInfo(String redirectUri,
                                                   String provider,
                                                   Map<String, Object> attributes) {
        provider = provider.toUpperCase();

        if (provider.equals(Provider.Constants.GOOGLE_VALUE)) {
            return GoogleOAuth2UserInfo.from(attributes);
        }
        if (provider.equals(Provider.Constants.APPLE_VALUE)) {
            return AppleOAuth2UserInfo.from(attributes);
        }

        throw new OAuth2InternalException(redirectUri);
    }
}
