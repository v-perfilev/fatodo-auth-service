package com.persoff68.fatodo.security.oauth2.handler;

import com.persoff68.fatodo.security.exception.OAuth2InternalException;
import com.persoff68.fatodo.security.oauth2.repository.CookieAuthorizationRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) {
        String redirectUri = getRedirectFromCookies();
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        log.warn("OAuth2 authentication failed: {}", exception.getMessage());
        throw new OAuth2InternalException(redirectUri);
    }

    private String getRedirectFromCookies() {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes != null
                ? cookieAuthorizationRequestRepository.loadRedirect(requestAttributes.getRequest())
                : null;
    }

}

