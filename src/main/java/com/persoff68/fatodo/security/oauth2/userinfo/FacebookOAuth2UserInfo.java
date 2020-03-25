package com.persoff68.fatodo.security.oauth2.userinfo;

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
    public String getImageUrl() {
        if (attributes.containsKey("picture")) {
            Map<String, Object> pictureObj = castToMap(attributes.get("picture"));
            if (pictureObj != null && pictureObj.containsKey("data")) {
                Map<String, Object> dataObj = castToMap(pictureObj.get("data"));
                if (dataObj != null && dataObj.containsKey("url")) {
                    return (String) dataObj.get("url");
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> castToMap(Object object) {
        return object instanceof HashMap ? (HashMap<String, Object>) object : null;
    }

}
