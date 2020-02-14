package com.persoff68.fatodo.security.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtils {

    public static void addTokenToResponse(HttpServletResponse response, String header, String token) throws IOException {
        response.setStatus(200);
        response.setHeader(header, token);
        response.getWriter().write(token);
    }

}
