package com.persoff68.fatodo.security.oauth2.handler;

import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.security.jwt.JwtTokenProvider;
import com.persoff68.fatodo.security.oauth2.repository.CookieAuthorizationRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final String TOKEN_PARAM = "token";

    private final JwtTokenProvider jwtTokenProvider;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String redirect = cookieAuthorizationRequestRepository.loadRedirect(request);
        if (response.isCommitted() || redirect == null) {
            return;
        }
        clearAuthenticationAttributes(request, response);

        OAuth2AuthenticationToken oAuth2Authentication = (OAuth2AuthenticationToken) authentication;
        UserPrincipal userPrincipal = (UserPrincipal) oAuth2Authentication.getPrincipal();
        User user = new User(userPrincipal.getUsername(), "", userPrincipal.getAuthorities());
        String jwt = jwtTokenProvider.createUserJwt(userPrincipal.getId(), user);

        String targetUrl = buildUrlWithToken(redirect, jwt);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

        log.info("OAuth2 authentication succeed: username {}", userPrincipal.getUsername());
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private String buildUrlWithToken(String redirect, String jwt) {
        return UriComponentsBuilder.fromUriString(redirect)
                .queryParam(TOKEN_PARAM, jwt)
                .build().toUriString();
    }

}
