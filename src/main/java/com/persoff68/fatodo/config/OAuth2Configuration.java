package com.persoff68.fatodo.config;

import com.persoff68.fatodo.repository.CookieAuthorizationRequestRepository;
import com.persoff68.fatodo.security.OAuth2AuthenticationFailureHandler;
import com.persoff68.fatodo.security.OAuth2AuthenticationSuccessHandler;
import com.persoff68.fatodo.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

@Configuration
@RequiredArgsConstructor
public class OAuth2Configuration {

    private final AppProperties appProperties;
    private final JwtTokenProvider jwtTokenProvider;
    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;

    @Bean
    DefaultOAuth2UserService defaultOAuth2UserService() {
        return new DefaultOAuth2UserService();
    }

    @Bean
    OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(appProperties, jwtTokenProvider, cookieAuthorizationRequestRepository);
    }

    @Bean
    OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(cookieAuthorizationRequestRepository);
    }

}
