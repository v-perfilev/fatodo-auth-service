package com.persoff68.fatodo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();
    private final OAuth2 oAuth2 = new OAuth2();

    @Data
    public static class Auth {
        private String authorizationHeader;
        private String authorizationPrefix;
        private String tokenSecret;
        private long tokenExpirationMSec;
    }

    @Data
    public static final class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();
    }

}

