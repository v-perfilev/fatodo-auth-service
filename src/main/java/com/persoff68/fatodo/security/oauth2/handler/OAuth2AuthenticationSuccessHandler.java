package com.persoff68.fatodo.security.oauth2.handler;

import com.persoff68.fatodo.config.AppProperties;
import com.persoff68.fatodo.model.UserPrincipal;
import com.persoff68.fatodo.repository.CookieAuthorizationRequestRepository;
import com.persoff68.fatodo.security.jwt.JwtTokenProvider;
import com.persoff68.fatodo.security.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        OAuth2AuthenticationToken oAuth2Authentication = (OAuth2AuthenticationToken) authentication;
        UserPrincipal userPrincipal = (UserPrincipal) oAuth2Authentication.getPrincipal();
        User user = new User(userPrincipal.getUsername(), "", userPrincipal.getAuthorities());
        String jwt = jwtTokenProvider.createUserJwt(userPrincipal.getId(), user);
        ResponseUtils.addJwtToResponse(response, appProperties.getAuth(), jwt);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        CookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

}
