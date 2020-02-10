package com.persoff68.fatodo.security;

import com.persoff68.fatodo.config.AppProperties;
import com.persoff68.fatodo.exception.OAuth2InternalException;
import com.persoff68.fatodo.security.jwt.JwtTokenProvider;
import com.persoff68.fatodo.security.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AppProperties appProperties;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieAuthorizationRequestRepository CookieAuthorizationRequestRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (response.isCommitted()) {
            return;
        }
        clearAuthenticationAttributes(request, response);
        String token = jwtTokenProvider.createToken(authentication);
        ResponseUtils.addTokenToResponse(response, token);
        String targetUrl = determineTargetUrl(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        CookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    protected String determineTargetUrl(HttpServletRequest request) {
        Optional<String> redirectUri = CookieAuthorizationRequestRepository.loadRedirectURI(request);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new OAuth2InternalException();
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        return UriComponentsBuilder.fromUriString(targetUrl).build().toUriString();
    }

    private boolean isAuthorizedRedirectUri(String value) {
        URI clientUri = URI.create(value);
        List<String> authorizedUriList = appProperties.getOAuth2().getAuthorizedRedirectUris();

        return authorizedUriList.stream().anyMatch(uri -> {
            URI authorizedUri = URI.create(uri);
            boolean isHostValid = authorizedUri.getHost().equalsIgnoreCase(clientUri.getHost());
            boolean isPortValid = authorizedUri.getPort() == clientUri.getPort();
            return isHostValid && isPortValid;
        });
    }
}
