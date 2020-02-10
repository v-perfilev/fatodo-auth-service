package com.persoff68.fatodo.security.oauth2user;

import java.util.HashMap;
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
        if (attributes.containsKey("picture")) {
            Map<String, Object> pictureObj = castToMap(attributes.get("picture"));
            if (pictureObj.containsKey("data")) {
                Map<String, Object> dataObj = castToMap(pictureObj.get("data"));
                if (dataObj.containsKey("url")) {
                    return (String) dataObj.get("url");
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> castToMap(Object object) throws ClassCastException {
        return object instanceof HashMap ? (HashMap<String, Object>) object : null;
    }

}
