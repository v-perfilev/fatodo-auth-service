package com.persoff68.fatodo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        String targetUrl = cookieAuthorizationRequestRepository.loadRedirectURI(request);
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
//        targetUrl = UriComponentsBuilder.fromUriString(targetUrl).build().toUriString();
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

}

