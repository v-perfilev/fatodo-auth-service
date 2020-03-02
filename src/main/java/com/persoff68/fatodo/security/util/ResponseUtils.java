package com.persoff68.fatodo.security.util;

import com.persoff68.fatodo.config.AppProperties;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtils {

    public static void addTokenToResponse(HttpServletResponse response, AppProperties.Auth auth, String token) throws IOException {
        String header = auth.getAuthorizationHeader();
        String prefix = auth.getAuthorizationPrefix();
        String fullToken = prefix + " " + token;
        response.setStatus(200);
        response.setHeader(header, fullToken);
    }

    public static HttpHeaders createHeaderWithToken(AppProperties.Auth auth, String token) {
        String header = auth.getAuthorizationHeader();
        String prefix = auth.getAuthorizationPrefix();
        String fullToken = prefix + " " + token;
        HttpHeaders headers = new HttpHeaders();
        headers.set(header, fullToken);
        return headers;
    }

}
