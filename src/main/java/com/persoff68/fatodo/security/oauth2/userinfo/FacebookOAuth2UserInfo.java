package com.persoff68.fatodo.security.oauth2.userinfo;

import java.util.Map;

public class FacebookOAuth2UserInfo extends OAuth2UserInfo {

    private FacebookOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    public static FacebookOAuth2UserInfo from(Map<String, Object> attributes) {
        return new FacebookOAuth2UserInfo(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

}
