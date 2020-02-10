package com.persoff68.fatodo.security.oauth2user;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    private GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    public static GoogleOAuth2UserInfo from(Map<String, Object> attributes) {
        return new GoogleOAuth2UserInfo(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }
}
