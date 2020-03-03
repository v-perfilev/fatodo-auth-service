package com.persoff68.fatodo.security.util;

import com.persoff68.fatodo.config.AppProperties;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtils {

    public static void addJwtToResponse(HttpServletResponse response, AppProperties.Auth auth, String jwt) throws IOException {
        String header = auth.getAuthorizationHeader();
        String prefix = auth.getAuthorizationPrefix();
        String fullToken = prefix + " " + jwt;
        response.setStatus(200);
        response.setHeader(header, fullToken);
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
