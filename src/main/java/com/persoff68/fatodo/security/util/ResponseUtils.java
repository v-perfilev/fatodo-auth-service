package com.persoff68.fatodo.security.util;

import com.persoff68.fatodo.config.AppProperties;
import org.springframework.http.HttpHeaders;

public class ResponseUtils {

    private ResponseUtils() {
    }

    public static HttpHeaders createHeaderWithJwt(AppProperties.Auth auth, String jwt) {
        String header = auth.getAuthorizationHeader();
        String prefix = auth.getAuthorizationPrefix();
        String fullToken = prefix + " " + jwt;
        HttpHeaders headers = new HttpHeaders();
        headers.set(header, fullToken);
        return headers;
    }

}
