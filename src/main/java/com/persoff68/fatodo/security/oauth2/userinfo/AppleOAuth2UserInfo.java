package com.persoff68.fatodo.security.oauth2.userinfo;

import java.util.Map;

public class AppleOAuth2UserInfo extends OAuth2UserInfo {

    private AppleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    public static AppleOAuth2UserInfo from(Map<String, Object> attributes) {
        return new AppleOAuth2UserInfo(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getImageUrl() {
        return null;
    }
}
